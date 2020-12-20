package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;

@ControllerAdvice
public class AccessDeniedExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public SimpleResponse AccessDeniedExceptionHandler(AccessDeniedException ex){
        return new SimpleResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
