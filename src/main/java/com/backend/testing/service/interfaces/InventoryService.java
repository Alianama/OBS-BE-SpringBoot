package com.backend.testing.service.interfaces;

import com.backend.testing.dto.InventoryRequest;
import com.backend.testing.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InventoryService {

    List<Inventory> getAllInventories();

    Inventory getInventoryById(Long id);

    Inventory createInventory(InventoryRequest request);

    Inventory updateInventory(Long id, InventoryRequest request);

    boolean deleteInventory(Long id);

    Page<Inventory> getInventoriesWithPagination(Pageable pageable);
}
