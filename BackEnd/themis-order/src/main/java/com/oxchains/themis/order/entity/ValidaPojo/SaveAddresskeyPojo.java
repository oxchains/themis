package com.oxchains.themis.order.entity.ValidaPojo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by xuqi on 2017/12/4.
 */
@Data
public class SaveAddresskeyPojo {
    private String orderId;
    @NotNull(message = "请输入公钥")
    @NotEmpty(message = "请输入公钥")
    @Size(max = 66,min = 66,message = "公钥格式错误")
    private String sellerPubAuth;
    @NotEmpty(message = "请输入私钥")
    @NotNull(message = "请输入私钥")
    @Size(max = 52,min = 52,message = "私钥格式错误")
    private String sellerPriAuth;
}
