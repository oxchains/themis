package com.oxchains.themis.order.entity.vo;
import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.UserTxDetail;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/26.
 * @author huohuo
 */
@Data
public class UserTxDetails extends UserTxDetail{
    private String imageName;
    private String createTime;     //用户创建时间
    private Notice notice;      //公告详细信息
    private String emailVerify; //电子邮箱是否验证
    private String usernameVerify; //真实名称是否验证
    private String mobilePhoneVerify;//电话是否验证
    private String loginname;
    private String goodDegree;//好评度
    public UserTxDetails(UserTxDetail userTxDetail){
        this.setFirstBuyTime(userTxDetail.getFirstBuyTime());
        this.setTxNum(userTxDetail.getTxNum());
        this.setBadDesc(userTxDetail.getBadDesc());
        this.setGoodDesc(userTxDetail.getGoodDesc());
        this.setId(userTxDetail.getId());
        this.setUserId(userTxDetail.getUserId());
        this.setBelieveNum(userTxDetail.getBelieveNum());
        this.setSuccessCount(userTxDetail.getSuccessCount());
    }

}
