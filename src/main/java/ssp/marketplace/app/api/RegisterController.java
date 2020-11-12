package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.*;
import ssp.marketplace.app.entity.RoleName;
import ssp.marketplace.app.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

    private UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public UserResponseDto registerUser(@RequestBody @Valid @NotNull RegisterUserDto registerUserDto){
        return userService.register(registerUserDto, RoleName.ROLE_USER);
    }

    @PostMapping("/admin")
    public UserResponseDto registerAdmin(@RequestBody @Valid @NotNull RegisterUserDto registerUserDto){
        return userService.register(registerUserDto, RoleName.ROLE_ADMIN);
    }

    @PostMapping("/lawyer")
    public UserResponseDto registerLawyer(@RequestBody @Valid @NotNull RegisterUserDto registerUserDto){
        return userService.register(registerUserDto, RoleName.ROLE_LAWYER);
    }
}
