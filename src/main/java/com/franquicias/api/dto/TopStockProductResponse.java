package com.franquicias.api.dto;

/** Producto con más stock de una sucursal, indicando a qué sucursal pertenece. */
public record TopStockProductResponse(
        String branchId,
        String branchName,
        String productId,
        String productName,
        int stock) {
}
