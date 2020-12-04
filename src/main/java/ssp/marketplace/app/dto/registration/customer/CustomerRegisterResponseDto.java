package ssp.marketplace.app.dto.registration.customer;

import lombok.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.User;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomerRegisterResponseDto extends UserResponseDto {
    private String fio;
    private String phone;

    public CustomerRegisterResponseDto(User user) {
        super.setId(user.getId());
        super.setEmail(user.getEmail());
        super.setCreatedAt(user.getCreatedAt());
        super.setUpdatedAt(user.getUpdatedAt());

        fio = user.getCustomerDetails().getFio();
        phone = user.getCustomerDetails().getPhone();
    }
}
