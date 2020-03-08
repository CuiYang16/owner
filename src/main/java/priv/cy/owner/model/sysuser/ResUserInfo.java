package priv.cy.owner.model.sysuser;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/3/8 10:57
 */
@Data
@Builder
public class ResUserInfo {

    private String userName;
    private String userAvatar;
    private String introduction;
    private List<String> roleList;
}
