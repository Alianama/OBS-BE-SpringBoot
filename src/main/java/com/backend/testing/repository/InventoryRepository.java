package com.backend.testing.repository;

import com.backend.testing.model.Inventory;
import com.backend.testing.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT COALESCE(SUM(CASE WHEN i.type = 'T' THEN i.qty WHEN i.type = 'W' THEN -i.qty ELSE 0 END), 0) " +
            "FROM Inventory i WHERE i.item.id = :itemId")
    int getAvailableQuantity(@Param("itemId") Long itemId);

    @Query("SELECT i FROM Inventory i WHERE i.item = :item ORDER BY i.id DESC LIMIT 1")
    Optional<Inventory> findLatestByItem(@Param("item") Item item);

    @Query("SELECT i FROM Inventory i WHERE i.item = :item AND i.type = 'W' AND i.qty = :qty ORDER BY i.id DESC LIMIT 1")
    Optional<Inventory> findWithdrawalRecordByItemAndQty(@Param("item") Item item, @Param("qty") Integer qty);
}
