package com.poc.reactmongotransaction.publisher;

import com.poc.reactmongotransaction.config.MessagingConfig;
import com.poc.reactmongotransaction.model.UserDetail;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class UserDetailPublisher {

    private RabbitTemplate template;

    @Autowired
    public UserDetailPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishMessage(UserDetail userDetail) {
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, userDetail);
    }

    public void publishMessages(Flux<UserDetail> list) {
        //list.subscribe(obj -> this.publishMessage(obj));
       // list.doOnNext(obj -> this.publishMessage(obj)).blockLast();
       /* Iterator<UserDetail> users = list.toIterable().iterator();
        while (users.hasNext()){
            publishMessage(users.next());
        }*/
        list.collectList().flatMap(userDetails -> publishMessageList(userDetails));
    }

    private Mono<Void> publishMessageList(List<UserDetail> userDetails) {
        for (UserDetail user:userDetails) {
            publishMessage(user);
        }
        return null;
    }
}
