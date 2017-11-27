package com.oxchains.themis.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ccl
 * @time 2017-11-09 17:36
 * @name AccessFilter
 * @desc:
 */

@Component
public class AccessFilter extends ZuulFilter{

    private static final Logger logger = Logger.getLogger(AccessFilter.class.getCanonicalName());

    @Override
    public String filterType() {
        //前置过滤器
        return "pre";
    }

    @Override
    public int filterOrder() {
        //优先级,数字越大,优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器
        return true;
    }

    @Override
    public Object run() {
        RequestContext rcx = RequestContext.getCurrentContext();
        HttpServletRequest request = rcx.getRequest();
        String url = request.getRequestURI();

        //String token = request.getParameter("Authorization");
        String token = request.getHeader("Authorization");
        logger.log(Level.FINE,"Authorization token: {}",token);
        if(null == token){
            if("/themis-user/user/login".equals(url)){
            }else {
                //过滤该请求，不往下级服务去转发请求，到此结束
                rcx.setSendZuulResponse(false);
                rcx.setResponseStatusCode(401);
                rcx.setResponseBody("{}");
                return null;

            }
        }
        //如果有token，则进行路由转发
        logger.info("Authorized,continue...");
        //这里return的值没有意义，zuul框架没有使用该返回值
        return null;
    }
}
