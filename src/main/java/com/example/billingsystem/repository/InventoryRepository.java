package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query
    List<Inventory> findByProductIdProductId(Long id);// find by id gurthundha akkada adhi primary key lagutadhi kadha





}// ikkada productid anedhi foriegn key field ante product object aa product object lo unna id productid , ee id batti inventory table laganu
//idhe native query lo select * from inventory where product_id = 1 alaga anna maaata