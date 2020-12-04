package ssp.marketplace.app.service;

import org.springframework.data.domain.Page;
import ssp.marketplace.app.dto.suppliers.*;
import ssp.marketplace.app.dto.suppliers.edit.EditSupplierDataRequestDto;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface SupplierService {

    SupplierResponseDto getSupplier(String id);

    Page<SupplierPageResponseDto> getAllSuppliers();
}
