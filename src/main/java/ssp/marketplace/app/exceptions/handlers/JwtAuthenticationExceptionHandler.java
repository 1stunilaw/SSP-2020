package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.security.jwt.JwtAuthenticationException;

public class JwtAuthenticationExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(JwtAuthenticationException.class)
    public SimpleResponse JwtAuthenticationException(JwtAuthenticationException ex){
        return new SimpleResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }
}
