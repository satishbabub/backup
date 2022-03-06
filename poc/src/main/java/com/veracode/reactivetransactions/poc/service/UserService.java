package com.veracode.reactivetransactions.poc.service;

import com.veracode.reactivetransactions.poc.entity.User;
import com.veracode.reactivetransactions.poc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

/*    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Mono<User> addUser(User user){
        return repository.save(user);
    }

    public Flux<User> getUsers(){
        return repository.findAll();
    }*/
}
