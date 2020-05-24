package priv.cy.owner.activemq.topic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/5/24 12:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {
    @Autowired
    private TopicProducer producer;

    @Test
    public void contextLoads() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            this.producer.sendTopic("Topic!!!" + i);
        }
    }
}

