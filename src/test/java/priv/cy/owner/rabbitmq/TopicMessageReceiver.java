package priv.cy.owner.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：cuiyang
 * @description：topic消费者
 * @date ：Created in 2020/6/20 17:45
 */
@Component
@RabbitListener(queues = "topic.message")
public class TopicMessageReceiver {
    @RabbitHandler

    public void process(String msg) {

        System.out.println("topicMessageReceiver : " + msg);

    }
}
