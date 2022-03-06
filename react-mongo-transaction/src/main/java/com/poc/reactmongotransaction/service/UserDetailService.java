package com.poc.reactmongotransaction.service;

import com.poc.reactmongotransaction.model.UserDetail;
import com.poc.reactmongotransaction.publisher.UserDetailPublisher;
import com.poc.reactmongotransaction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailService {

    private UserRepository repository;

    private UserDetailPublisher publisher;


    @Autowired
    public UserDetailService(UserRepository repository, UserDetailPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public Mono<UserDetail> addUser(UserDetail user){
        return repository.save(user).doOnSuccess(userDetail -> publisher.publishMessage(userDetail));
       // return repository.save(user).doOnSuccess(userDetail -> printMessage(userDetail));
    }

    @Transactional
    public Flux<UserDetail> addUsers(Flux<UserDetail> users){
        Flux<UserDetail> returnList = repository.saveAll(users);
                /*.map(entity -> entity)
                .flatMap(repository::saveAll);*/
        publisher.publishMessages(returnList);
        return returnList;
    }

    public Flux<UserDetail> getUsers(){
        return repository.findAll();
    }

    //This works fine
    private void printMessage(UserDetail userDetail) {
        System.out.println(userDetail);
        throw new RuntimeException("Pretend I failed..");
    }
}
