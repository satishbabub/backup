package com.poc.transactionwithreact.consumer;

import com.poc.transactionwithreact.config.MessagingConfig;
import com.poc.transactionwithreact.model.UserDetail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserDetailConsumer {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void readMsgFromQueue(UserDetail userDetail) {
        System.out.println(String.format("Message from Queue:  %s", userDetail));
    }
}
