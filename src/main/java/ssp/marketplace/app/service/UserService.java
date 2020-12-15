package ssp.marketplace.app.service;

import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.registration.*;
import ssp.marketplace.app.dto.user.*;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.validation.FieldValueExists;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public interface UserService extends FieldValueExists {

    UserResponseDto register(RegisterRequestUserDto registerDto);

    User findByEmail(String email);

    User findById(UUID id);

    void deleteUser(UUID id);

    VerificationToken createVerificationToken(User user);

    void confirmRegister(String token);

    User getUserFromHttpServletRequest(HttpServletRequest req);

    String createToken(User user);

    UserResponseDto getCurrentUser(HttpServletRequest request);

    CustomerResponseDto updateCustomer(HttpServletRequest request, CustomerUpdateRequestDto dto);

    SupplierResponseDto updateSupplier(HttpServletRequest request, SupplierUpdateRequestDto dto);

    SupplierResponseDtoWithNewToken fillSupplier(HttpServletRequest request, SupplierFirstUpdateRequestDto dto);

    boolean verifyJwt(HttpServletRequest request);
}
