package org.pqkkkkk.ticsys.order_service.infrastructure.dao;

import org.pqkkkkk.ticsys.order_service.domain.dao.OrderCommandDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.infrastructure.dao.jpa_repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCommandJpaDao implements OrderCommandDao {
    private final OrderRepository orderRepository;

    public OrderCommandJpaDao(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(Order order) {
        
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOrder'");
    }

}
