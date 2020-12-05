package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class MethodArgumentTypeMismatchExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse BadRequestHandler(){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Неправильный путь");
    }
}
