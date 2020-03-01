package priv.cy.owner.web.controller.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/3/1 16:34
 */
@Controller
public class ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping("/403")
    public void errorInfo() {
        logger.debug("403");
    }
}
