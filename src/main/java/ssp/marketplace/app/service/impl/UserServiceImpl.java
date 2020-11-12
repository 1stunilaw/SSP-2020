package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ssp.marketplace.app.dto.*;
import ssp.marketplace.app.dto.mappers.RegisterDtoMapper;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.NotFoundException;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
