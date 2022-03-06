package com.poc.transactionwithreact.publisher;

import com.poc.transactionwithreact.config.MessagingConfig;
import com.poc.transactionwithreact.model.UserDetail;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
/*  public void publishMessage(UserDetail userDetail){
      try {
          template.convertAndSend("Dummy", "Dummy", userDetail);
      }catch (AmqpException e){
          e.printStackTrace();
      }
      catch (Exception e){
          e.printStackTrace();
      }
  }*/
}
