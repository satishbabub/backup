package com.poc.reactmongotransaction.consumer;

import com.poc.reactmongotransaction.config.MessagingConfig;
import com.poc.reactmongotransaction.model.UserDetail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserDetailConsumer {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void readMsgFromQueue(UserDetail userDetail) {
        System.out.println(String.format("Message from Queue:  %s", userDetail));
    }
}
