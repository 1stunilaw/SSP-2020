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
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DocumentService documentService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public SupplierServiceImpl(
            UserRepository userRepository, RoleRepository roleRepository, DocumentService documentService, UserService userService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.documentService = documentService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public SupplierResponseDto getSupplier(String id, HttpServletRequest req) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> user = userRepository.findById(uuid);

            if (!user.isPresent() || user.get().getSupplierDetails() == null){
                throw new NotFoundException("Поставщик с данным идентификатором не был найден");
            }
            return new SupplierForAdminResponseDto(user.get());
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный идентификатор");
        }
    }

    @Override
    public SupplierForAdminResponseDto addAccreditationStatus(String id, SupplierAddAccreditationRequestDto accreditationStatus) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> user = userRepository.findById(uuid);
            if (!user.isPresent() || user.get().getSupplierDetails() == null){
                throw new NotFoundException("Поставщик с данным идентификатором не был найден");
            }
            User u = user.get();
            u.getSupplierDetails().setAccreditationStatus(accreditationStatus.getAccreditationStatus());
            userRepository.save(u);
            return new SupplierForAdminResponseDto(u);
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный идентификатор");
        }
    }

    @Override
    public Page<SupplierPageResponseDto> getAllSuppliers(Pageable pageable) {
        List<Role> roles = roleRepository.findByNameIsIn(Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_BLANK_USER));
        Page<User> userPage = userRepository.findByRolesInAndStatus(pageable, roles, UserStatus.ACTIVE);

        return new PageImpl<>(userPage.stream().map(SupplierPageResponseDto::new).collect(Collectors.toList()), pageable, userPage.getTotalElements());
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

    @Override
    public void deleteDocument(UUID supplierId, String filename, HttpServletRequest request) {
        User user = userService.getUserFromHttpServletRequest(request);
        if (!user.getId().equals(supplierId)) {
            throw new AccessDeniedException("Доступ запрещён");
        }
        documentService.deleteDocument(filename);
    }

    @Override
    public void deleteTagFromSupplier(HttpServletRequest request, UUID tagId) {
        User user = userService.getUserFromHttpServletRequest(request);
        Set<RoleName> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        if (!roles.contains(RoleName.ROLE_USER)) {
            throw new AccessDeniedException("Доступ запрещён");
        }
        Optional<Tag> tagToDelete = user.getSupplierDetails().getTags().stream().filter(x -> x.getId().equals(tagId)).findFirst();
        if (tagToDelete.isPresent()){
            user.getSupplierDetails().getTags().remove(tagToDelete.get());
            userRepository.save(user);
        } else {
            throw new NotFoundException("Тега с данным ID нет в списке ваших тегов");
        }
    }
}
