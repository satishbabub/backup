package com.poc.transactionwithreact.repository;

import com.poc.transactionwithreact.model.UserDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserDetail, Integer> {
    Flux<UserDetail> findByName(String name);
}
