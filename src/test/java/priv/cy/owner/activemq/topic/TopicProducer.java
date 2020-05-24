package priv.cy.owner.activemq.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;

/**
 * @author ：cuiyang
 * @description：生产者
 * @date ：Created in 2020/5/24 11:57
 */
@Component
public class TopicProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;

    public void sendTopic(String msg) {
        this.jmsMessagingTemplate.convertAndSend(this.topic, msg);
    }
}
