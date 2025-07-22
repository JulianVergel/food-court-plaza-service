package com.foodcourt.plaza_service.infrastructure.output.feign.adapter;

import com.foodcourt.plaza_service.domain.spi.IEmployeePersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.feign.client.IUserFeignClient;
import com.foodcourt.plaza_service.infrastructure.output.feign.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.foodcourt.plaza_service.infrastructure.utils.constants.FeignConstants.EMPLOYEE_NOT_FOUND_MESSAGE;

@Component
@RequiredArgsConstructor
public class EmployeeJpaAdapter implements IEmployeePersistencePort {

    private final IUserFeignClient userFeignClient;

    @Override
    public Long findRestaurantIdByEmployeeId(Long employeeId) {
        UserResponseDto employee = userFeignClient.getUserById(employeeId);
        if (employee == null) {
            throw new RuntimeException(EMPLOYEE_NOT_FOUND_MESSAGE);
        }
        return employee.getRestaurantId();
    }
}
