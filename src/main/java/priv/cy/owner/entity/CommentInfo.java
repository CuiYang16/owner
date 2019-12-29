package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class CommentInfo implements Serializable {
    /**
    * 评论留言表
    */
    private String commentId;

    /**
    * 创建人FK
    */
    private String createByUser;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 上级评论
    */
    private String parentId;

    /**
    * 评论内容
    */
    private String commentContent;

    private static final long serialVersionUID = 1L;
}