package org.pqkkkkk.ticsys.order_service.infrastructure.dao.jpa_repository;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
