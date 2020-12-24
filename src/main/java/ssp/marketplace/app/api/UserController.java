package ssp.marketplace.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.dto.user.*;
import ssp.marketplace.app.dto.user.customer.*;
import ssp.marketplace.app.dto.user.supplier.request.*;
import ssp.marketplace.app.dto.user.supplier.response.*;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Контроллер действий пользователя
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final SupplierService supplierService;

    @Autowired
    public UserController(UserService userService, SupplierService supplierService) {
        this.userService = userService;
        this.supplierService = supplierService;
    }

    /**
     * Запрашивает у сервиса информацию об авторизованном пользователе
     * @param request Информация о запросе
     * @return Информацию об авторизованном пользователе
     */
    @GetMapping("/user")
    public ResponseUserDto getCurrentUser(HttpServletRequest request){
        return userService.getCurrentUser(request);
    }

    /**
     * Передаёт сервису запрос на изменение данных авторизованного заказчика
     * @param request Информация о запросе
     * @param dto Новые данные заказчика
     * @return Изменённые данные заказчика
     */
    @PatchMapping(value = "/customer/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCustomerDto updateCustomer(
            HttpServletRequest request,
            @RequestBody @Valid @NotNull RequestCustomerUpdateDto dto
    )
    {
        return userService.updateCustomer(request, dto);
    }


    @PatchMapping(value = "/user/password")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse updatePassword(
            HttpServletRequest request,
            @RequestBody @Valid @NotNull RequestPasswordUpdateDto passwordUpdateDto
    )
    {
        userService.updatePassword(request, passwordUpdateDto);
        return new SimpleResponse(HttpStatus.OK.value(), "Пароль успешно изменён");
    }



    /**
     * Передаёт сервису запрос на изменение данных авторизованного поставщика
     * @param request Информация о запросе
     * @param dto Новые данные поставщика
     * @return Изменённые данные поставщика
     */
    @PatchMapping(value = "/supplier/update", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ResponseSupplierDto updateSupplier(
            HttpServletRequest request,
            @ModelAttribute @Valid @NotNull RequestSupplierUpdateDto dto
    )
    {
        return userService.updateSupplier(request, dto);
    }

    /**
     * Передаёт сервису запрос на первичное заполнение поставщика
     * @param request Информация о запросе
     * @param dto Данные поставщика для заполнения
     * @return Обновлённая информация о поставщике
     */
    @PatchMapping(value = "/supplier/fill", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ResponseSupplierDtoWithNewToken fillSupplier(
            HttpServletRequest request,
            @ModelAttribute @Valid @NotNull RequestSupplierFirstUpdateDto dto
    )
    {
        return userService.fillSupplier(request, dto);
    }

    @DeleteMapping(value = "/supplier/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteTagFromSupplier(
            HttpServletRequest request,
            @PathVariable String tagId
    )
    {
        try {
            UUID id = UUID.fromString(tagId);
            supplierService.deleteTagFromSupplier(request, id);
            return new SimpleResponse(HttpStatus.OK.value(), "Тег успешно откреплён");
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный ID тега");
        }
    }

    @GetMapping("/user/token")
    @ResponseStatus(HttpStatus.OK)
    public boolean verifyJwt(HttpServletRequest request){
        return userService.verifyJwt(request);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @InitBinder("customerUpdateRequestDto")
    public void initBinderForCustomerUpdate(WebDataBinder binder){
        // TODO: 20.12.2020 Попробовать убрать метод
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
