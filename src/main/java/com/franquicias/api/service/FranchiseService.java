package com.franquicias.api.service;

import com.franquicias.api.domain.Branch;
import com.franquicias.api.domain.Franchise;
import com.franquicias.api.domain.Product;
import com.franquicias.api.dto.TopStockProductResponse;
import com.franquicias.api.exception.ConflictException;
import com.franquicias.api.exception.NotFoundException;
import com.franquicias.api.repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class FranchiseService {

    private final FranchiseRepository repository;

    public FranchiseService(FranchiseRepository repository) {
        this.repository = repository;
    }

    // ---------- Franquicias ----------

    public Flux<Franchise> findAll() {
        return repository.findAll();
    }

    public Mono<Franchise> findById(String franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada: " + franchiseId)));
    }

    public Mono<Franchise> createFranchise(String name) {
        String cleanName = name.trim();
        return repository.existsByNameIgnoreCase(cleanName)
                .flatMap(exists -> exists
                        ? Mono.error(new ConflictException("Ya existe una franquicia con el nombre: " + cleanName))
                        : repository.save(new Franchise(null, cleanName, new ArrayList<>())));
    }

    public Mono<Franchise> updateFranchiseName(String franchiseId, String name) {
        return modify(franchiseId, franchise -> franchise.setName(name.trim()));
    }

    // ---------- Sucursales ----------

    public Mono<Franchise> addBranch(String franchiseId, String name) {
        return modify(franchiseId, franchise -> franchise.getBranches()
                .add(new Branch(newId(), name.trim(), new ArrayList<>())));
    }

    public Mono<Franchise> updateBranchName(String franchiseId, String branchId, String name) {
        return modify(franchiseId, franchise -> findBranch(franchise, branchId).setName(name.trim()));
    }

    // ---------- Productos ----------

    public Mono<Franchise> addProduct(String franchiseId, String branchId, String name, int stock) {
        return modify(franchiseId, franchise -> findBranch(franchise, branchId).getProducts()
                .add(new Product(newId(), name.trim(), stock)));
    }

    public Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId) {
        return modify(franchiseId, franchise -> {
            Branch branch = findBranch(franchise, branchId);
            boolean removed = branch.getProducts().removeIf(p -> p.getId().equals(productId));
            if (!removed) {
                throw new NotFoundException("Producto no encontrado: " + productId);
            }
        });
    }

    public Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, int stock) {
        return modify(franchiseId, franchise -> findProduct(findBranch(franchise, branchId), productId).setStock(stock));
    }

    public Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String name) {
        return modify(franchiseId, franchise -> findProduct(findBranch(franchise, branchId), productId).setName(name.trim()));
    }

    // ---------- Consultas ----------

    /**
     * Para cada sucursal de la franquicia devuelve el producto con más stock.
     * Las sucursales sin productos se omiten.
     */
    public Flux<TopStockProductResponse> topStockProductsByBranch(String franchiseId) {
        return findById(franchiseId)
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> Mono.justOrEmpty(branch.getProducts().stream()
                        .max(Comparator.comparingInt(Product::getStock))
                        .map(product -> new TopStockProductResponse(
                                branch.getId(), branch.getName(),
                                product.getId(), product.getName(), product.getStock()))));
    }

    // ---------- Utilidades ----------

    /** Carga la franquicia, aplica el cambio y la guarda. Los errores del cambio se propagan como Mono.error. */
    private Mono<Franchise> modify(String franchiseId, Consumer<Franchise> change) {
        return findById(franchiseId)
                .flatMap(franchise -> Mono.fromCallable(() -> {
                    change.accept(franchise);
                    return franchise;
                }))
                .flatMap(repository::save);
    }

    private static Branch findBranch(Franchise franchise, String branchId) {
        return franchise.getBranches().stream()
                .filter(b -> b.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada: " + branchId));
    }

    private static Product findProduct(Branch branch, String productId) {
        return branch.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + productId));
    }

    private static String newId() {
        return UUID.randomUUID().toString();
    }
}
