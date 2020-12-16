package ssp.marketplace.app.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ssp.marketplace.app.entity.user.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public abstract class UserResponseDto implements Serializable {
    private UUID id;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Timestamp updatedAt;

    public UserResponseDto(User user) {
        id = user.getId();
        email = user.getEmail();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
    }
}
