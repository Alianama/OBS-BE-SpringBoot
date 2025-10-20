package com.backend.testing.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.testing.dto.InventoryRequest;
import com.backend.testing.model.Inventory;
import com.backend.testing.service.interfaces.InventoryService;

@RestController
@RequestMapping("/api/inventories")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<Page<Inventory>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventoriesPage = inventoryService.getInventoriesWithPagination(pageable);
        return ResponseEntity.ok(inventoriesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);

        Map<String, Object> response = new HashMap<>();
        if (inventory == null) {
            response.put("message", "Inventory not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("message", "Inventory retrieved successfully");
        response.put("data", inventory);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.createInventory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody InventoryRequest updatedInventory) {
        Inventory inventory = inventoryService.updateInventory(id, updatedInventory);
        Map<String, Object> response = new HashMap<>();

        if (inventory == null) {
            response.put("message", "Inventory not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("message", "Inventory updated successfully");
        response.put("data", inventory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        boolean deleted = inventoryService.deleteInventory(id);
        Map<String, String> response = new HashMap<>();

        if (!deleted) {
            response.put("message", "Item not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("message", "Item deleted successfully");
        return ResponseEntity.ok(response);
    }

}
