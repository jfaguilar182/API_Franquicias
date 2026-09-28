package com.franquicias.api.service;

import com.franquicias.api.domain.Branch;
import com.franquicias.api.domain.Franchise;
import com.franquicias.api.domain.Product;
import com.franquicias.api.exception.ConflictException;
import com.franquicias.api.exception.NotFoundException;
import com.franquicias.api.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FranchiseServiceTest {

    private FranchiseRepository repository;
    private FranchiseService service;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        service = new FranchiseService(repository);
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    private Franchise sampleFranchise() {
        Branch centro = new Branch("b1", "Centro", new ArrayList<>(List.of(
                new Product("p1", "Hamburguesa", 10),
                new Product("p2", "Papas", 50))));
        Branch norte = new Branch("b2", "Norte", new ArrayList<>(List.of(
                new Product("p3", "Gaseosa", 30))));
        Branch vacia = new Branch("b3", "Vacia", new ArrayList<>());
        return new Franchise("f1", "Burger", new ArrayList<>(List.of(centro, norte, vacia)));
    }

    @Test
    void createFranchise_savesWhenNameIsFree() {
        when(repository.existsByNameIgnoreCase("Burger")).thenReturn(Mono.just(false));

        StepVerifier.create(service.createFranchise("  Burger "))
                .expectNextMatches(f -> f.getName().equals("Burger") && f.getBranches().isEmpty())
                .verifyComplete();
    }

    @Test
    void createFranchise_failsWhenNameExists() {
        when(repository.existsByNameIgnoreCase("Burger")).thenReturn(Mono.just(true));

        StepVerifier.create(service.createFranchise("Burger"))
                .expectError(ConflictException.class)
                .verify();
        verify(repository, never()).save(any());
    }

    @Test
    void addBranch_addsBranchWithGeneratedId() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.addBranch("f1", "Sur"))
                .expectNextMatches(f -> f.getBranches().size() == 4
                        && f.getBranches().get(3).getName().equals("Sur")
                        && f.getBranches().get(3).getId() != null)
                .verifyComplete();
    }

    @Test
    void addBranch_failsWhenFranchiseDoesNotExist() {
        when(repository.findById("x")).thenReturn(Mono.empty());

        StepVerifier.create(service.addBranch("x", "Sur"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void addProduct_addsProductToBranch() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.addProduct("f1", "b2", "Helado", 7))
                .expectNextMatches(f -> f.getBranches().get(1).getProducts().size() == 2)
                .verifyComplete();
    }

    @Test
    void deleteProduct_removesProduct() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.deleteProduct("f1", "b1", "p1"))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().stream()
                        .noneMatch(p -> p.getId().equals("p1")))
                .verifyComplete();
    }

    @Test
    void deleteProduct_failsWhenProductDoesNotExist() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.deleteProduct("f1", "b1", "nope"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void updateProductStock_changesStock() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.updateProductStock("f1", "b1", "p1", 99))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().get(0).getStock() == 99)
                .verifyComplete();
    }

    @Test
    void updateBranchName_failsWhenBranchDoesNotExist() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.updateBranchName("f1", "nope", "Nuevo"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void topStockProducts_returnsMaxPerBranchAndSkipsEmptyBranches() {
        when(repository.findById("f1")).thenReturn(Mono.just(sampleFranchise()));

        StepVerifier.create(service.topStockProductsByBranch("f1"))
                .expectNextMatches(r -> r.branchName().equals("Centro") && r.productName().equals("Papas") && r.stock() == 50)
                .expectNextMatches(r -> r.branchName().equals("Norte") && r.productName().equals("Gaseosa") && r.stock() == 30)
                .verifyComplete();
    }
}
