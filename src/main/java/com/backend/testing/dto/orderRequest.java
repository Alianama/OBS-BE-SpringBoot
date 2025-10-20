package com.backend.testing.dto;

import lombok.Data;

@Data
public class orderRequest {
    private Long itemId;
    private Integer qty;
}
