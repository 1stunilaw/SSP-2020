package ssp.marketplace.app.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class SimpleResponse implements Serializable {
    private final int status;
    private final String message;

    public SimpleResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
