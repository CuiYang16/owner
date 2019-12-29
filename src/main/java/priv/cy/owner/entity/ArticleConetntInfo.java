package priv.cy.owner.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class ArticleConetntInfo implements Serializable {
    /**
    * 文章内容详情
    */
    private String articleConetntId;

    private static final long serialVersionUID = 1L;
}