package com.veracode.reactivetransactions.poc.repository;

import com.veracode.reactivetransactions.poc.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
   // Mono<User> findByName(String name);
}
