package ssp.marketplace.app.api;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.suppliers.*;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private SupplierService supplierService;


    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping()
    public Page<SupplierPageResponseDto> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public SupplierResponseDto getSupplier(@PathVariable("id") String id){
        return supplierService.getSupplier(id);
    }

    @GetMapping(value = "/{supplierId}/{filename}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getSupplierDocument(
            @PathVariable String filename,
            @PathVariable UUID supplierId,
            HttpServletRequest request
    ){
        return supplierService.getSupplierDocument(filename, supplierId,request);
    }

    @GetMapping("/principal")
    public String home(Authentication authentication){
        return authentication.getName();
    }

}
