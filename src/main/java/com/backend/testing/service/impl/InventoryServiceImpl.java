package com.backend.testing.service.impl;

import com.backend.testing.model.Inventory;
import com.backend.testing.repository.InventoryRepository;
import com.backend.testing.service.interfaces.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getAllStocks() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getStockById(Long id) {
        Optional<Inventory> stock = inventoryRepository.findById(id);
        return stock.orElse(null);
    }

    @Override
    public Inventory createStock(Inventory stock) {
        return inventoryRepository.save(stock);
    }

    @Override
    public Inventory updateStock(Long id, Inventory updatedStock) {
        Optional<Inventory> existingStock = inventoryRepository.findById(id);

        if (existingStock.isPresent()) {
            Inventory stock = existingStock.get();
            stock.setItem_Id(updatedStock.getItem_Id());
            stock.setQty(updatedStock.getQty());
            stock.setInventory_type(updatedStock.getInventory_type());
            return inventoryRepository.save(stock);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteStock(Long id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<Inventory> getStocksWithPagination(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }
}
