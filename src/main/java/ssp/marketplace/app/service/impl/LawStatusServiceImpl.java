package ssp.marketplace.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.user.supplier.LawStatusResponseDto;
import ssp.marketplace.app.repository.LawStatusRepository;
import ssp.marketplace.app.service.LawStatusService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LawStatusServiceImpl implements LawStatusService {

    private LawStatusRepository lawStatusRepository;

    @Autowired
    public LawStatusServiceImpl(LawStatusRepository lawStatusRepository) {
        this.lawStatusRepository = lawStatusRepository;
    }

    @Override
    public List<LawStatusResponseDto> getAllStatuses() {
        return lawStatusRepository.findAll().stream().map(LawStatusResponseDto::new).collect(Collectors.toList());
    }
}
