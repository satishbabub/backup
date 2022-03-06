package com.veracode.reactivetransactions.poc.controller;

import com.veracode.reactivetransactions.poc.entity.User;
import com.veracode.reactivetransactions.poc.repository.UserRepository;
import com.veracode.reactivetransactions.poc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

   // private UserService svc;

    private UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "/user")
    public Mono<User> addUser(@RequestBody final User user){
        return repository.save(user);
    }

    @GetMapping(value = "/user")
    public Flux<User> getUsers(){
        return repository.findAll();
    }
}
