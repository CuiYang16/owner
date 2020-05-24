package priv.cy.owner.activemq.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/5/24 12:19
 */
@Component
public class TopicConsumer2 {

    @JmsListener(destination = "neo.topic")
    public void receiveTopic(String text) {
        System.out.println(2 + text + 2);
    }

}
