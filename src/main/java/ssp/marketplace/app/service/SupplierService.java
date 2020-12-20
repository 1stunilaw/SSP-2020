package ssp.marketplace.app.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import ssp.marketplace.app.dto.user.supplier.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface SupplierService {

    SupplierResponseDto getSupplier(String id, HttpServletRequest req);

    SupplierResponseDto addAccreditationStatus(String id, SupplierAddAccreditationRequestDto accreditationStatus);

    Page<SupplierPageResponseDto> getAllSuppliers(Pageable pageable);

    ResponseEntity<InputStreamResource> getSupplierDocument(String filename, UUID supplierId, HttpServletRequest request);

    // TODO: 20.12.2020 Переделать название метода
    void deleteDocument(UUID supplierId, String filename, HttpServletRequest request);

    void deleteTagFromSupplier(HttpServletRequest request, UUID tagId);
}
