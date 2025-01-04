package com.example.billingsystem.service;

import com.example.billingsystem.entity.Inventory;
import com.example.billingsystem.entity.Product;
import com.example.billingsystem.model.InventoryModel;
import com.example.billingsystem.model.ProductModel;
import com.example.billingsystem.model.ProductResponseModel;
import com.example.billingsystem.repository.InventoryRepository;
import com.example.billingsystem.repository.ProductRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public String createUpdateInventory(InventoryModel inventoryModel){

        if (inventoryModel.getInventoryId() != null){
            Inventory inventory = inventoryRepository.findById(inventoryModel.getInventoryId()).get();
            inventory.setStockQuantity(inventoryModel.getStockQuantity());
            inventory.setReorderLevel(inventoryModel.getReorderLevel());
            inventory.setSupplierName(inventoryModel.getSupplierName());
            inventory.setLastStockDate(LocalDateTime.now());
            inventory.setLocation(inventoryModel.getLocation());
            inventory.setUnitPrice(inventoryModel.getUnitPrice());
            inventory.setTotalValue( inventoryModel.getUnitPrice().multiply(BigDecimal.valueOf(inventoryModel.getStockQuantity())));
            inventoryRepository.save(inventory);
            return "Inventory Updated";
        }
        Product product = productRepository.findById(inventoryModel.getProductId().getProductId()).get();
        Inventory inventory = new Inventory();
                inventory.setProductId(product);
                inventory.setStockQuantity(inventoryModel.getStockQuantity());
                inventory.setReorderLevel(inventoryModel.getReorderLevel());
                inventory.setSupplierName(inventoryModel.getSupplierName());
                inventory.setLastStockDate(LocalDateTime.now());// idhi naku doubt
                inventory.setUnitPrice(inventoryModel.getUnitPrice());
                inventory.setTotalValue(inventoryModel.getUnitPrice().multiply(BigDecimal.valueOf(inventoryModel.getStockQuantity())));
         inventoryRepository.save(inventory);
         return "Inventory Created";
    }

    public InventoryModel getInventory(Long id){
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->new RuntimeException("no inventory found"));
        InventoryModel inventoryModel= InventoryModel.builder()
                .inventoryId(inventory.getInventoryId())
                .productId(ProductResponseModel.builder().productId(inventory.getProductId().getProductId())
                        .name(inventory.getProductId().getName())
                        .description(inventory.getProductId().getDescription())
                        .category(inventory.getProductId().getCategory()).build())

                .reorderLevel(inventory.getReorderLevel())
                .stockQuantity(inventory.getStockQuantity())
                .supplierName(inventory.getSupplierName())
                .location(inventory.getLocation())
                .unitPrice(inventory.getUnitPrice())
                .build();
        return inventoryModel;
    }

    public String deleteInventory(Long id){
        if(inventoryRepository.findById(id).isPresent()) {
            inventoryRepository.deleteById(id);
            return "inventory deleted";
        }
        return "inventory not found";
    }

    public List<InventoryModel> findByProdId(Long id){
        List<Inventory> existInventory = inventoryRepository.findByProductIdProductId(id);
        List<InventoryModel> inventoryModelList = new ArrayList<>();
        for (Inventory inventory : existInventory ) {
            InventoryModel inventoryModel= InventoryModel.builder()
                    .inventoryId(inventory.getInventoryId())
                    .productId(ProductResponseModel.builder().productId(inventory.getProductId().getProductId())
                            .name(inventory.getProductId().getName())
                            .description(inventory.getProductId().getDescription())
                            .category(inventory.getProductId().getCategory())
                            .createdAt(inventory.getProductId().getCreatedAt())
                            .updateAt(inventory.getProductId().getUpdateAt())
                            .isActive(inventory.getProductId().getIsActive()).build())

                    .reorderLevel(inventory.getReorderLevel())
                    .stockQuantity(inventory.getStockQuantity())
                    .supplierName(inventory.getSupplierName())
                    .location(inventory.getLocation())
                    .unitPrice(inventory.getUnitPrice())
                    .build();
            inventoryModelList.add(inventoryModel);
        }

       return inventoryModelList;
    }

}
