package ssp.marketplace.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.entity.Order;

@Service
public class ConvertServices implements Converter<String, RequestOrderDto> {

    private final ObjectMapper objectMapper;

    public ConvertServices(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public RequestOrderDto convert(String source) {
        return objectMapper.readValue(source, RequestOrderDto.class);
    }
}
