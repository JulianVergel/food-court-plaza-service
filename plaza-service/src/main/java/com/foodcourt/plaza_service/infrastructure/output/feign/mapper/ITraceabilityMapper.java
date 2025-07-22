package com.foodcourt.plaza_service.infrastructure.output.feign.mapper;

import com.foodcourt.plaza_service.domain.model.Traceability;
import com.foodcourt.plaza_service.infrastructure.output.feign.dto.request.TraceabilityRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceabilityMapper {
    TraceabilityRequestDto toRequestDto(Traceability traceability);
}
