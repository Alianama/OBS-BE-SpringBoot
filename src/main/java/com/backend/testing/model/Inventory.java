package com.backend.testing.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    private Integer qty;

    public enum Type {
        W,
        T
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 1)
    private Type type;
}
