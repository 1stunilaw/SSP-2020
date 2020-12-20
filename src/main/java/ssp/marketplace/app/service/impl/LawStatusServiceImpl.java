package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.lawStatus.RequestLawStatusCreateDto;
import ssp.marketplace.app.dto.lawStatus.ResponseLawStatusDto;
import ssp.marketplace.app.entity.statuses.StateStatus;
import ssp.marketplace.app.entity.supplier.LawStatus;
import ssp.marketplace.app.exceptions.NotFoundException;
import ssp.marketplace.app.repository.LawStatusRepository;
import ssp.marketplace.app.service.LawStatusService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LawStatusServiceImpl implements LawStatusService {

    private LawStatusRepository lawStatusRepository;

    @Autowired
    public LawStatusServiceImpl(LawStatusRepository lawStatusRepository) {
        this.lawStatusRepository = lawStatusRepository;
    }

    @Override
    public List<ResponseLawStatusDto> getAllStatuses() {
        return lawStatusRepository.findByStatus(StateStatus.ACTIVE).stream().map(ResponseLawStatusDto::new).collect(Collectors.toList());
    }

    @Override
    public ResponseLawStatusDto createLawStatus(RequestLawStatusCreateDto dto) {
        LawStatus lawStatus = new LawStatus(dto.getName());
        return new ResponseLawStatusDto(lawStatusRepository.save(lawStatus));
    }

    @Override
    public void deleteLawStatus(UUID id) {
        LawStatus lawStatus = lawStatusRepository.findByIdAndStatus(id, StateStatus.ACTIVE)
                .orElseThrow(()-> new NotFoundException("Юридического статуса с данным ID не существует"));

        lawStatus.setStatus(StateStatus.DELETED);
        lawStatusRepository.save(lawStatus);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if ("name".equals(fieldName)){
            Set<LawStatus> result = lawStatusRepository.findByNameAndStatus(value.toString(), StateStatus.ACTIVE);
            return !result.isEmpty();
        }

        throw new UnsupportedOperationException("Данное поле не поддерживается");
    }
}
