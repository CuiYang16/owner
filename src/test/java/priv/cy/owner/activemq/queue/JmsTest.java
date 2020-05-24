package priv.cy.owner.activemq.queue;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/5/24 11:37
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JmsTest {
    @Autowired
    private Producer producer;

    @Test
    public void contextLoads() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            this.producer.sendMessage("myname is chhliu!!!" + i);
        }
    }
}
