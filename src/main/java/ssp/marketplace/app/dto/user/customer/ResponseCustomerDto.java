package ssp.marketplace.app.dto.user.customer;

import lombok.*;
import ssp.marketplace.app.dto.user.ResponseUserDto;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
public class ResponseCustomerDto extends ResponseUserDto {
    private String fio;
    private String phone;

    public ResponseCustomerDto(User user) {
        super(user);
        fio = user.getCustomerDetails().getFio();
        phone = user.getCustomerDetails().getPhone();
    }
}
