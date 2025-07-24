package com.foodcourt.plaza_service.infrastructure.output.feign.client;

import com.foodcourt.plaza_service.infrastructure.output.feign.dto.request.MessagingRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "messaging-service", url = "${feign.client.messaging-service.url}")
public interface IMessagingFeignClient {
    @PostMapping("/api/v1/notification/send-sms")
    void sendNotification(MessagingRequestDto messagingRequestDto);
}
