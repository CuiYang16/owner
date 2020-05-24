package priv.cy.owner.activemq.topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Topic;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/5/24 11:56
 */
@Configuration
public class TopicConfig {
    @Bean
    public Topic topic() {
        return new ActiveMQTopic("neo.topic");
    }
}
