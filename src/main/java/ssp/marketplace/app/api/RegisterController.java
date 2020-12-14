package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.registration.customer.CustomerRegisterRequestDto;
import ssp.marketplace.app.dto.registration.supplier.SupplierRegisterRequestDto;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/supplier")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerSupplier(@RequestBody @Valid @NotNull SupplierRegisterRequestDto registerUserDto){
        return userService.register(registerUserDto);
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerCustomer(@RequestBody @Valid @NotNull CustomerRegisterRequestDto registerCustomerDto){
        return userService.register(registerCustomerDto);
    }

//    @PostMapping("/lawyer")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponseDto registerLawyer(@RequestBody @Valid @NotNull RegisterRequestUserDto registerUserDto){
//        return userService.register(registerUserDto);
//    }

    @GetMapping("/verify")
    public void confirmRegister(@RequestParam("token") String token){
        userService.confirmRegister(token);
    }
}
