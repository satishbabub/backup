package com.poc.transactionwithreact.controller;

import com.poc.transactionwithreact.model.UserDetail;
import com.poc.transactionwithreact.repository.UserRepository;
import com.poc.transactionwithreact.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Collectors;

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
        //return service.getUsers();
        return service.getUsers().delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(value = "/number-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getNumberStream(){
        return Flux.just(1,2,3,4,5,6,7,8,9).delayElements(Duration.ofSeconds(1)).log();
    }

  /*  @GetMapping("/{name}")
    public Flux<UserDetail> getUsersByName(@PathVariable String name){
        return repository.findByName(name);
    }*/

   /* @PostMapping
    public Mono<User> addUser(@RequestBody final User user){
        //return repository.save(user);
        return getTemplate().insert(User.class).using(user);
    }

    @GetMapping
    public Flux<User> getUsers(){
        return getTemplate().select(User.class).all();
       // return repository.findAll();
    }

    private R2dbcEntityTemplate getTemplate(){
        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host("localhost:8088")
                .database("test-transaction-react")
                .username("test")
                .password("test").build());

        return new R2dbcEntityTemplate(connectionFactory);
    }*/
}
