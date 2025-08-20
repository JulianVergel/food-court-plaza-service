package com.foodcourt.plaza_service.infrastructure.output.feign.client;

import com.foodcourt.plaza_service.infrastructure.output.feign.dto.request.TraceabilityRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "tracking-service", url = "${feign.client.tracking-service.url}")
public interface ITraceabilityFeignClient {
    @PostMapping("/api/v1/trace/")
    void logOrderTrace(TraceabilityRequestDto trace);
}
