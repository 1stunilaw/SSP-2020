package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.dto.registration.customer.RequestCustomerRegisterDto;
import ssp.marketplace.app.dto.registration.supplier.RequestSupplierRegisterDto;
import ssp.marketplace.app.dto.user.ResponseUserDto;
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
    public ResponseUserDto registerSupplier(@RequestBody @Valid @NotNull RequestSupplierRegisterDto registerUserDto){
        return userService.register(registerUserDto);
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto registerCustomer(@RequestBody @Valid @NotNull RequestCustomerRegisterDto registerCustomerDto){
        return userService.register(registerCustomerDto);
    }

//    @PostMapping("/lawyer")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponseDto registerLawyer(@RequestBody @Valid @NotNull RegisterRequestUserDto registerUserDto){
//        return userService.register(registerUserDto);
//    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse confirmRegister(@RequestParam("token") String token){
        userService.confirmRegister(token);
        return new SimpleResponse(HttpStatus.OK.value(), "Регистрация успешно подтверждена");
    }
}
