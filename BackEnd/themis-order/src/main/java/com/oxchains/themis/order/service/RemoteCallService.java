package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.OrderArbitrate;
import com.oxchains.themis.repo.entity.Transaction;
import com.oxchains.themis.repo.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author huohuo
 * @desc Class that is invoked remotely
 */
@Service
public class RemoteCallService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    HashOperations hashOperations;
    @Resource
    ListOperations listOperations;
    @Value("${themis.user.redisInfo.hk}")
    private String userHK;
    @Value("${themis.notice.redisInfo.hk}")
    private String noticeHk;
    @Value("${themis.arbitrate.redisInfo.hk}")
    private String arbitrateHK;
    @Value("${themis.txAddress.redisInfo.hk}")
    private String txAddressHK;
    private String arbitrateK = "1";
    @Resource
    private RestTemplate restTemplate;
    //从用户中心 根据用户id获取用户信息
    public User getUserById(Long userId){

        try {
            String userInfo = (String) hashOperations.get(userHK, userId.toString());
            if(StringUtils.isNotBlank(userInfo)){
                return JsonUtil.jsonToEntity(userInfo,User.class);
            }
            String str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, String.class);
            if(null != str){
                RestResp restResp = JsonUtil.jsonToEntity(str, RestResp.class);
                if(null != restResp && restResp.status == 1){
                    hashOperations.put(userHK,userId.toString(),JsonUtil.toJson(restResp.data));
                    return JsonUtil.objectToEntity(restResp.data,User.class);
                }
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e.getMessage(),e);
        }
        return null;
    }

    //从用户中心获取仲裁者用户列表
    public List<User> getArbitrateUser(){
        try {
            List<String> userList1 = listOperations.range(arbitrateHK, 0,3L);
            if(userList1!=null && userList1.size()>=3){
                List<User> ulist = new ArrayList<>(5);
                for (String s: userList1) {
                    ulist.add(JsonUtil.jsonToEntity(s,User.class));
                }
                return ulist;
            }
            String str = restTemplate.getForObject(ThemisUserAddress.GET_ARBITRATE_USER, String.class);
            if(null != str){
                RestResp restResp = JsonUtil.jsonToEntity(str,RestResp.class);
                if(null != restResp && restResp.status== 1){
                    List<User> userList = JsonUtil.objectToList(restResp.data, User.class);
                    for (User user : userList) {
                        listOperations.leftPush(arbitrateHK, JsonUtil.toJson(user));
                    }
                    return JsonUtil.objectToList(restResp.data,User.class);
                }
            }
        } catch (RestClientException e) {
            LOG.error("get arbitrate user from themis-user faild : {}",e.getMessage(),e);
        }
        return null;
    }
    //从用户中心获取协商地址
    public String getP2shAddressByOrderId(String id){
        try {
           String str  = (String) hashOperations.get(arbitrateHK, id);
           if(StringUtils.isNotBlank(str)){
               return str;
           }
            String jsonObject = restTemplate.getForObject(ThemisUserAddress.GET_PTSHADDRESS+id, String.class);
            RestResp restResp = JsonUtil.jsonToEntity(jsonObject,RestResp.class);
            if(restResp != null){
                if(restResp.status == 1){
                    Transaction transaction = JsonUtil.objectToEntity(restResp.data, Transaction.class);
                    if(transaction != null){
                        hashOperations.put(txAddressHK,id,transaction.getP2shAddress());
                        return transaction .getP2shAddress();
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get transaction from themis-user faild : {}",e.getMessage(),e);
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
        }
        return -1;
    }
    //从公告系统 获取公告
    public Notice findNoticeById(Long id){
        Notice notice1 = null;
        try {
            String noticeStrs = (String) hashOperations.get(noticeHk, id.toString());
            if(StringUtils.isNotBlank(noticeStrs)){
                return JsonUtil.jsonToEntity(noticeStrs,Notice.class);
            }
            String noticeStr = this.getNotice(id);
            if(noticeStr != null){
                RestResp restResp = JsonUtil.jsonToEntity(noticeStr, RestResp.class);
                if(null != restResp && restResp.status == 1){
                    notice1 = JsonUtil.objectToEntity(restResp.data, Notice.class);
                }
                hashOperations.put(noticeHk,id.toString(),JsonUtil.toJson(restResp.data));
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
