package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.OrderArbitrate;
import com.oxchains.themis.repo.entity.Transaction;
import com.oxchains.themis.repo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
/**
 * @author huohuo
 * @desc Class that is invoked remotely
 */
@Service
public class RemoteCallService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RestTemplate restTemplate;
    //从用户中心 根据用户id获取用户信息
    public User getUserById(Long userId){
        try {
            String str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, String.class);
            if(null != str){
                RestResp restResp = JsonUtil.jsonToEntity(str, RestResp.class);
                if(null != restResp && restResp.status == 1){
                    return JsonUtil.objectToEntity(restResp.data,User.class);
                }
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e.getMessage(),e);
            throw  e;
        }
        return null;
    }

    //从用户中心获取仲裁者用户列表
    @HystrixCommand(fallbackMethod = "remoteError")
    public List<User> getArbitrateUser(){
        try {
            String str = restTemplate.getForObject(ThemisUserAddress.GET_ARBITRATE_USER, String.class);
            if(null != str){
                RestResp restResp = JsonUtil.jsonToEntity(str,RestResp.class);
                if(null != restResp && restResp.status== 1){
                    return JsonUtil.objectToList(restResp.data,User.class);
                }
            }
        } catch (RestClientException e) {
            LOG.error("get arbitrate user from themis-user faild : {}",e.getMessage(),e);
            throw  e;
        }
        return null;
    }
    //从用户中心获取协商地址
    public String getP2shAddressByOrderId(String id){
        try {
            String jsonObject = restTemplate.getForObject(ThemisUserAddress.GET_PTSHADDRESS+id, String.class);
            RestResp restResp = JsonUtil.jsonToEntity(jsonObject,RestResp.class);
            if(restResp != null){
                if(restResp.status == 1){
                    Transaction transaction = JsonUtil.objectToEntity(restResp.data, Transaction.class);
                    return transaction != null?transaction.getP2shAddress():null;
                }
            }
        } catch (Exception e) {
            LOG.error("get transaction from themis-user faild : {}",e.getMessage(),e);
            throw  e;
        }
        return null;
    }
    //从仲裁系统添加仲裁信息
    public Integer saveOrderAbritrate(List<OrderArbitrate> orderArbitrate){
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(orderArbitrate), this.getHttpHeader());
            String jsonObject = restTemplate.postForObject(ThemisUserAddress.SAVE_ARBITRATE, formEntity, String.class);
            if(jsonObject != null){
                RestResp restResp = JsonUtil.jsonToEntity(jsonObject,RestResp.class);
                if(restResp != null){
                    return restResp.status;
                }
            }
        } catch (RestClientException e) {
            LOG.error("save order arbitrate faild : {}",e.getMessage(),e);
            throw  e;
        }
        return -1;
    }
    //从公告系统 获取公告
    public Notice findNoticeById(Long id){
        Notice notice1 = null;
        try {
            String noticeStr = this.getNotice(id);
            if(noticeStr != null){
                RestResp restResp = JsonUtil.jsonToEntity(noticeStr, RestResp.class);
                if(null != restResp && restResp.status == 1){
                    notice1 = JsonUtil.objectToEntity(restResp.data, Notice.class);
                }
                return notice1;
            }
        } catch (RestClientException e) {
            LOG.error("get notice faild : {}",e.getMessage(),e);
        }
        return null;
    }
    @HystrixCommand(fallbackMethod = "remoteNoticeError")
    private String getNotice(Long id){
        return restTemplate.getForObject(ThemisUserAddress.GET_NOTICE + id, String.class);
    }
    private String remoteNoticeError(Long noticeId){
        return "error"+noticeId;
    }
    /**
     * 工具类方法 用来在用户系统获取一对随机的公私匙
     * */
    public AddressKeys getAddressKeys(){
        AddressKeys ak = null;
        try {
            String   r = restTemplate.getForObject(ThemisUserAddress.GET_ADDRESS_KEYS,String.class);
            if(r != null){
                RestResp restResp = JsonUtil.jsonToEntity(r,RestResp.class);
                if(restResp != null && restResp.status == 1){
                    ak = JsonUtil.objectToEntity(restResp.data,AddressKeys.class);
                }
                return ak;
            }
        } catch (RestClientException e) {
            LOG.error("get address key faild : {}",e.getMessage(),e);
        }
        return  null;
    };

    public HttpHeaders getHttpHeader(){
        HttpHeaders headers = null;
        try {
            headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        } catch (Exception e) {
            LOG.error("get http header faild : {}",e.getMessage(),e);
        }
        return  headers;
    }
}
