package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class AccessDeniedExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse AccessDeniedExceptionHandler(AccessDeniedException ex){
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
