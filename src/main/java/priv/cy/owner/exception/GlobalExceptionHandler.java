package priv.cy.owner.exception;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import priv.cy.owner.model.ResultCodeEnum;
import priv.cy.owner.model.ResultInfo;

/**
 * @author ：cuiyang
 * @description：统一异常处理
 * @date ：Created in 2020/5/3 15:15
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * -------- 通用异常处理方法 --------
     **/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultInfo error(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ResultInfo.error();
    }

    /**
     * -------- 指定异常处理方法 --------
     **/
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResultInfo error(NullPointerException e) {
        e.printStackTrace();
        return ResultInfo.setResult(ResultCodeEnum.NULL_POINT);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ResultInfo error(IndexOutOfBoundsException e) {
        e.printStackTrace();
        return ResultInfo.setResult(ResultCodeEnum.HTTP_CLIENT_ERROR);
    }

    @ExceptionHandler(RRException.class)
    @ResponseBody
    public ResultInfo error(RRException e) {
        e.printStackTrace();
        return ResultInfo.setResult(ResultCodeEnum.XSS_ERROR);
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseBody
    public ResultInfo error(UnknownAccountException e) {
        e.printStackTrace();
        return ResultInfo.setResult(ResultCodeEnum.UNKNOWN_USER);
    }

    @ExceptionHandler(LockedAccountException.class)
    @ResponseBody
    public ResultInfo error(LockedAccountException e) {
        e.printStackTrace();
        return ResultInfo.setResult(ResultCodeEnum.LOCK_USER);
    }


    /**
     * -------- 自定义定异常处理方法 --------
     **/
    @ExceptionHandler(CMSException.class)
    @ResponseBody
    public ResultInfo error(CMSException e) {
        e.printStackTrace();
        return ResultInfo.error().message(e.getMessage());
    }
}