package org.pqkkkkk.ticsys.order_service.infrastructure.dao;

import org.pqkkkkk.ticsys.order_service.domain.dao.OrderQueryDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.infrastructure.dao.jpa_repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderQueryJpaDao implements OrderQueryDao {
    private final OrderRepository orderRepository;

    public OrderQueryJpaDao(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

}
