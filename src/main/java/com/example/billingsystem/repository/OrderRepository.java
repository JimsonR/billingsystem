package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.model.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value =" select * from orders as o join customer as c on o.customer = c.id;",nativeQuery = true)
    Page<OrderResponseDTO> orderList(Pageable pageable);

    @Query(value = "select * from orders as o join customer as c on o.customer = c.id where c.mobile_number = :term or lower(c.customer_name) like lower(concat('%', :term ,'%'));",nativeQuery = true)
    Page<OrderResponseDTO> search(@Param(
            "term"
    ) String mobileNumber,Pageable pageable);

}
