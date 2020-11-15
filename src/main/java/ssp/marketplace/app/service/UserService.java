package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.validation.FieldValueExists;

import java.util.*;

public interface UserService extends FieldValueExists {

    UserResponseDto register(RegisterUserDto registerDto, RoleName role);

    Set<UserResponseDto> getAllUsers();

    User findByEmail(String email);

    User findById(UUID id);

    void deleteUser(UUID id);

    VerificationToken createVerificationToken(User user);

    void confirmRegister(String token);
}
