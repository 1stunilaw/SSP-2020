package ssp.marketplace.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.*;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final SupplierService supplierService;

    @Autowired
    public UserController(UserService userService, SupplierService supplierService) {
        this.userService = userService;
        this.supplierService = supplierService;
    }

    @GetMapping("/user")
    public UserResponseDto getCurrentUser(HttpServletRequest request){
        return userService.getCurrentUser(request);
    }

    @PatchMapping(value = "/customer/update")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponseDto updateCustomer(HttpServletRequest request, @RequestBody @Valid @NotNull CustomerUpdateRequestDto dto){
        return userService.updateCustomer(request, dto);
    }

    @PatchMapping(value = "/supplier/update", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponseDto updateSupplier(
            HttpServletRequest request,
            @ModelAttribute @Valid @NotNull SupplierUpdateRequestDto dto
    )
    {
        return userService.updateSupplier(request, dto);
    }

    @PatchMapping(value = "/supplier/fill", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponseDtoWithNewToken fillSupplier(
            HttpServletRequest request,
            @ModelAttribute @Valid @NotNull SupplierFirstUpdateRequestDto dto
    )
    {
        return userService.fillSupplier(request, dto);
    }

    @DeleteMapping(value = "/supplier/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteTagFromSupplier(
            HttpServletRequest request,
            @PathVariable String tagId
    )
    {
        try {
            UUID id = UUID.fromString(tagId);
            supplierService.deleteTagFromSupplier(request, id);
            HashMap response = new HashMap();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Тег успешно удалён");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный ID тега");
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
