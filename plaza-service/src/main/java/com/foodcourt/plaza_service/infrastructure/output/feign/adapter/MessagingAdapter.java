package com.foodcourt.plaza_service.infrastructure.output.feign.adapter;

import com.foodcourt.plaza_service.domain.spi.IMessagingPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.feign.client.IMessagingFeignClient;
import com.foodcourt.plaza_service.infrastructure.output.feign.dto.request.MessagingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagingAdapter implements IMessagingPersistencePort {

    private final IMessagingFeignClient messagingFeignClient;

    @Override
    public void sendNotification(String message, String phoneNumber) {
        messagingFeignClient.sendNotification(new MessagingRequestDto(message, phoneNumber));
    }
}
