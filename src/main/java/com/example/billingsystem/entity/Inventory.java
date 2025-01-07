package com.example.billingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "productId" , nullable = false)
    private Product productId;

@Column(nullable = false)
    private Integer stockQuantity;

@Column(nullable = false)
    private Integer reorderLevel;

@Column(length = 225)
    private String supplierName;

@Column(nullable = false , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastStockDate;

@Column(length = 225)
    private String location;

@Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal unitPrice;

@Column(nullable = false,precision = 15,scale = 2)
    private BigDecimal totalValue;

@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Integer getStockQuantity() {
        if(stockQuantity == null || stockQuantity < 0){
            throw new IllegalArgumentException("Stock Quantity cannot be null or negative");
        }
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        if(stockQuantity == null || stockQuantity < 0){
            throw new IllegalArgumentException("Stock Quantity cannot be null or negative");
        }
        this.stockQuantity = stockQuantity;
    }

    public Integer getReorderLevel() {
        if(reorderLevel == null || reorderLevel < 0){
            throw new IllegalArgumentException("Stock Quantity cannot be null or negative");
        }
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        if(reorderLevel == null || reorderLevel < 0){
            throw new IllegalArgumentException("Stock Quantity cannot be null or negative");
        }
        this.reorderLevel = reorderLevel;
    }

    public String getSupplierName() {
        if(supplierName == null || supplierName.trim().isEmpty()){
            throw new IllegalArgumentException("Supplier Name cannot be null or empty");
        }
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        if(supplierName == null || supplierName.trim().isEmpty()){
            throw new IllegalArgumentException("Supplier Name cannot be null or empty");
        }
        this.supplierName = supplierName;
    }

    public LocalDateTime getLastStockDate() {
        return lastStockDate;
    }

    public void setLastStockDate(LocalDateTime lastStockDate) {
        this.lastStockDate = lastStockDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getUnitPrice() {
        if(unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unit Price cannot be null or negative");
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if(unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unit Price cannot be null or negative");
        }
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalValue() {
      if(totalValue == null || unitPrice.compareTo(BigDecimal.ZERO) < 0){
          throw new IllegalArgumentException("Total value cannot be null or negative");
      }
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        if(totalValue == null || unitPrice.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Total value cannot be null or negative");
        }
        this.totalValue = totalValue;
    }

    public Boolean getActive() {

        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

//
////    @Column(name = "name" , nullable = false)
////    private String name;
//
//    @Column(name = "category" , nullable = false)
//    private String category;
//
////    @Column(name = "description")
////    private String description;
//
//    @Column(name = "price" , nullable = false )
//    private BigDecimal price;
//
//    @Column(name = "available_quantity" , nullable = false)
//    private Integer availableQuantity;
//
//    @Column(name = "is_active" , nullable = false , columnDefinition = "BOOLEAN DEFAULT TRUE")
//    private Boolean isActive;
//
//    @Column(name = "created_at" , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at" , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private LocalDateTime updateAt;
//
//    public Product getProductId() {
//        return productId;
//    }
//
//    public void setProductId(Product productId) {
//        this.productId = productId;
//    }
//
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
//
//    public Long getInventoryId() {
//        return inventoryId;
//    }
//
//    public void setInventoryId(Long inventoryId) {
//        this.inventoryId = inventoryId;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
////    public String getDescription() {
////        return description;
////    }
////
////    public void setDescription(String description) {
////        this.description = description;
////    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        if (price.compareTo(BigDecimal.ZERO) <= 0){
//            throw new IllegalArgumentException("Price must be greater than zero");
//        }
//        this.price = price;
//    }
//
//    public Integer getAvailableQuantity() {
//        if (availableQuantity != null && availableQuantity < 0){
//            throw new IllegalArgumentException("Available quantity cannot be negative");
//        }
//        return availableQuantity;
//    }
//
//    public void setAvailableQuantity(Integer availableQuantity) {
//        this.availableQuantity = availableQuantity;
//    }
//
//    public Boolean getIsActive() {
//        return isActive;
//    }
//
//    public void setIsActive(Boolean isActive) {
//        this.isActive = isActive;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdateAt() {
//        return updateAt;
//    }
//
//    public void setUpdateAt(LocalDateTime updateAt) {
//        this.updateAt = updateAt;
//    }
}
