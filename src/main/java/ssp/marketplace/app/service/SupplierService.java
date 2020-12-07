package ssp.marketplace.app.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ssp.marketplace.app.dto.user.supplier.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface SupplierService {

    SupplierResponseDto getSupplier(String id);

    Page<SupplierPageResponseDto> getAllSuppliers();

    ResponseEntity<InputStreamResource> getSupplierDocument(String filename, UUID supplierId, HttpServletRequest request);

    void deleteDocument(UUID supplierId, String filename, HttpServletRequest request);

    void deleteTagFromSupplier(HttpServletRequest request, UUID tagId);
}
