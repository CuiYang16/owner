package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class ArticlePictureInfo implements Serializable {
    /**
    * 文章图片表
    */
    private String articlePictureId;

    /**
    * 图片url
    */
    private String pictureUrl;

    /**
    * 图片名称
    */
    private String pictrueName;

    /**
    * 宽
    */
    private Double pictureWidth;

    /**
    * 高
    */
    private Double pictureHeight;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 创建用户FK
    */
    private String createByUser;

    private static final long serialVersionUID = 1L;
}