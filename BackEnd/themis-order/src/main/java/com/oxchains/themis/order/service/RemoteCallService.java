package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.OrdersKeyAmount;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.order.entity.ValidaPojo.UploadTxIdPojo;
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
import java.util.Optional;

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
    @HystrixCommand(fallbackMethod = "getUserError")
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
            LOG.error("get user by id from themis-user faild : {}", e.getMessage(), e);
            return null;
        }
        return null;
    }
    public User getUserError(Long userId){
        return null;
    }
    //从用户中心获取仲裁者用户列表
    @HystrixCommand(fallbackMethod = "getArbitrateUserError")
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
            return null;
        }
        return null;
    }
    public List<User> getArbitrateUserError(){
        return null;
    }
    //从用户中心获取协商地址
    @HystrixCommand(fallbackMethod = "getP2shAddressByOrderIdError")
    public Transaction getTransactionById(String id){
        try {
           String str  = (String) hashOperations.get(txAddressHK, id);
           if(StringUtils.isNotBlank(str)){
               return JsonUtil.jsonToEntity(str,Transaction.class);
           }
            String jsonObject = restTemplate.getForObject(ThemisUserAddress.GET_PTSHADDRESS+id, String.class);
           if(jsonObject != null){
               RestResp restResp = JsonUtil.jsonToEntity(jsonObject,RestResp.class);
               if(restResp != null){
                   if(restResp.status == 1){
                       Transaction transaction = JsonUtil.objectToEntity(restResp.data, Transaction.class);
                       if(transaction != null){
                           hashOperations.put(txAddressHK,id,JsonUtil.toJson(transaction));
                           return transaction;
                       }
                   }
               }
           }
        } catch (Exception e) {
            LOG.error("get transaction from themis-user faild : {}",e.getMessage(),e);
            return null;
        }
        return null;
    }
    public Transaction getP2shAddressByOrderIdError(String s){
        return null;
    }
    //从仲裁系统添加仲裁信息
    @HystrixCommand(fallbackMethod = "saveOrderAbritrateError")
    public Integer saveOrderAbritrate(List<OrderArbitrate> orderArbitrate){
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(orderArbitrate), this.getHttpHeader());
            String jsonObject = restTemplate.postForObject(ThemisUserAddress.SAVE_ARBITRATE, formEntity, String.class);
            if(jsonObject != null){
                if(jsonObject != null){
                    RestResp restResp = JsonUtil.jsonToEntity(jsonObject,RestResp.class);
                    if(restResp != null){
                        return restResp.status;
                    }
                }
            }
        } catch (RestClientException e) {
            LOG.error("save order arbitrate faild : {}",e.getMessage(),e);
            return -1;
        }
        return -1;
    }
    public Integer saveOrderAbritrateError(List<OrderArbitrate> orderArbitrate){
        return null;
    }
    //从公告系统 获取公告
    @HystrixCommand(fallbackMethod = "remoteNoticeError")
    public Notice findNoticeById(Long id){
        Notice notice1 = null;
        try {
            String noticeStrs = (String) hashOperations.get(noticeHk, id.toString());
            if(StringUtils.isNotBlank(noticeStrs)){
                return JsonUtil.jsonToEntity(noticeStrs,Notice.class);
            }
            String noticeStr = restTemplate.getForObject(ThemisUserAddress.GET_NOTICE + id, String.class);
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
            return null;
        }
        return null;
    }
    private Notice remoteNoticeError(Long noticeId){
        return null;
    }
    /**
     * 工具类方法 用来在用户系统获取一对随机的公私匙
     * */
    @HystrixCommand(fallbackMethod = "getAddressKeysError")
    public AddressKeys getAddressKeys(){
        AddressKeys ak = null;
        try {
            String  r = restTemplate.getForObject(ThemisUserAddress.GET_ADDRESS_KEYS,String.class);
            if(r != null){
                RestResp restResp = JsonUtil.jsonToEntity(r,RestResp.class);
                if(restResp != null && restResp.status == 1){
                    ak = JsonUtil.objectToEntity(restResp.data,AddressKeys.class);
                }
                return ak;
            }
        } catch (RestClientException e) {
            LOG.error("get address key faild : {}",e.getMessage(),e);
            return null;
        }
        return  null;
    };
    public AddressKeys getAddressKeysError(){
        return null;
    }

    public HttpHeaders getHttpHeader(){
        HttpHeaders headers = null;
        try {
            headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        } catch (Exception e) {
            LOG.error("get http header faild : {}",e);
        }
        return  headers;
    }
    @HystrixCommand(fallbackMethod = "createCenterAddressError")
    public JSONObject createCenterAddress(OrdersKeyAmount ordersKeyAmount){
        JSONObject jsonObject = null;
        try {
            if(ordersKeyAmount != null){
                HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
                jsonObject = restTemplate.postForObject(ThemisUserAddress.CREATE_CENTET_ADDRESS,formEntity,JSONObject.class);
            }
        } catch (RestClientException e) {
            LOG.error("create center address faild：{}",e.getMessage(),e);
            return null;
        }
        return jsonObject;
    }
    public JSONObject createCenterAddressError(OrdersKeyAmount ordersKeyAmount){
        return null;
    }
    @HystrixCommand(fallbackMethod = "uploadTxIdError")
    public JSONObject uploadTxId(OrdersKeyAmount ordersKeyAmount, String id){
        JSONObject jsonObject = null;
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
            jsonObject = restTemplate.postForObject(ThemisUserAddress.CHECK_BTC + id, formEntity, JSONObject.class);
        } catch (RestClientException e) {
            LOG.error("upload txid faild：{}",e);
        }
        return jsonObject;
    }
    public JSONObject uploadTxIdError(OrdersKeyAmount ordersKeyAmount, String id){
        return null;
    }
    @HystrixCommand(fallbackMethod = "moveBTCError" )
    public JSONObject moveBTC(OrdersKeyAmount ordersKeyAmount){
        JSONObject jsonObject = null;
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
            jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
        } catch (RestClientException e) {
            LOG.error("releaseBTC faild : {}",e.getMessage(),e);
        }
        return jsonObject;
    }
    public void uploadTxInform(UploadTxIdPojo pojo){
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(pojo), this.getHttpHeader());
            restTemplate.postForObject(ThemisUserAddress.TX_INFORM,formEntity,JSONObject.class);
        } catch (RestClientException e) {
            LOG.error("uploadTx inform fail",e);
        }
    }
    public JSONObject moveBTCError(OrdersKeyAmount ordersKeyAmount){
        return null;
    }
}
