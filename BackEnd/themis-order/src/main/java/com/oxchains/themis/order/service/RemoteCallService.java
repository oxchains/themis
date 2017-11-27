package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.AddressKeys;
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
        User user = null;
        try {
            JSONObject str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, JSONObject.class);
            if(null != str){
                Integer status = (Integer) str.get("status");
                if(status == 1){
                    Object data = str.get("data");
                    String userStr = JsonUtil.toJson(data);
                    user = JsonUtil.jsonToEntity(userStr, User.class);
                }
                return user;
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e.getMessage(),e);
            throw  e;
        }
        return null;
    }
    //从用户中心获取仲裁者用户列表
    public List<User> getArbitrateUser(){
        List<User> list = null;
        try {
            JSONObject str = restTemplate.getForObject(ThemisUserAddress.GET_ARBITRATE_USER, JSONObject.class);
            if(null != str){
                Integer status = (Integer) str.get("status");
                if(status == 1){
                    Object data = str.get("data");
                    String strs = JsonUtil.toJson(data);
                    list = JsonUtil.jsonToList(strs, User.class);
                }
                return list;
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
            System.out.println("获取协商地址 "+ThemisUserAddress.GET_PTSHADDRESS+id);
            JSONObject jsonObject = restTemplate.getForObject(ThemisUserAddress.GET_PTSHADDRESS+id, JSONObject.class);
            System.out.println("获取协商地址 "+ThemisUserAddress.GET_PTSHADDRESS+id+"------数据"+JsonUtil.toJson(jsonObject));
            if(jsonObject != null){
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    Object data = jsonObject.get("data");
                    String str = JsonUtil.toJson(data);
                    Transaction transaction = JsonUtil.jsonToEntity(str, Transaction.class);
                    return transaction !=null ? transaction.getP2shAddress():null;
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
            JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.SAVE_ARBITRATE, formEntity, JSONObject.class);
            if(jsonObject != null){
                return (Integer) jsonObject.get("status");
            }
        } catch (RestClientException e) {
            LOG.error("save order arbitrate faild : {}",e.getMessage(),e);
            throw  e;
        }
        return 0;
    }
    //从公告系统 获取公告
    public Notice findNoticeById(Long id){
        try {
            JSONObject forObject = restTemplate.getForObject(ThemisUserAddress.GET_NOTICE + id, JSONObject.class);
            Integer status = (Integer) forObject.get("status");
            if(status == 1){
                Object data = forObject.get("data");
                String str = JsonUtil.toJson(data);
                Notice notice = JsonUtil.jsonToEntity(str, Notice.class);
                return notice;
            }
        } catch (RestClientException e) {
            LOG.error("get notice faild : {}",e.getMessage(),e);
            throw  e;
        }
        return null;
    }
    /**
     * 工具类方法 用来在用户系统获取一对随机的公私匙
     * */
    public AddressKeys getAddressKeys(){
        AddressKeys ak = null;
        try {
            String   r = restTemplate.getForObject(ThemisUserAddress.GET_ADDRESS_KEYS,String.class);
            JSONObject result= JSON.parseObject(r);
            int status= (int) result.get("status");
            if(status==1){
                Object o=result.get("data");
                ak = (AddressKeys) JsonUtil.fromJson(JsonUtil.toJson(o),AddressKeys.class);
            }
        } catch (RestClientException e) {
            LOG.error("get address key faild : {}",e.getMessage(),e);
        }
        return  ak;
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
