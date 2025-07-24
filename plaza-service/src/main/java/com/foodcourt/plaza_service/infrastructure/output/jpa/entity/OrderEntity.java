package com.foodcourt.plaza_service.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_user_id")
    private Long customerId;
    private LocalDate orderDate;
    private String status;
    @Column(name = "chef_id")
    private Long chefId;
    private Long restaurantId;
    private String securityPin;

    @OneToMany(mappedBy = "order")
    private List<OrderDishEntity> orderDishes;
}
