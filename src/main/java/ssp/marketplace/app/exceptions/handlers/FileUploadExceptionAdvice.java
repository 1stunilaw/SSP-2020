package ssp.marketplace.app.exceptions.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ssp.marketplace.app.dto.SimpleResponse;
// TODO: 20.12.2020 Переименовать класс
@ControllerAdvice
public class FileUploadExceptionAdvice {


    @Value("${spring.servlet.multipart.max-file-size}")
    private String size;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public SimpleResponse FileUploadExceptionAdvice(
            MaxUploadSizeExceededException ex
    ) {
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), "Суммарный размер файлов не должен превышать "+size);
    }
}