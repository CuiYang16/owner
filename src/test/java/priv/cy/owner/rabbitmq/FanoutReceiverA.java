package priv.cy.owner.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/6/20 17:51
 */
@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("FanoutReceiverA : " + msg);
    }
}
