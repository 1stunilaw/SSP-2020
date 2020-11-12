package ssp.marketplace.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.response.ValidationErrorResponse;

import java.util.*;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ValidationErrorResponse processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        ValidationErrorResponse error = new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), "Ошибка валидации");
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

}
