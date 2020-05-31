package priv.cy.owner.model;

import lombok.Getter;

/**
 * @author ：cuiyang
 * @description：结果状态类
 * @date ：Created in 2020/5/3 15:09
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(true, 20000, "成功"),

    USER_LOGIN_SUCCESS(true, 20000, "登录成功"),
    USER_LOGOUT_SUCCESS(true, 20000, "登出成功"),
    //java自带错误
    UNKNOWN_ERROR(false, 50001, "未知错误"),
    PARAM_ERROR(false, 50002, "参数错误"),
    NULL_POINT(false, 50003, "空指针异常"),
    HTTP_CLIENT_ERROR(false, 50004, "网络异常"),

    //用户登录错误
    USER_NEED_AUTHORITIES(false, 51001, "用户登录失败"),
    USER_NO_ACCESS(false, 51002, "用户无权访问"),
    USER_NO_LOGIN(false, 51003, "用户未登录"),
    UNKNOWN_USER(false, 51004, "未知用户"),
    LOCK_USER(false, 51005, "用户已锁定"),
    UNAUTHENTIC(false, 51006, "无权访问，当前是匿名访问，请先登录！"),
    UNAUTHORIZED(false, 51007, "无权访问，当前帐号权限不足！"),
    RANDOM_IMAGE_ERROR(false, 51008, "验证码输入错误，请确认后重试！"),
    CAPTCHA_GET_ERROR(false, 51009, "验证码刷新错误，请确认后重试！"),
    LOGINUSER_INFO_ERROR(false, 51010, "用户登录信息错误，请确认后重试！"),
    LOGOUT_FAIL(false, 51011, "用户登出失败，请重试！"),

    //数据校验错误
    XSS_ERROR(false, 52001, "输入违法信息"),

    //用户token异常
    USER_TOKEN_EXPIRED(false, 53001, "用户登录信息过期，请重新登录"),

    USER_TOKEN_INCORRECT(false, 53002, "用户登录信息异常，请确认后重试"),

    //数据操作异常
    USER_CREATE_ERROR(false, 540001, "创建用户失败，请刷新重试！"),

    NOTFOUND(false, 40401, "服务器未找到资源");

    // 响应是否成功
    private Boolean success;
    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    ResultCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
