package com.oxchains.themis.order.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * Created by huohuo on 2017/10/26.
 * @author huohuo
 */
@Entity
@Table(name = "user_tx_detail")
@Data
public class UserTxDetails {
    @Id
    private Long id;
    private Integer txNum;     //交易次数
    private Integer goodDesc;   //好评次数
    private Integer badDesc;    //差评次数
    private String firstBuyTime;  //第一次购买时间
    private Long userId;
    @Transient
    private String createTime;     //用户创建时间
    private Integer believeNum;    // 信任次数
    @Transient
    private Notice notice;      //公告详细信息
    @Transient
    private String emailVerify; //电子邮箱是否验证
    @Transient
    private String usernameVerify; //真实名称是否验证
    @Transient
    private String mobilePhoneVerify;//电话是否验证
    @Transient
    private String loginname;
    @Transient
    private String goodDegree;//好评度
    @Transient
    private Integer successCount;
    @Override
    public String toString() {
        return "UserTxDetail{" +
                "id=" + id +
                ", txNum=" + txNum +
                ", goodDesc=" + goodDesc +
                ", badDesc=" + badDesc +
                ", firstBuyTime='" + firstBuyTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", believeNum=" + believeNum +
                ", notice=" + notice +
                ", emailVerify='" + emailVerify + '\'' +
                ", usernameVerify='" + usernameVerify + '\'' +
                ", mobilePhoneVerify='" + mobilePhoneVerify + '\'' +
                ", goodDegree='" + goodDegree + '\'' +
                '}';
    }
}
