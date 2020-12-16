package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.lawStatus.LawStatusCreateRequestDto;
import ssp.marketplace.app.dto.user.supplier.LawStatusResponseDto;
import ssp.marketplace.app.validation.unique.FieldValueExists;

import java.util.*;

public interface LawStatusService extends FieldValueExists {
    List<LawStatusResponseDto> getAllStatuses();

    LawStatusResponseDto createLawStatus(LawStatusCreateRequestDto dto);

    void deleteLawStatus(UUID id);
}
