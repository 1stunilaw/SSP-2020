package ssp.marketplace.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String name, Object key) {
        super(String.format("Сущность %s с ключом %s не найдена.", name, key.toString()));
    }
}