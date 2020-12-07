package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.user.supplier.LawStatusResponseDto;

import java.util.List;

public interface LawStatusService {
    List<LawStatusResponseDto> getAllStatuses();
}
