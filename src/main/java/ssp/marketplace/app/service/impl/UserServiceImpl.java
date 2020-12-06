package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssp.marketplace.app.dto.registration.*;
import ssp.marketplace.app.dto.registration.customer.*;
import ssp.marketplace.app.dto.registration.supplier.*;
import ssp.marketplace.app.dto.user.*;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.customer.CustomerDetails;
import ssp.marketplace.app.entity.supplier.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;
import ssp.marketplace.app.service.impl.events.OnRegistrationCompleteEvent;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
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

    private final LawStatusRepository lawStatusRepository;

    private final DocumentService documentService;

    @Autowired
    public UserServiceImpl(
            ApplicationEventPublisher eventPublisher,
            UserRepository userRepository,
            RoleRepository roleRepository,
            TokenRepository tokenRepository,
            LawStatusRepository lawStatusRepository,
            DocumentService documentService,
            MessageSource messageSource,
            @Lazy JwtTokenProvider jwtTokenProvider
    ) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.messageSource = messageSource;
        this.jwtTokenProvider = jwtTokenProvider;
        this.lawStatusRepository = lawStatusRepository;
        this.documentService = documentService;
    }

    @Override
    public UserResponseDto register(RegisterRequestUserDto registerDto) {

        if (registerDto instanceof CustomerRegisterRequestDto) {
            User user = registerCustomer((CustomerRegisterRequestDto)registerDto);
            log.info("IN register - user: {} successfully registered", user);
            return new CustomerRegisterResponseDto(user);
        } else if (registerDto instanceof SupplierRegisterRequestDto) {
            User user = registerSupplier((SupplierRegisterRequestDto)registerDto);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, baseUrl));
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

        user.setSupplierDetails(details);
        details.setUser(user);

        return userRepository.save(user);
    }

    private Role getRoleFromRepository(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role was not found: " + roleName));
    }

    @Override
    public User findByEmail(String email) {
        User result = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с данным email не был найден: " + email));
        log.info("IN findByEmail - user: {} found by email {}", result, email);
        return result;
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

    @Override
    public VerificationToken createVerificationToken(User user) {
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
                throw new ConfirmationTokenInvalidException("Срок действия токена истёк");
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
        if (!"email".equals(fieldName)) {
            throw new UnsupportedOperationException("Данное поле не поддерживается");
        }

        if (value == null) {
            return false;
        }

        return userRepository.existsByEmail(value.toString());
    }

    @Override
    public User getUserFromHttpServletRequest(HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        String email = jwtTokenProvider.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return user;
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
        } else if (roles.contains(RoleName.ROLE_USER) || roles.contains(RoleName.ROLE_BLANK_USER)) {
            return new SupplierResponseDto(user);
        }

        return null;
    }

    @Override
    public CustomerResponseDto updateCustomer(HttpServletRequest request, CustomerUpdateRequestDto dto) {
        User user = getUserFromHttpServletRequest(request);
        // TODO: 03.12.2020 Переделать через MapStruct
        
        if (dto.getFio() != null) {
            user.getCustomerDetails().setFio(dto.getFio());
        }

        if (dto.getPhone() != null) {
            user.getCustomerDetails().setPhone(dto.getPhone());
        }

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
        if (!roles.contains(RoleName.ROLE_USER)){
            throw new AccessDeniedException("Доступ запрещён");
        }

        if (dto.getLawStatusId() != null) {
            LawStatus status = lawStatusRepository.findById(UUID.fromString(dto.getLawStatusId()))
                    .orElseThrow(() -> new NotFoundException("Юридический статус с данным ID не найден"));
            user.getSupplierDetails().setLawStatus(status);
        }

        if (user.getSupplierDetails().getDocuments().size() + dto.getFiles().length > 10) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "files.errors.amount", null, new Locale("ru", "RU")
                    )
            );
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

        if (dto.getContactFio() != null) {
            user.getSupplierDetails().setContactFio(dto.getContactFio());
        }

        if (dto.getPhone() != null) {
            user.getSupplierDetails().setPhone(dto.getPhone());
        }

        if (dto.getRegion() != null) {
            user.getSupplierDetails().setRegion(dto.getRegion());
        }

        if (dto.getContacts() != null){
            user.getSupplierDetails().setContacts(dto.getContacts());
        }

        MultipartFile[] files = dto.getFiles();
        if (files != null && files.length != 0) {
            addDocumentToUser(user, files);
        }

        user.setUpdatedAt(new Timestamp(new Date().getTime()));
        return new SupplierResponseDto(userRepository.save(user));
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
        if (!roles.contains(RoleName.ROLE_BLANK_USER)){
            throw new AccessDeniedException("Доступ запрещён");
        }
        // TODO: 03.12.2020 Переделать через MapStruct
        LawStatus status = lawStatusRepository.findById(UUID.fromString(dto.getLawStatusId()))
                .orElseThrow(() -> new NotFoundException("Юридический статус с данным ID не найден"));
        if (user.getSupplierDetails().getDocuments().size() + dto.getFiles().length > 10) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "files.errors.amount", null, new Locale("ru", "RU")
                    )
            );
        }
        user.getSupplierDetails().setCompanyName(dto.getCompanyName());
        user.getSupplierDetails().setDescription(dto.getDescription());
        user.getSupplierDetails().setInn(dto.getInn());
        user.getSupplierDetails().setContactFio(dto.getContactFio());
        user.getSupplierDetails().setPhone(dto.getPhone());
        user.getSupplierDetails().setContacts(dto.getContacts());
        user.getSupplierDetails().setRegion(dto.getRegion());
        user.getSupplierDetails().setLawStatus(status);

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

}
