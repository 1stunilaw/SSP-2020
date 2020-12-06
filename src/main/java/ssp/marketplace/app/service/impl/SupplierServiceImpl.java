package ssp.marketplace.app.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.user.supplier.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DocumentService documentService;
    private final UserService userService;

    @Autowired
    public SupplierServiceImpl(UserRepository userRepository, RoleRepository roleRepository, DocumentService documentService,UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.documentService = documentService;
        this.userService = userService;
    }

    @Override
    public SupplierResponseDto getSupplier(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> user = userRepository.findById(uuid);

            if (!user.isPresent() || user.get().getSupplierDetails() == null){
                throw new NotFoundException("Поставщик с данным идентификатором не был найден");
            }

            return new SupplierResponseDto(user.get());
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный идентификатор");
        }
    }

    @Override
    public Page<SupplierPageResponseDto> getAllSuppliers() {
        Pageable pageable = PageRequest.of(0,10, Sort.Direction.ASC, "name");
        List<RoleName> suppliers = new ArrayList<>();
        suppliers.add(RoleName.ROLE_USER);
        //suppliers.add(RoleName.ROLE_BLANK_USER);
        Page<Role> page = roleRepository.findByNameIsIn(suppliers, pageable);
        Set<User> users = page.getContent().stream().map(Role::getUsers).findFirst().get();
        users.forEach(x -> log.info(x.getEmail()));
        return new PageImpl<>(page.stream().map(Role::getUsers).map(SupplierPageResponseDto::listOfDto).findFirst().get());
    }

    @Override
    public ResponseEntity<InputStreamResource> getSupplierDocument(
            String filename, UUID supplierId, HttpServletRequest request
    ) {
        User user = userService.getUserFromHttpServletRequest(request);
        Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN).get();

        if (!user.getRoles().contains(roleAdmin) && !user.getId().equals(supplierId)) {
            throw new AccessDeniedException("Доступ запрещён");
        }

        S3ObjectInputStream s3is = documentService.downloadSupplierFile(filename, supplierId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE)).cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(new InputStreamResource(s3is));
    }
}
