package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.suppliers.*;
import ssp.marketplace.app.dto.suppliers.edit.EditSupplierDataRequestDto;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.SupplierService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public SupplierServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public SupplierResponseDto getSupplier(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> user = userRepository.findById(uuid);

            if (user.isEmpty() || user.get().getSupplierDetails() == null){
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
}
