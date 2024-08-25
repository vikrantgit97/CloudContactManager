package com.project.repo;

import com.project.entities.MyOrder;
import com.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {
	
	MyOrder findByOrderId(String orderId);

	List<MyOrder> findByUser(User user);
}
