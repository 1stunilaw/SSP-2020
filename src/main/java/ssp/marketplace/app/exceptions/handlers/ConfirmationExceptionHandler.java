package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.ConfirmationException;
import ssp.marketplace.app.exceptions.response.ValidationErrorResponse;

import java.util.*;

@ControllerAdvice
public class ConfirmationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConfirmationException.class)
    public ValidationErrorResponse confirmationExceptionHandler(ConfirmationException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации"
        );
        error.addFieldError(ex.getField(), ex.getMessage());
        return error;
    }
}
