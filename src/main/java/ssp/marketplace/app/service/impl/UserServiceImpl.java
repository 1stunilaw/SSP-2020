package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.registration.RegisterRequestUserDto;
import ssp.marketplace.app.dto.registration.customer.*;
import ssp.marketplace.app.dto.registration.supplier.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.customer.CustomerDetails;
import ssp.marketplace.app.entity.statuses.StateStatus;
import ssp.marketplace.app.entity.supplier.*;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final ApplicationEventPublisher eventPublisher;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenRepository tokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    private final MailService mailService;

    private final LawStatusRepository lawStatusRepository;

    private final DocumentService documentService;

    private final TagRepository tagRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public UserServiceImpl(
            ApplicationEventPublisher eventPublisher,
            UserRepository userRepository,
            RoleRepository roleRepository,
            TokenRepository tokenRepository,
            TagRepository tagRepository,
            LawStatusRepository lawStatusRepository,
            DocumentService documentService,
            MessageSource messageSource,
            MailService mailService,
            @Lazy JwtTokenProvider jwtTokenProvider
    ) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.tagRepository = tagRepository;
        this.messageSource = messageSource;
        this.mailService = mailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.lawStatusRepository = lawStatusRepository;
        this.documentService = documentService;
    }

    // TODO: 20.12.2020 Убрать лишние логи
    @Override
    public UserResponseDto register(RegisterRequestUserDto registerDto) {
        if (registerDto instanceof CustomerRegisterRequestDto) {
            User user = registerCustomer((CustomerRegisterRequestDto)registerDto);
            log.info("IN register - user: {} successfully registered", user);
            return new CustomerRegisterResponseDto(user);
        } else if (registerDto instanceof SupplierRegisterRequestDto) {
            User user = registerSupplier((SupplierRegisterRequestDto)registerDto);
            sendConfirmationEmail(user, createVerificationToken(user));
            log.info("IN register - user: {} successfully registered", user);
            return new SupplierRegisterResponseDto(user);
        }

        throw new NotFoundException("Role was not found");
    }

    private User registerCustomer(CustomerRegisterRequestDto dto) {
        Role roleAdmin = getRoleFromRepository(RoleName.ROLE_ADMIN);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleAdmin);

//        RegisterDtoMapper mapper = Mappers.getMapper(RegisterDtoMapper.class);
        User user = new User();
        // TODO: 19.11.2020 Переделать создание пользователя через MapStruct
        user.setEmail(dto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setRoles(userRoles);
        if (dto.getSendEmail() != null && "false".equals(dto.getSendEmail())) {
            user.setSendEmail(MailAgreement.NO);
        }
        CustomerDetails details = new CustomerDetails(user, dto.getFio(), dto.getPhone());
        user.setCustomerDetails(details);
        details.setUser(user);

        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    private User registerSupplier(SupplierRegisterRequestDto dto) {
        Role roleUser = getRoleFromRepository(RoleName.ROLE_BLANK_USER);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);

        // TODO: 19.11.2020 Переделать создание пользователя через MapStruct
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setRoles(userRoles);
        SupplierDetails details = new SupplierDetails(user, dto.getCompanyName());

        if (dto.getSendEmail() != null && "false".equals(dto.getSendEmail())) {
            user.setSendEmail(MailAgreement.NO);
        }

        user.setSupplierDetails(details);

        return userRepository.save(user);
    }

    private Role getRoleFromRepository(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role was not found: " + roleName));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с данным email не был найден: " + email));
    }

    @Override
    public User findById(UUID id) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с данным id не был найден: " + id));
        log.info("IN findByEmail - user: {} found by id {}", result, id);
        return result;
    }

    @Override
    public void deleteUser(UUID id) {
        User toDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с данным id не был найден: " + id));
        toDelete.setStatus(UserStatus.DELETED);
        userRepository.save(toDelete);
        log.info("IN deleteUser - user with id {} was successfully disabled", id);
    }


    private VerificationToken createVerificationToken(User user) {
        VerificationToken token = new VerificationToken(user);
        return tokenRepository.save(token);
    }

    @Override
    public void confirmRegister(String token) {
        try {
            UUID uuid = UUID.fromString(token);
            VerificationToken verificationToken = getVerificationToken(uuid);

            if (verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTimeInMillis() <= 0L) {
                tokenRepository.delete(verificationToken);
                throw new BadRequestException("Срок действия токена истёк");
            }

            User user = verificationToken.getUser();
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            tokenRepository.delete(verificationToken);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Невалидный токен");
        }
    }

    private VerificationToken getVerificationToken(UUID token) {
        return tokenRepository.findById(token).orElseThrow(
                () -> new NotFoundException("Токен не найден")
        );
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {

        switch (fieldName) {
            case "email":
                return userRepository.existsByEmail(value.toString());
            case "inn":
                return userRepository.existsBySupplierDetails_Inn(value.toString());
            case "companyName":
                return userRepository.existsBySupplierDetails_CompanyName(value.toString());
            case "supplierPhone":
                return userRepository.existsBySupplierDetails_Phone(value.toString());
            case "customerPhone":
                return userRepository.existsByCustomerDetails_Phone(value.toString());
            default:
                throw new UnsupportedOperationException("Данное поле не поддерживается");
        }
    }

    @Override
    public User getUserFromHttpServletRequest(HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        String email = jwtTokenProvider.getEmail(token);
        return findByEmail(email);
    }

    @Override
    public String createToken(User user) {
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

    @Override
    public UserResponseDto getCurrentUser(HttpServletRequest request) {
        User user = getUserFromHttpServletRequest(request);
        Set<RoleName> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        if (roles.contains(RoleName.ROLE_ADMIN)) {
            return new CustomerResponseDto(user);
        }

        if (roles.contains(RoleName.ROLE_USER) || roles.contains(RoleName.ROLE_BLANK_USER)) {
            return new SupplierResponseDto(user);
        }

        return null;
    }

    @Override
    public CustomerResponseDto updateCustomer(HttpServletRequest request, CustomerUpdateRequestDto dto) {
        User user = getUserFromHttpServletRequest(request);
        // TODO: 03.12.2020 Переделать через MapStruct

        if (dto.getSendEmail() != null) {
            if ("true".equals(dto.getSendEmail())) {
                user.setSendEmail(MailAgreement.YES);
            } else if ("false".equals(dto.getSendEmail())) {
                user.setSendEmail(MailAgreement.NO);
            }
        }

        if (dto.getFio() != null && !dto.getPhone().trim().isEmpty()) {
            user.getCustomerDetails().setFio(dto.getFio());
        }

        log.info(dto.getPhone());
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            user.getCustomerDetails().setPhone(dto.getPhone());
        }

        user.setUpdatedAt(new Timestamp(new Date().getTime()));
        return new CustomerResponseDto(userRepository.save(user));
    }

    @Override
    public SupplierResponseDto updateSupplier(
            HttpServletRequest request,
            SupplierUpdateRequestDto dto
    ) {
        User user = getUserFromHttpServletRequest(request);
        // TODO: 03.12.2020 Переделать через MapStruct
        Set<RoleName> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        if (!roles.contains(RoleName.ROLE_USER)) {
            throw new AccessDeniedException("Доступ запрещён");
        }

        if (dto.getLawStatusId() != null) {
            LawStatus status = lawStatusRepository.findById(UUID.fromString(dto.getLawStatusId()))
                    .orElseThrow(() -> new NotFoundException("Юридический статус с данным ID не найден"));
            user.getSupplierDetails().setLawStatus(status);
        }

        List<Document> userDocs = user.getSupplierDetails().getDocuments();

        if (userDocs != null && dto.getFiles() != null && userDocs.size() + dto.getFiles().length > 10) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "files.errors.amount", null, new Locale("ru", "RU")
                    )
            );
        }

        if (dto.getSendEmail() != null) {
            if ("true".equals(dto.getSendEmail())) {
                user.setSendEmail(MailAgreement.YES);
            } else if ("false".equals(dto.getSendEmail())) {
                user.setSendEmail(MailAgreement.NO);
            }
        }

        if (dto.getDescription() != null) {
            user.getSupplierDetails().setDescription(dto.getDescription());
        }

        if (dto.getCompanyName() != null) {
            user.getSupplierDetails().setCompanyName(dto.getCompanyName());
        }

        if (dto.getInn() != null) {
            user.getSupplierDetails().setInn(dto.getInn());
        }

        if (dto.getFio() != null) {
            user.getSupplierDetails().setContactFio(dto.getFio());
        }

        if (dto.getPhone() != null) {
            user.getSupplierDetails().setPhone(dto.getPhone());
        }

        if (dto.getRegion() != null) {
            user.getSupplierDetails().setRegion(dto.getRegion());
        }

        if (dto.getContacts() != null) {
            user.getSupplierDetails().setContacts(dto.getContacts());
        }

        if (dto.getTags() != null) {
            addTagsToUser(user, dto.getTags());
        }

        MultipartFile[] files = dto.getFiles();
        if (files != null && files.length != 0) {
            addDocumentToUser(user, files);
        }

        user.setUpdatedAt(new Timestamp(new Date().getTime()));
        return new SupplierResponseDto(userRepository.save(user));
    }

    private void addTagsToUser(User user, Set<String> setOfId) {
        setOfId.forEach(id -> {
            Tag tag = tagRepository.findByIdAndStatusForTagNotIn(UUID.fromString(id), Collections.singleton(StateStatus.DELETED))
                    .orElseThrow(() -> new NotFoundException("Тег с данным id не найден"));
            user.getSupplierDetails().getTags().add(tag);
        });
    }

    private void addDocumentToUser(
            User user,
            MultipartFile[] multipartFiles
    ) {
        String pathS3 = "/" + user.getClass().getSimpleName() + "/" + user.getId();
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3);
        if (user.getSupplierDetails().getDocuments() != null) {
            user.getSupplierDetails().getDocuments().addAll(documents);
        } else {
            user.getSupplierDetails().setDocuments(documents);
        }
    }

    @Override
    public SupplierResponseDtoWithNewToken fillSupplier(HttpServletRequest request, SupplierFirstUpdateRequestDto dto) {
        User user = getUserFromHttpServletRequest(request);
        Set<RoleName> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        if (!roles.contains(RoleName.ROLE_BLANK_USER)) {
            throw new AccessDeniedException("Доступ запрещён");
        }
        // TODO: 03.12.2020 Переделать через MapStruct
        LawStatus status = lawStatusRepository.findById(
                UUID.fromString(dto.getLawStatusId())
        ).orElseThrow(() -> new NotFoundException("Юридический статус с данным ID не найден"));

        List<Document> userDocs = user.getSupplierDetails().getDocuments();

        if (userDocs != null && dto.getFiles() != null && userDocs.size() + dto.getFiles().length > 10) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "files.errors.amount", null, new Locale("ru", "RU")
                    )
            );
        }
        if (dto.getCompanyName() != null) {
            user.getSupplierDetails().setCompanyName(dto.getCompanyName());
        }

        if (dto.getSendEmail() != null) {
            if ("true".equals(dto.getSendEmail())) {
                user.setSendEmail(MailAgreement.YES);
            } else if ("false".equals(dto.getSendEmail())) {
                user.setSendEmail(MailAgreement.NO);
            }
        }

        user.getSupplierDetails().setDescription(dto.getDescription());
        user.getSupplierDetails().setInn(dto.getInn());
        user.getSupplierDetails().setContactFio(dto.getFio());
        user.getSupplierDetails().setPhone(dto.getPhone());
        user.getSupplierDetails().setContacts(dto.getContacts());
        user.getSupplierDetails().setRegion(dto.getRegion());
        user.getSupplierDetails().setLawStatus(status);

        if (dto.getTags() != null) {
            addTagsToUser(user, dto.getTags());
        }

        MultipartFile[] files = dto.getFiles();
        if (files != null && files.length != 0) {
            addDocumentToUser(user, files);
        }

        Role roleUser = getRoleFromRepository(RoleName.ROLE_USER);
        Role blankUser = getRoleFromRepository(RoleName.ROLE_BLANK_USER);

        user.getRoles().remove(blankUser);
        user.getRoles().add(roleUser);
        user.setUpdatedAt(new Timestamp(new Date().getTime()));

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        return new SupplierResponseDtoWithNewToken(userRepository.save(user), token);
    }

    @Override
    public boolean verifyJwt(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.validateToken(token);
    }

    @Async
    public void sendConfirmationEmail(User user, VerificationToken token){
        Map<String, Object> data = new HashMap<>();
        data.put("url", frontendUrl + "/verify");
        data.put("token", token.getId().toString());
        mailService.sendMailAnyway("verification", "Подтверждение регистрации", data, user);
    }
}
