package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class ArticleInfo implements Serializable {
    /**
    * 文章详情表
    */
    private String articleId;

    /**
    * 标题
    */
    private String articleTitle;

    /**
    * 版本号
    */
    private String version;

    /**
    * 文章类别FK
    */
    private String articleType;

    /**
    * 创建用户FK
    */
    private String createByUser;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新用户FK
    */
    private String updateByUser;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 是否置顶
    */
    private Boolean isTop;

    /**
    * 是否原创
    */
    private Boolean isOriginal;

    /**
    * 原始作者
    */
    private String originalAuthor;

    /**
    * 原始链接
    */
    private String originalLink;

    /**
    * 是否私密
    */
    private Boolean isPrivate;

    /**
    * 是否删除
    */
    private Boolean isDeleted;

    /**
    * 删除时间
    */
    private Date deleteTime;

    /**
    * 阅读数
    */
    private Integer pageview;

    /**
    * 是否开放评论
    */
    private Boolean isComment;

    /**
    * 评论FK
    */
    private String commentId;

    private static final long serialVersionUID = 1L;
}