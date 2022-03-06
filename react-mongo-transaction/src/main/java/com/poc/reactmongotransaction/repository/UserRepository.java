package com.poc.reactmongotransaction.repository;

import com.poc.reactmongotransaction.model.UserDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigInteger;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDetail, BigInteger> {
    //Flux<UserDetail> findByName(String name);
}
