package priv.cy.owner.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：cuiyang
 * @description：消费者，生产后立刻消费
 * @date ：Created in 2020/6/20 17:29
 */
@Component
@RabbitListener(queues = "helloQuery")
public class HelloReceiverDirect {

    @RabbitHandler
    public void acceptDirectMsg(String hello) {
        System.out.println("Receiver1 : " + hello);
    }
}
