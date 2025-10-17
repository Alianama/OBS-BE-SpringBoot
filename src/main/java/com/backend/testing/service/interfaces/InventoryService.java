package com.backend.testing.service.interfaces;

import com.backend.testing.model.Inventory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    List<Inventory> getAllStocks();

    Inventory getStockById(Long id);

    Inventory createStock(Inventory stock);

    Inventory updateStock(Long id, Inventory updatedStock);

    boolean deleteStock(Long id);

    Page<Inventory> getStocksWithPagination(Pageable pageable);

}
