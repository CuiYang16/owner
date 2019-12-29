package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class ArticleTypeInfo implements Serializable {
    /**
    * 文章类别表
    */
    private String articleTypeId;

    /**
    * 类别名称
    */
    private String typeName;

    /**
    * 类别编码
    */
    private String typeCode;

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