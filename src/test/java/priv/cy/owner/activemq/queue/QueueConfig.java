package priv.cy.owner.activemq.queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/5/24 12:05
 */
@Configuration
public class QueueConfig {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("mytest.queue");
    }
}
