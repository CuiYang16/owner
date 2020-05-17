package priv.cy.owner.util.redis;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/5/17 13:58
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private static final String PROPERTIES_FILE = "application.properties";
    
    private Integer database;
    private String host;
    private Integer port;
    private Boolean ssl;
    private String password;
    private Long connTimeout;
    private Integer maxActive;
    private Integer maxIdle;
    private Integer minIdle;
    private Integer maxWait;
}
