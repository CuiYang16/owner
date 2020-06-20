package priv.cy.owner.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import priv.cy.owner.common.util.uuid.UUIDutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ：cuiyang
 * @description：生产者
 * @date ：Created in 2020/6/20 17:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class sendMsg {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired

    private AmqpTemplate ramqpTemplate;

    @Test
    public void sendDirectMessage() {
        String messageId = String.valueOf(UUIDutil.getUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

//将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend("helloQuery", messageId);
        }


    }

    @Test
    public void sendTopicMsg() {
        String messageId = String.valueOf(UUIDutil.getUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend("exchange", "topic.message", messageId + createTime);
        }
    }

    @Test
    public void sendFanoutMsg() {
        String messageId;
        String createTime;
        for (int i = 0; i < 100; i++) {
            messageId = String.valueOf(UUIDutil.getUUID());
            createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ramqpTemplate.convertAndSend("fanoutExchange", "", messageId + createTime);
        }
    }
}
