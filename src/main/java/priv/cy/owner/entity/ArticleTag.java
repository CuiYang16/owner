package priv.cy.owner.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class ArticleTag implements Serializable {
    /**
    * 文章主键FK
    */
    private String articleId;

    /**
    * 标签主键FK
    */
    private String tagId;

    /**
    * 唯一主键
    */
    private String articleTagId;

    private static final long serialVersionUID = 1L;
}