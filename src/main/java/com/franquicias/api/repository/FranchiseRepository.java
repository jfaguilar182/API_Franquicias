package com.franquicias.api.repository;

import com.franquicias.api.domain.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {

    Mono<Boolean> existsByNameIgnoreCase(String name);
}
