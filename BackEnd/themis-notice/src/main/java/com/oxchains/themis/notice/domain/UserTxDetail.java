package com.oxchains.themis.notice.domain;

import com.oxchains.themis.notice.domain.Notice;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 交易次数，信任人数，好评度，信任度
 * Created by huohuo on 2017/10/26.
 */
@Entity
@Table(name = "user_tx_detail")
@Data
public class UserTxDetail {

    @Id
    private Long id;
    private Integer txNum;     //交易次数
    private Integer goodDesc;   //好评次数
    private Integer badDesc;    //差评次数
    private String firstBuyTime;  //第一次购买时间
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
    private String username;
    @Transient
    private String goodDegree;//好评度

}
