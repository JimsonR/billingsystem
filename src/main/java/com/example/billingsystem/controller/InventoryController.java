package com.example.billingsystem.controller;

import com.example.billingsystem.model.InventoryModel;
import com.example.billingsystem.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{id}")// ee endpoint vadithe ee method call avtundhi
    public ResponseEntity<?> getInventory(@PathVariable("id") long id){
        return ResponseEntity.ok(inventoryService.getInventory(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> setInventory(@RequestBody InventoryModel inventoryModel)
    {
        return ResponseEntity.ok(inventoryService.createUpdateInventory(inventoryModel));
    }

   /* @PutMapping("/updateinventory")
    public ResponseEntity<String> putInventory(@RequestBody InventoryModel inventoryModel){
        return ResponseEntity.ok(inventoryService.createUpdateInventory(inventoryModel));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable ("id") long id){
        return ResponseEntity.ok(inventoryService.deleteInventory(id));
    }// request param ante /deleteinventory?id=1 ala vastadhi , path variable ante /deleteinventory/1



    @GetMapping("/all")
    public ResponseEntity<?> getAllInv(@RequestParam("pgNo")int pgNo, @RequestParam("pgSize") int pgSize){
        return ResponseEntity.ok(inventoryService.getAllInventory(pgNo,pgSize));
    }


}
