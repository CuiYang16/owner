package priv.cy.owner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import priv.cy.owner.common.jwt.JwtProperties;


@EnableConfigurationProperties({JwtProperties.class})
@ServletComponentScan
@SpringBootApplication
public class OwnerApplication {

    public static void main(String[] args) {

        SpringApplication.run(OwnerApplication.class, args);
    }

    //解决RequestContextHolder.getRequestAttributes()).getRequest()空指针
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
