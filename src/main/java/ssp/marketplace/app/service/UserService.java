package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.registration.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.validation.FieldValueExists;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public interface UserService extends FieldValueExists {

    UserResponseDto register(RegisterRequestUserDto registerDto);

//    Set<UserResponseDto> getAllUsers();

    User findByEmail(String email);

    User findById(UUID id);

    void deleteUser(UUID id);

    VerificationToken createVerificationToken(User user);

    void confirmRegister(String token);

    User getUserFromHttpServletRequest(HttpServletRequest req);
}
