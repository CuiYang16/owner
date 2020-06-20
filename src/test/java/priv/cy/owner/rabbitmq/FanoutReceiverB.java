package priv.cy.owner.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/6/20 17:53
 */
@Component
@RabbitListener(queues = "fanout.B")
public class FanoutReceiverB {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("FanoutReceiverB : " + msg);
    }
}