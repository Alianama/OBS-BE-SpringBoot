package com.backend.testing.service.interfaces;

import com.backend.testing.model.Order;
import com.backend.testing.dto.orderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(orderRequest request);

    Order updateOrder(Long id, orderRequest request);

    boolean deleteOrder(Long id);

    Page<Order> getOrderWithPagination(Pageable pageable);
}
