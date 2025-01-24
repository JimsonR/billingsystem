package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.model.OrderResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value =" select * from orders as o join customer as c on o.customer = c.id;",nativeQuery = true)
    List<OrderResponseDTO> orderList();
}
