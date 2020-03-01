package priv.cy.owner.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ：cuiyang
 * @description：请求返回对象
 * @date ：Created in 2020/1/5 13:09
 */
@Data
@Builder
public class ResponseModel {
    // 响应码
    private int code;

    // 响应文本信息
    private String msg;

    // 响应对象信息
    private Object resObj;

    private int count;
    // 响应集合
    private List data;
}
