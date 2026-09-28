package com.franquicias.api.controller;

import com.franquicias.api.domain.Franchise;
import com.franquicias.api.dto.Requests.NameRequest;
import com.franquicias.api.dto.Requests.ProductRequest;
import com.franquicias.api.dto.Requests.StockRequest;
import com.franquicias.api.dto.TopStockProductResponse;
import com.franquicias.api.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseService service;

    public FranchiseController(FranchiseService service) {
        this.service = service;
    }

    // ---------- Franquicias ----------

    @GetMapping
    public Flux<Franchise> findAll() {
        return service.findAll();
    }

    @GetMapping("/{franchiseId}")
    public Mono<Franchise> findById(@PathVariable String franchiseId) {
        return service.findById(franchiseId);
    }

    /** Criterio 2: agregar una nueva franquicia. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> createFranchise(@Valid @RequestBody NameRequest request) {
        return service.createFranchise(request.name());
    }

    /** Extra: actualizar el nombre de una franquicia. */
    @PatchMapping("/{franchiseId}")
    public Mono<Franchise> updateFranchiseName(@PathVariable String franchiseId,
                                               @Valid @RequestBody NameRequest request) {
        return service.updateFranchiseName(franchiseId, request.name());
    }

    // ---------- Sucursales ----------

    /** Criterio 3: agregar una nueva sucursal a una franquicia. */
    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> addBranch(@PathVariable String franchiseId,
                                     @Valid @RequestBody NameRequest request) {
        return service.addBranch(franchiseId, request.name());
    }

    /** Extra: actualizar el nombre de una sucursal. */
    @PatchMapping("/{franchiseId}/branches/{branchId}")
    public Mono<Franchise> updateBranchName(@PathVariable String franchiseId,
                                            @PathVariable String branchId,
                                            @Valid @RequestBody NameRequest request) {
        return service.updateBranchName(franchiseId, branchId, request.name());
    }

    // ---------- Productos ----------

    /** Criterio 4: agregar un nuevo producto a una sucursal. */
    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> addProduct(@PathVariable String franchiseId,
                                      @PathVariable String branchId,
                                      @Valid @RequestBody ProductRequest request) {
        return service.addProduct(franchiseId, branchId, request.name(), request.stock());
    }

    /** Criterio 5: eliminar un producto de una sucursal. */
    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<Franchise> deleteProduct(@PathVariable String franchiseId,
                                         @PathVariable String branchId,
                                         @PathVariable String productId) {
        return service.deleteProduct(franchiseId, branchId, productId);
    }

    /** Criterio 6: modificar el stock de un producto. */
    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<Franchise> updateProductStock(@PathVariable String franchiseId,
                                              @PathVariable String branchId,
                                              @PathVariable String productId,
                                              @Valid @RequestBody StockRequest request) {
        return service.updateProductStock(franchiseId, branchId, productId, request.stock());
    }

    /** Extra: actualizar el nombre de un producto. */
    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<Franchise> updateProductName(@PathVariable String franchiseId,
                                             @PathVariable String branchId,
                                             @PathVariable String productId,
                                             @Valid @RequestBody NameRequest request) {
        return service.updateProductName(franchiseId, branchId, productId, request.name());
    }

    // ---------- Consultas ----------

    /** Criterio 7: producto con más stock por sucursal para una franquicia. */
    @GetMapping("/{franchiseId}/top-stock-products")
    public Flux<TopStockProductResponse> topStockProducts(@PathVariable String franchiseId) {
        return service.topStockProductsByBranch(franchiseId);
    }
}
