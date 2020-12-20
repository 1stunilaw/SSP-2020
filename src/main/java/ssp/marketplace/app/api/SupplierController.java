package ssp.marketplace.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.dto.user.supplier.request.RequestSupplierAddAccreditationDto;
import ssp.marketplace.app.dto.user.supplier.response.*;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.service.SupplierService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/suppliers")
@Slf4j
public class SupplierController {

    private SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<ResponseSupplierPageDto> getAllSuppliers(
            @PageableDefault(sort = {"supplierDetails.companyName"}, size = 30, value = 30, direction = Sort.Direction.ASC, page = 0) Pageable pageable
    ) {
        return supplierService.getAllSuppliers(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseSupplierDto getSupplier(
            @PathVariable("id") String id,
            HttpServletRequest req
    ) {
        return supplierService.getSupplier(id, req);
    }

    @PostMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseSupplierDto addAccreditationStatus(
            @PathVariable("id") String id,
            @RequestBody @Valid RequestSupplierAddAccreditationDto accreditationStatus
    ) {
        return supplierService.addAccreditationStatus(id, accreditationStatus);
    }

    @GetMapping(value = "/{supplierId}/{filename}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InputStreamResource> getSupplierDocument(
            @PathVariable String filename,
            @PathVariable String supplierId,
            HttpServletRequest request
    ) {
        try {
            UUID userId = UUID.fromString(supplierId);
            return supplierService.getSupplierDocument(filename, userId, request);
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный ID пользователя");
        }
    }

    @DeleteMapping("/{supplierId}/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteDocument(
            @PathVariable String supplierId,
            @PathVariable String filename,
            HttpServletRequest request
    ) {
        try {
            UUID userId = UUID.fromString(supplierId);
            supplierService.deleteDocument(userId, filename, request);
            return new SimpleResponse(HttpStatus.OK.value(), "Документ успешно удалён");
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный ID пользователя");
        }
    }
}
