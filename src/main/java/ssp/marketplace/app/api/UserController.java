package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.*;
import ssp.marketplace.app.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public UserResponseDto getCurrentUser(HttpServletRequest request){
        return userService.getCurrentUser(request);
    }

    @PatchMapping(value = "/customer/update")
    public CustomerResponseDto updateCustomer(HttpServletRequest request, @RequestBody @Valid @NotNull CustomerUpdateRequestDto dto){
        return userService.updateCustomer(request, dto);
    }

    @PatchMapping(value = "/supplier/update")
    public SupplierResponseDto updateSupplier(
            HttpServletRequest request,
            @RequestPart("user") SupplierUpdateRequestDto dto,
            @RequestParam("files") MultipartFile[] files)
    {
        return userService.updateSupplier(request, dto, files);
    }

    @PatchMapping(value = "/supplier/fill")
    public SupplierResponseDto fillSupplier(
            HttpServletRequest request,
            @RequestPart("user") @Valid @NotNull SupplierFirstUpdateRequestDto dto,
            @RequestParam("files") MultipartFile[] files)
    {
        return userService.fillSupplier(request, dto, files);
    }
}
