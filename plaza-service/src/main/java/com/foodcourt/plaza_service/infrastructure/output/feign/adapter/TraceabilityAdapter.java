package com.foodcourt.plaza_service.infrastructure.output.feign.adapter;

import com.foodcourt.plaza_service.domain.model.Traceability;
import com.foodcourt.plaza_service.domain.spi.ITraceabilityPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.feign.client.ITraceabilityFeignClient;
import com.foodcourt.plaza_service.infrastructure.output.feign.mapper.ITraceabilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraceabilityAdapter implements ITraceabilityPersistencePort {
    private final ITraceabilityFeignClient traceabilityFeignClient;
    private final ITraceabilityMapper traceabilityMapper;

    @Override
    public void logOrderTrace(Traceability trace) {
        traceabilityFeignClient.logOrderTrace(traceabilityMapper.toRequestDto(trace));
    }
}
