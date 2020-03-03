package priv.cy.owner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import priv.cy.owner.util.jwt.JwtProperties;

@EnableConfigurationProperties({JwtProperties.class})
@ServletComponentScan
@SpringBootApplication
public class OwnerApplication {

    public static void main(String[] args) {

        SpringApplication.run(OwnerApplication.class, args);
    }
}
