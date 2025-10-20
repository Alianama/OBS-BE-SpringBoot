package com.backend.testing.service.impl;

import com.backend.testing.dto.orderRequest;
import com.backend.testing.exception.ResourceNotFoundException;
import com.backend.testing.model.Inventory;
import com.backend.testing.model.Item;
import com.backend.testing.model.Order;
import com.backend.testing.repository.InventoryRepository;
import com.backend.testing.repository.ItemRepository;
import com.backend.testing.repository.OrderRepository;
import com.backend.testing.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository ItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public Order createOrder(orderRequest request) {
        Item item = ItemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + request.getItemId()));

        int availableQty = inventoryRepository.getAvailableQuantity(request.getItemId());
        if (availableQty < request.getQty()) {
            throw new IllegalStateException(
                    "Insufficient stock. Available: " + availableQty + ", Requested: " + request.getQty());
        }

        Inventory withdrawalRecord = new Inventory();
        withdrawalRecord.setItem(item);
        withdrawalRecord.setQty(request.getQty());
        withdrawalRecord.setType(Inventory.Type.W);
        inventoryRepository.save(withdrawalRecord);

        Order order = new Order();
        order.setItem(item);
        order.setQty(request.getQty());
        order.setPrice(item.getPrice() * request.getQty());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, orderRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        Item item = request.getItemId() != null && !request.getItemId().equals(existingOrder.getItem().getId())
                ? ItemRepository.findById(request.getItemId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Item not found with id: " + request.getItemId()))
                : existingOrder.getItem();

        if (request.getQty() != null && !request.getQty().equals(existingOrder.getQty())
                || !item.getId().equals(existingOrder.getItem().getId())) {

            if (!item.getId().equals(existingOrder.getItem().getId())) {
                Inventory returnRecord = new Inventory();
                returnRecord.setItem(existingOrder.getItem());
                returnRecord.setQty(existingOrder.getQty());
                returnRecord.setType(Inventory.Type.T);
                inventoryRepository.save(returnRecord);
            }

            int availableQty = inventoryRepository.getAvailableQuantity(item.getId());
            int oldQty = existingOrder.getQty();
            int newQty = request.getQty() != null ? request.getQty() : oldQty;

            if (item.getId().equals(existingOrder.getItem().getId())) {
                if (newQty > oldQty && (availableQty < (newQty - oldQty))) {
                    throw new IllegalStateException(
                            "Insufficient stock. Available: " + availableQty +
                                    ", Additional requested: " + (newQty - oldQty));
                }
            } else {
                if (availableQty < newQty) {
                    throw new IllegalStateException(
                            "Insufficient stock. Available: " + availableQty +
                                    ", Requested: " + newQty);
                }
            }

            if (newQty != oldQty || !item.getId().equals(existingOrder.getItem().getId())) {
                if (item.getId().equals(existingOrder.getItem().getId())) {
                    Inventory existingWithdrawal = inventoryRepository
                            .findWithdrawalRecordByItemAndQty(existingOrder.getItem(), oldQty)
                            .orElseThrow(() -> new IllegalStateException("Could not find original withdrawal record"));
                    existingWithdrawal.setQty(newQty);
                    inventoryRepository.save(existingWithdrawal);
                } else {
                    Inventory withdrawalRecord = new Inventory();
                    withdrawalRecord.setItem(item);
                    withdrawalRecord.setQty(newQty);
                    withdrawalRecord.setType(Inventory.Type.W);
                    inventoryRepository.save(withdrawalRecord);
                }
            }
        }

        existingOrder.setItem(item);
        if (request.getQty() != null) {
            existingOrder.setQty(request.getQty());
            existingOrder.setPrice(item.getPrice() * request.getQty());
        }

        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        Inventory returnRecord = new Inventory();
        returnRecord.setItem(order.getItem());
        returnRecord.setQty(order.getQty());
        returnRecord.setType(Inventory.Type.T);
        inventoryRepository.save(returnRecord);

        orderRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<Order> getOrderWithPagination(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
