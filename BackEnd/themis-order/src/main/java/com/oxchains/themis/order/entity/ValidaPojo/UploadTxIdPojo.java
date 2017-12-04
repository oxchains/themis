package com.oxchains.themis.order.entity.ValidaPojo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by xuqi on 2017/12/4.
 */
@Data
public class UploadTxIdPojo {
    @NotNull(message = "订单编号为空")
    @NotEmpty(message = "订单编号为空")
    private String id;
    @NotNull(message = "请输入交易凭据")
    @NotEmpty(message = "请输入交易凭据")
    @Size(max = 64,min = 64,message = "交易凭据格式错误")
    private String txId;
}
