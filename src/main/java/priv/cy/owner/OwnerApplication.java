package priv.cy.owner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class OwnerApplication {

    public static void main(String[] args) {

        SpringApplication.run(OwnerApplication.class, args);
    }
}
