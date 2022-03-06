package com.poc.transactionwithreact.service;

import com.poc.transactionwithreact.config.MessagingConfig;
import com.poc.transactionwithreact.consumer.UserDetailConsumer;
import com.poc.transactionwithreact.model.UserDetail;
import com.poc.transactionwithreact.publisher.UserDetailPublisher;
import com.poc.transactionwithreact.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserDetailService {

    private UserRepository repository;

    private UserDetailPublisher publisher;

    //private UserDetailConsumer consumer;

    @Autowired
    public UserDetailService(UserRepository repository, UserDetailPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
       // this.consumer = consumer;
    }

    @Transactional
    public Mono<UserDetail> addUser(UserDetail user){
        return repository.save(user).doOnSuccess(userDetail -> publisher.publishMessage(userDetail));
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
