package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;



    private Double price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_quantity", joinColumns = @JoinColumn(name = "order_item_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    @ToString.Exclude
    private Map<Product, Integer> orderedProduct;
}
