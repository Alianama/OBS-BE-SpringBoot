package com.backend.testing.controller;

import com.backend.testing.model.Item;
import com.backend.testing.service.interfaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<Page<Item>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemsPage = itemService.getItemsWithPagination(pageable);
        return ResponseEntity.ok(itemsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Item updatedItem) {
        Item item = itemService.updateItem(id, updatedItem);
        Map<String, Object> response = new HashMap<>();

        if (item == null) {
            response.put("message", "Item not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("message", "Item updated successfully");
        response.put("data", item);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        boolean deleted = itemService.deleteItem(id);
        Map<String, String> response = new HashMap<>();

        if (!deleted) {
            response.put("message", "Item not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("message", "Item deleted successfully");
        return ResponseEntity.ok(response);
    }
}
