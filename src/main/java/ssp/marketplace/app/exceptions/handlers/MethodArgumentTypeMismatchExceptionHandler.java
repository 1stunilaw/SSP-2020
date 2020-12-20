package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ssp.marketplace.app.dto.SimpleResponse;

@ControllerAdvice
public class MethodArgumentTypeMismatchExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public SimpleResponse BadRequestHandler(){
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), "Ошибка запроса. Проверьте метод, адрес и тело запроса");
    }
}
