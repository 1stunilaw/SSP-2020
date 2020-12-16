package ssp.marketplace.app.exceptions.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String size;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorResponse FileUploadExceptionAdvice(
            MaxUploadSizeExceededException ex
    ) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Суммарный размер файлов не должен превышать "+size);
    }
}