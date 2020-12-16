package ssp.marketplace.app.dto.user.customer;

import lombok.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
public class CustomerResponseDto extends UserResponseDto {
    private String fio;
    private String phone;

    public CustomerResponseDto(User user) {
        super(user);
        fio = user.getCustomerDetails().getFio();
        phone = user.getCustomerDetails().getPhone();
    }
}
