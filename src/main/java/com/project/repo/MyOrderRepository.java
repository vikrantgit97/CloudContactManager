package com.project.repo;

import com.project.entities.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {
	
	MyOrder findByOrderId(String orderId);

}
