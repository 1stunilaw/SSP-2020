package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.response.ErrorResponse;
import ssp.marketplace.app.security.jwt.JwtAuthenticationException;

public class JwtAuthenticationExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(JwtAuthenticationException.class)
    public ErrorResponse JwtAuthenticationException(JwtAuthenticationException ex){
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }
}
