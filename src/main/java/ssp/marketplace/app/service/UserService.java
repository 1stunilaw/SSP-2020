package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.registration.*;
import ssp.marketplace.app.dto.user.*;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.request.*;
import ssp.marketplace.app.dto.user.supplier.response.*;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.validation.unique.FieldValueExists;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public interface UserService extends FieldValueExists {

    ResponseUserDto register(RequestRegisterUserDto registerDto);

    User findByEmail(String email);

    User findById(UUID id);

    void deleteUser(UUID id);

    void confirmRegister(String token);

    User getUserFromHttpServletRequest(HttpServletRequest req);

    String createToken(User user);

    ResponseUserDto getCurrentUser(HttpServletRequest request);

    ResponseCustomerDto updateCustomer(HttpServletRequest request, RequestCustomerUpdateDto dto);

    ResponseSupplierDto updateSupplier(HttpServletRequest request, RequestSupplierUpdateDto dto);

    ResponseSupplierDtoWithNewToken fillSupplier(HttpServletRequest request, RequestSupplierFirstUpdateDto dto);

    boolean verifyJwt(HttpServletRequest request);
}
