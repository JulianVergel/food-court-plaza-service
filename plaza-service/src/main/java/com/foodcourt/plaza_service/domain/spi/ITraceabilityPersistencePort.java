package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Traceability;

public interface ITraceabilityPersistencePort {
    void logOrderTrace(Traceability trace);
}
