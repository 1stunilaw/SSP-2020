package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssp.marketplace.app.dto.*;
import ssp.marketplace.app.dto.mappers.RegisterDtoMapper;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.custom.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.service.impl.events.OnRegistrationCompleteEvent;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(
            ApplicationEventPublisher eventPublisher,
            UserRepository userRepository,
            RoleRepository roleRepository,
            TokenRepository tokenRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserResponseDto register(RegisterUserDto registerDto, RoleName role) {
        RoleName roleName = role;
        Role roleUser = roleRepository.findByName(roleName)
                .orElseThrow(()-> new NotFoundException("Role was not found: " + roleName));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);

        RegisterDtoMapper mapper = Mappers.getMapper(RegisterDtoMapper.class);
        User user = new User();

        mapper.createUserFromDto(registerDto, user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(userRoles);

        User registeredUser = userRepository.save(user);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        log.info("Starting event");
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, baseUrl));
        log.info("IN register - user: {} successfully registered", registeredUser);

        return new UserResponseDto(registeredUser);
    }

    @Override
    public Set<UserResponseDto> getAllUsers() {
        Set<User> users = new HashSet<>(userRepository.findAll());
        Set<UserResponseDto> result = users.stream().map(UserResponseDto::new).collect(Collectors.toSet());
        log.info("IN getAllUsers - {} users found", result.size());
        return result;
    }

    @Override
    public User findByEmail(String email) {
        User result = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Пользователь с данным email не был найден: " + email));
        log.info("IN findByEmail - user: {} found by email {}", result, email);
        return result;
    }

    @Override
    public User findById(UUID id) {
        User result = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Пользователь с данным id не был найден: " + id));
        log.info("IN findByEmail - user: {} found by id {}", result, id);
        return result;
    }

    @Override
    public void deleteUser(UUID id) {
        User toDelete = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Пользователь с данным id не был найден: " + id));
        toDelete.setStatus(Status.DELETED);
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

            if(verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTimeInMillis() <= 0L){
                tokenRepository.delete(verificationToken);
                throw new ConfirmationTokenInvalidException("Срок действия токена истёк");
            }

            User user = verificationToken.getUser();
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
            tokenRepository.delete(verificationToken);
        } catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Невалидный токен");
        }


    }

    private VerificationToken getVerificationToken(UUID token){
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
}
