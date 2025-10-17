package com.backend.testing.service.interfaces;

import com.backend.testing.model.Item;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {
  List<Item> getAllItems();

  Item getItemById(Long id);

  Item createItem(Item item);

  Item updateItem(Long id, Item updatedItem);

  boolean deleteItem(Long id);

  Page<Item> getItemsWithPagination(Pageable pageable);

}
