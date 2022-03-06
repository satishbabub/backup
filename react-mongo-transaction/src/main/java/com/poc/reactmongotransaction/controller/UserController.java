package com.poc.reactmongotransaction.controller;

import com.poc.reactmongotransaction.model.UserDetail;
import com.poc.reactmongotransaction.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserDetailService service;

    @Autowired
    public UserController(UserDetailService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<UserDetail> addUser(@RequestBody final UserDetail userDetail){
        return service.addUser(userDetail);
    }

    @GetMapping
    public Flux<UserDetail> getUsers(){
         return service.getUsers();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserDetail> getUserStream(){
        return service.getUsers().delayElements(Duration.ofSeconds(1));
    }

    @PostMapping(value = "/list")
    public Flux<UserDetail> addUsers(@RequestBody final Flux<UserDetail> users){
        return service.addUsers(users);
    }

}
