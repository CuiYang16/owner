package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class ArticleTagInfo implements Serializable {
    /**
    * 文章标签表
    */
    private String articleTagId;

    /**
    * 标签名称
    */
    private String tagName;

    /**
    * 标签编码
    */
    private String tagCode;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 创建人
    */
    private String createByUser;

    /**
    * 更新人
    */
    private String updateByUser;

    private static final long serialVersionUID = 1L;
}