package com.contact.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contact.model.MyOrder;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {
	
	MyOrder findByOrderId(String orderId);

}