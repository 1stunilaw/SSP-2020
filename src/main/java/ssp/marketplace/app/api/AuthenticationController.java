package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.*;
import ssp.marketplace.app.entity.User;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.UserService;

import java.util.*;

@RestController
@RequestMapping(value = "api/")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("login")
    public AuthenticationResponseDto login(@RequestBody AuthenticationRequestDto requestDto){
        try {
            String email = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
            User user = userService.findByEmail(email);

            String token = userService.createToken(user);

            return new AuthenticationResponseDto(email, token);
        } catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
