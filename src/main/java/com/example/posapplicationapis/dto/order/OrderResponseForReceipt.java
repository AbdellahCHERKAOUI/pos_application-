package com.example.posapplicationapis.dto.order;

import com.example.posapplicationapis.dto.product.ProductResponseForOrder;
import lombok.Data;

import java.util.Map;

@Data
public class OrderResponseForReceipt {
    Map<ProductResponseForOrder,Integer> productAndQuantity;
}
