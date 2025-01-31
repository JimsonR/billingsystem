package com.example.billingsystem.service;

import com.example.billingsystem.Exceptions.CustomerNotFoundException;
import com.example.billingsystem.Exceptions.InventoryNotFoundException;
import com.example.billingsystem.Exceptions.OrderNotFoundException;
import com.example.billingsystem.Exceptions.ProductNotFoundException;
import com.example.billingsystem.entity.*;
import com.example.billingsystem.events.InvoiceGeneratedEvent;
import com.example.billingsystem.model.CustomerModel;
import com.example.billingsystem.model.OrderModel;
import com.example.billingsystem.model.OrderResponseDTO;
import com.example.billingsystem.repository.CustomerRepository;
import com.example.billingsystem.repository.InventoryRepository;
import com.example.billingsystem.repository.OrderRepository;
import com.example.billingsystem.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public String createAndUpdate(OrderModel orderModel) throws Exception {


        if (orderModel.getId() != null ){
            Orders order = orderRepository.findById(orderModel.getId()).orElseThrow(()-> new OrderNotFoundException());

            order.setOrderDate(order.getOrderDate());

            order.setCustomer(customerRepository.findByMobileNumber(orderModel
                    .getCustomer()
                    .getMobileNumber())
                    .orElseThrow(()-> new CustomerNotFoundException()));

            if(customerService
                    .findByPhNo(orderModel
                            .getCustomer()
                            .getMobileNumber()) == null){

                customerService
                        .createAndUpdate(orderModel
                                .getCustomer());
            }

            for(long productId : orderModel.getProducts()){
                order.getProducts().add(productRepository.findById(productId).orElseThrow(()-> new RuntimeException("no product found")));
            }
            List<Long> productIds = orderModel.getProducts();

            Map<Long, Integer> productCounts = productIds.stream().collect(Collectors.toMap(id -> id,id -> 1,Integer :: sum));



            BigDecimal total = BigDecimal.valueOf(0);

            for (Map.Entry<Long , Integer> entry : productCounts.entrySet()){
                Long productId = entry.getKey();
                int quantityOrdered = entry.getValue();

                Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException());

                List<Inventory> inventories = inventoryRepository.findByProductIdProductId(productId);
                if (inventories.isEmpty()){
                    throw new InventoryNotFoundException();
                }
                int totalStock = inventories.stream().mapToInt(Inventory::getStockQuantity).sum();
                if (totalStock < quantityOrdered){
                    throw new RuntimeException("Insufficient stock for product:"+product.getName());
                }
                //deduct stock iteratively from inventories
                int remainingQuantity = quantityOrdered;
                for (Inventory inventory : inventories){
                    if(remainingQuantity == 0) break;

                    int availableStock = inventory.getStockQuantity();
                    if (availableStock > 0){
                        int deducted = Math.min(remainingQuantity , availableStock);
                        inventory.setStockQuantity(availableStock - deducted);
                        inventoryRepository.save(inventory);
                        remainingQuantity -= deducted;
                    }
                }

                //caluculate the cost for this product

                BigDecimal productCost = inventories.get(0).getUnitPrice().multiply(BigDecimal.valueOf(quantityOrdered));
                total = total.add(productCost);
            }

//            order.setTotalPrice(total);
//            orderRepository.save(order);
//
//            for (Product product : order.getProducts()){
//              total =   total.add(inventoryService.findByProdId(product.getProductId()).getFirst().getUnitPrice());
//            }
//            order.setStatus(order.getStatus());
//            order.setTotalPrice(total);
            Orders ordered = orderRepository.save(order);
//           invoiceService.generateInvoice(orderRepository.save(order));
            eventPublisher.publishEvent(new InvoiceGeneratedEvent(this, ordered));

            return "order updated";
        }

        Orders orders = new Orders();

        orders.setCustomer(customerRepository.findByMobileNumber(orderModel.
                getCustomer().
                getMobileNumber()).
                orElseThrow(()-> new RuntimeException("Customer not found")));
        if(orders.getCustomer() == null){
            customerService.createAndUpdate(orderModel.getCustomer());
        }
        orders.setOrderDate(LocalDateTime.now());

        List<Long> productIds = orderModel.getProducts();
        Map<Long,Integer> productCounts = productIds.stream().collect(Collectors.toMap(id -> id, id ->1 , Integer :: sum));

        List<Product> products = new ArrayList<>();

        BigDecimal total = new BigDecimal(0);

        for (Map.Entry<Long,Integer> entry : productCounts.entrySet()){
            Long productId = entry.getKey();
            int quantityOrdered = entry.getValue();

            Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException());

            List<Inventory> inventories = inventoryRepository.findByProductIdProductId(productId);
            if (inventories.isEmpty()){
                throw new InventoryNotFoundException();
            }

            int totalStock = inventories.stream().mapToInt(Inventory :: getStockQuantity).sum();
            if (totalStock< quantityOrdered){
                throw new RuntimeException("Insufficient stock for product:" + product.getName());
            }

            int remainingQuantity = quantityOrdered;
            for (Inventory inventory : inventories){
                if (remainingQuantity == 0) break;

                int availableStock = inventory.getStockQuantity();
                if(availableStock > 0){
                    int deducted = Math.min(remainingQuantity , availableStock);
                    inventory.setStockQuantity(availableStock - deducted);
                    inventoryRepository.save(inventory);
                    remainingQuantity -= deducted;
                }
            }   //Calculate the cost for this product

            BigDecimal productCost = inventories.get(0).getUnitPrice().multiply(BigDecimal.valueOf(quantityOrdered));
            total = total.add(productCost);
            for (int i = 0; i < quantityOrdered; i++) {
                products.add(product);
            }
        }

//        for(long id : orderModel.getProducts()){
//
//          Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException());
//           products.add(product);
//          total = total.add(inventoryService.
//                    findByProdId(
//                            product.getProductId())
//                    .getFirst()
//                    .getUnitPrice());
//
//        }

        orders.setProducts(products);
        orders.setTotalPrice(total);
        orders.setStatus(orderModel.getStatus());
        Orders ordered = orderRepository.save(orders);
//           invoiceService.generateInvoice(orderRepository.save(order));
        eventPublisher.publishEvent(new InvoiceGeneratedEvent(this, ordered));
        return "Order created successfully";


    }

    public OrderResponseDTO getOrder(long id){
        return (OrderResponseDTO)orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException());
    }

    public List<OrderResponseDTO> getOrders(int pgNo , int pgSize){
//        List<Orders> orders = orderRepository.findAll();
//        List<OrderResponseDTO> response = new ArrayList<>();
//        for (Orders i : orders){
//            response.add((OrderResponseDTO) i);
//        }

        Pageable pageable = PageRequest.of(pgNo,pgSize);

        return orderRepository.orderList(pageable).getContent();
    }

    public String deleteOrder(long id){

        productRepository.deleteById(id);

        return "Product deleted";
    }

    public List<OrderResponseDTO> findByCustomerMobileNumber(String term , int pgNo , int pgSize ){
    Pageable page = PageRequest.of(pgNo , pgSize);
    return orderRepository.search(term , page).getContent();
    }





}
