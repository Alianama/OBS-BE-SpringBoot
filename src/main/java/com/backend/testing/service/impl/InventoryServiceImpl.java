package com.backend.testing.service.impl;

import com.backend.testing.dto.InventoryRequest;
import com.backend.testing.exception.ResourceNotFoundException;
import com.backend.testing.model.Inventory;
import com.backend.testing.model.Item;
import com.backend.testing.repository.InventoryRepository;
import com.backend.testing.repository.ItemRepository;
import com.backend.testing.service.interfaces.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public Page<Inventory> getInventoriesWithPagination(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    @Override
    public Inventory createInventory(InventoryRequest request) {
        Inventory inventory = new Inventory();
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        inventory.setItem(item);
        inventory.setQty(request.getQty());
        inventory.setType(Inventory.Type.valueOf(request.getType()));

        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Long id, InventoryRequest request) {
        Inventory existing = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        existing.setItem(item);
        existing.setQty(request.getQty());
        existing.setType(Inventory.Type.valueOf(request.getType()));

        return inventoryRepository.save(existing);
    }

    @Override
    public boolean deleteInventory(Long id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
