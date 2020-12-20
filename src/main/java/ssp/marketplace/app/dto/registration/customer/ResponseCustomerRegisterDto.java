package ssp.marketplace.app.dto.registration.customer;

import lombok.*;
import ssp.marketplace.app.dto.user.ResponseUserDto;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseCustomerRegisterDto extends ResponseUserDto {
    private String fio;
    private String phone;

    public ResponseCustomerRegisterDto(User user) {
        super.setId(user.getId());
        super.setEmail(user.getEmail());
        super.setCreatedAt(user.getCreatedAt());
        super.setUpdatedAt(user.getUpdatedAt());

        fio = user.getCustomerDetails().getFio();
        phone = user.getCustomerDetails().getPhone();
    }
}
