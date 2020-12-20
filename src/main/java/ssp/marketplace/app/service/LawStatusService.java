package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.lawStatus.RequestLawStatusCreateDto;
import ssp.marketplace.app.dto.lawStatus.ResponseLawStatusDto;
import ssp.marketplace.app.validation.unique.FieldValueExists;

import java.util.*;

public interface LawStatusService extends FieldValueExists {
    List<ResponseLawStatusDto> getAllStatuses();

    ResponseLawStatusDto createLawStatus(RequestLawStatusCreateDto dto);

    void deleteLawStatus(UUID id);
}
