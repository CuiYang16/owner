package priv.cy.owner.activemq.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;


/**
 * @author ：cuiyang
 * @description：生产者
 * @date ：Created in 2020/5/24 11:38
 */
@Component
public class Producer {
    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private JmsMessagingTemplate jmsTemplate;
    @Autowired
    private Queue queue;

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(final String message) {
        this.jmsTemplate.convertAndSend(this.queue, message);
    }
}



