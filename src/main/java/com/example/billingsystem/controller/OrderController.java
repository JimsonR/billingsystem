package com.example.billingsystem.controller;

import com.example.billingsystem.model.OrderModel;
import com.example.billingsystem.model.OrderResponseDTO;
import com.example.billingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

  @PostMapping("add")
    public ResponseEntity<String> createAndUpdate(@RequestBody OrderModel orderModel) throws Exception {
      return ResponseEntity.ok(orderService.createAndUpdate(orderModel));
  }

  @GetMapping("all")
    public ResponseEntity<?> allOrders(@RequestParam int pgNo , @RequestParam int pgSize){
      return ResponseEntity.ok(orderService.getOrders(pgNo,pgSize));
  }

  @PostMapping("customerorders")
    public ResponseEntity<List<OrderResponseDTO>> findByCustomerMobileNumber(@RequestBody String term, @RequestParam int pgNo , @RequestParam int pgSize){
      return ResponseEntity.ok(orderService.findByCustomerMobileNumber(term , pgNo , pgSize));
  }



}
