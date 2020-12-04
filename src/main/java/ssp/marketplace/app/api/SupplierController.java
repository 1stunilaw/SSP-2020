package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.suppliers.*;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.service.SupplierService;

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

    @GetMapping("/principal")
    public String home(Authentication authentication){
        return authentication.getName();
    }

}
