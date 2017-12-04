package com.oxchains.themis.common.exception;

import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.*;

/**
 * @author ccl
 * @time 2017-12-04 11:29
 * @name RestExceptionHandler
 * @desc: result 异常统一处理
 */
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class RestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus
    public RestResp runtimeExceptionHandler(Exception e){
        return RestResp.success("服务器繁忙,请稍后再试!");
    }
}
