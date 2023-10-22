package com.acks.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acks.model.MyOrder;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {

    MyOrder findByOrderId(String orderId);

}
