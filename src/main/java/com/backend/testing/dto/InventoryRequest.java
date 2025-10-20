package com.backend.testing.dto;

import lombok.Data;

@Data
public class InventoryRequest {
    private Long itemId;
    private Integer qty;
    private String type;
}
