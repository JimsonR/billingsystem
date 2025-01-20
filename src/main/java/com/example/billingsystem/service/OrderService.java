package com.example.billingsystem.service;

import com.example.billingsystem.Exceptions.OrderNotFoundException;
import com.example.billingsystem.Exceptions.ProductNotFoundException;
import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.entity.Product;
import com.example.billingsystem.model.CustomerModel;
import com.example.billingsystem.model.OrderModel;
import com.example.billingsystem.model.OrderResponseDTO;
import com.example.billingsystem.repository.CustomerRepository;
import com.example.billingsystem.repository.InventoryRepository;
import com.example.billingsystem.repository.OrderRepository;
import com.example.billingsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CustomerService customerService;


    public String createAndUpdate(OrderModel orderModel){


        if (orderModel.getId() != null){
            Orders order = orderRepository.findById(orderModel.getId()).orElseThrow(()-> new RuntimeException("Order not found"));
            order.setOrderDate(order.getOrderDate());
            orderModel.setCustomer(customerRepository.findById(orderModel.getCustomer().getId()).orElseThrow(()-> new RuntimeException("Customer not found")));


            for(long productId : orderModel.getProducts()){
                order.getProducts().add(productRepository.findById(productId).orElseThrow(()-> new RuntimeException("no product found")));

            }
            BigDecimal total = BigDecimal.valueOf(0);
            for (Product product : order.getProducts()){
              total =   total.add(inventoryService.findByProdId(product.getProductId()).getFirst().getUnitPrice());
            }
            order.setStatus(order.getStatus());
            order.setTotalPrice(total);
            orderRepository.save(order);

            return "order updated";
        }

        Orders orders = new Orders();

        orders.setCustomer(customerRepository.findById(orderModel.getCustomer().getId()).orElseThrow(()-> new RuntimeException("Customer not found")));

        orders.setOrderDate(LocalDateTime.now());

        List<Product> products = new ArrayList<>();

        BigDecimal total = new BigDecimal(0);

        for(long id : orderModel.getProducts()){

          Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException());
           products.add(product);
          total = total.add(inventoryService.
                    findByProdId(
                            product.getProductId())
                    .getFirst()
                    .getUnitPrice());

        }

        orders.setProducts(products);
        orders.setTotalPrice(total);
        orders.setStatus(orderModel.getStatus());

return "Order created successfully";


    }

    public OrderResponseDTO getOrder(long id){
        return (OrderResponseDTO)orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException());
    }

    public List<OrderResponseDTO> getOrders(){
        List<Orders> orders = orderRepository.findAll();
        List<OrderResponseDTO> response = new ArrayList<>();
        for (Orders i : orders){
            response.add((OrderResponseDTO) i);
        }
        return response;
    }

    public String deleteOrder(long id){

        productRepository.deleteById(id);

        return "Product deleted";
    }



}
