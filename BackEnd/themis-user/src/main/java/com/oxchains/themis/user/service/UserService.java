package com.oxchains.themis.user.service;

import com.oxchains.themis.common.auth.JwtService;
import com.oxchains.themis.common.constant.Status;
import com.oxchains.themis.common.constant.UserConstants;
import com.oxchains.themis.common.mail.Email;
import com.oxchains.themis.common.mail.MailService;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.ParamType;
import com.oxchains.themis.common.param.RequestBody;
import com.oxchains.themis.common.util.ConstantUtils;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.EncryptUtils;
import com.oxchains.themis.repo.dao.*;
import com.oxchains.themis.repo.entity.*;
import com.oxchains.themis.user.domain.UserRelationInfo;
import com.oxchains.themis.user.domain.UserTrust;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author ccl
 * @time 2017-10-12 17:24
 * @name UserService
 * @desc:
 */

//@Transactional
@Service
public class UserService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserDao userDao;

    @Resource
    JwtService jwtService;

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserTxDetailDao userTxDetailDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    MailService mailService;

    @Resource
    private RedisTemplate redisTemplate;

    private String token;

//    @Resource
//    AccountService accountService;

    public RestResp addUser(User user) {
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        Optional<User> optional = findUser(user);
        if (optional.isPresent()) {
            User u = optional.get();
            if(null != user.getLoginname() && user.getLoginname().equals(u.getLoginname())){
                return RestResp.fail("用户名已经存在");
            }
            if(null != user.getMobilephone() && user.getMobilephone().equals(u.getMobilephone())){
                return RestResp.fail("该手机号已被注册");
            }
            if(null != user.getEmail() && user.getEmail().equals(u.getEmail())){
                return RestResp.fail("该邮箱已被注册");
            }
            return RestResp.fail("注册用户已经存在");
        }
        if(null == user.getCreateTime()){
            user.setCreateTime(DateUtil.getPresentDate());
        }
        if(null == user.getRoleId()){
            user.setRoleId(4L);
        }
        if (null == user.getLoginStatus()){
            user.setLoginStatus(0);
        }
        user = userDao.save(user);
        if (user == null) {
            return RestResp.fail("操作失败");
        }

        UserTxDetail userTxDetail = new UserTxDetail(true);
        userTxDetail.setUserId(user.getId());

        try{
            userTxDetailDao.save(userTxDetail);
        }catch (Exception e){
            logger.error(e.getMessage());
            userDao.delete(user.getId());
            return RestResp.fail("操作失败");
        }

        return RestResp.success("操作成功");
    }

    public RestResp updateUser(User user) {
        User u = userDao.findByLoginname(user.getLoginname());
        if(u==null){
            return RestResp.fail("操作失败");
        }
        u.setUsername(user.getUsername());
        user = userDao.save(u);
        if (user == null) {
            return RestResp.fail("操作失败");
        }
        return RestResp.success("操作成功");
    }
    public RestResp updateUser(User user, ParamType.UpdateUserInfoType uuit) {
        User u = userDao.findByLoginname(user.getLoginname());
        switch (uuit){
            case INFO:
                u.setImage(user.getImage());
                u.setDescription(user.getDescription());
                break;
            case PWD:
                if(EncryptUtils.encodeSHA256(user.getPassword()).equals(u.getPassword())){
                    u.setPassword(EncryptUtils.encodeSHA256(user.getNewPassword()));
                }else {
                    return RestResp.fail("输入的旧密码错误");
                }
                break;
            case FPWD:
                u.setFpassword(EncryptUtils.encodeSHA256(user.getFpassword()));
                break;
            case EMAIL:
                u.setEmail(user.getEmail());
                break;
            case PHONE:
                u.setMobilephone(user.getMobilephone());
                break;
                default:
        }
        reSaveRedis(u, token);
        return save(u);
    }
    private RestResp save(User user){
        try {
            userDao.save(user);
            return RestResp.success("操作成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail("操作失败");
        }
    }

    public RestResp login(User user) {
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        try{
            Optional<User> optional = findUser(user);
            return optional.map(u -> {
                if(u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())){
                    return RestResp.fail("用户已经登录");
                }
                String originToken = jwtService.generate(u);
                token = "Bearer " + originToken;

                Role role = roleDao.findById(u.getRoleId());
                UserTxDetail userTxDetail = findUserTxDetailByUserId(u.getId());

                logger.info("token = " + token);
                User userInfo = new User(u);
                userInfo.setRole(role);
                userInfo.setPassword(null);
                userInfo.setToken(token);

                userInfo.setUserTxDetail(userTxDetail);

                u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
                User save = userDao.save(u);

                // redis 存储
                boolean keyExist = redisTemplate.hasKey(save.getId().toString());
                if (!keyExist){
                    logger.info("保存 TOKEN 到 REDIS");
                    saveRedis(save ,originToken);
                }

                ConstantUtils.USER_TOKEN.put(u.getLoginname(), token);

                //new UserToken(u.getUsername(),token)
                return RestResp.success("登录成功", userInfo);
            }).orElse(RestResp.fail("登录账号或密码错误"));
        }catch (Exception e){
            return RestResp.fail("用户信息异常");
        }
    }

    @Deprecated
    public String _queryRedisValue(String key){
        ValueOperations operations = redisTemplate.opsForValue();
        String value = (String) operations.get(key);
        System.out.println("UserService：redis中的token = " + value);
        return value;
    }

    private void saveRedis(User save, String originToken){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(save.getId().toString(), originToken, 7, TimeUnit.DAYS);
    }

    private void reSaveRedis(User save, String originToken){
        logger.info("重新保存 TOKEN 到 REDIS ");
        redisTemplate.delete(save.getId().toString());
        saveRedis(save, originToken);
    }

    public RestResp logout(User user){
        User u = userDao.findByLoginname(user.getLoginname());
        if(null != u && u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())){
            u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
            userDao.save(u);
            redisTemplate.delete(u.getId().toString());
            return RestResp.success("退出成功");
        }else {
            return RestResp.fail("退出失败");
        }
    }

    public Optional<User> findUser(User user) {
        Optional<User> optional = null;
        if (null != user.getLoginname()) {
            optional = userDao.findByLoginnameAndPassword(user.getLoginname(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        if (null != user.getEmail()) {
            optional = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        if (null != user.getMobilephone()) {
            optional = userDao.findByMobilephoneAndPassword(user.getMobilephone(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        return Optional.empty();
    }

    public RestResp findUsers() {
        return RestResp.success(newArrayList(userDao.findAll()));
    }

    /**
     * 信任/屏蔽
     * @return
     */
    public RestResp trustUsers(RequestBody body, Status.TrustStatus status){
        com.oxchains.themis.common.model.Page<UserTrust> res =new com.oxchains.themis.common.model.Page(body.getPageNo(),body.getPageSize());
        Pageable pager=new PageRequest((body.getPageNo()-1)*body.getPageSize(),body.getPageSize(),new Sort(Sort.Direction.ASC,"toUserId"));
        Page<UserRelation> page = null;
        if(status.equals(Status.TrustStatus.SHIELD)){
            page = userRelationDao.findByFromUserIdAndStatus(body.getUserId(), Status.TrustStatus.SHIELD.getStatus(),pager);
        }else {
            page = userRelationDao.findByFromUserIdAndStatus(body.getUserId(), Status.TrustStatus.TRUST.getStatus(),pager);
        }

        res.setTotalCount((int)page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        List<UserTrust> list = new ArrayList<>();
        Iterator<UserRelation> it = page.iterator();
        UserTrust trustu = null;
        while (it.hasNext()){
            UserRelation relation = it.next();
            trustu = new UserTrust();
            User u = userDao.findOne(relation.getToUserId());
            int txToNum = orderDao.countByBuyerIdOrSellerId(body.getUserId(),relation.getToUserId()) + orderDao.countByBuyerIdOrSellerId(relation.getToUserId(),body.getUserId());
            trustu.setTxToNum(txToNum);
            trustu.setFromUserId(relation.getFromUserId());
            trustu.setFromUserName(u.getLoginname());
            trustu.setToUserId(relation.getToUserId());
            trustu.setToUserName(u.getLoginname());

            UserTxDetail detail = findUserTxDetailByUserId(relation.getToUserId());

            trustu.setTxNum(detail.getTxNum());
            trustu.setGoodDesc(detail.getGoodDesc());
            trustu.setBadDesc(detail.getBadDesc());
            trustu.setFirstBuyTime(detail.getFirstBuyTime());
            trustu.setBelieveNum(detail.getBelieveNum());
            trustu.setBuyAmount(detail.getBuyAmount());
            trustu.setSellAmount(detail.getSellAmount());


            list.add(trustu);
        }
        res.setResult(list);

        return RestResp.success(res);
    }

    /**
     * 被信任
     * @return
     */
    public RestResp trustedUsers(RequestBody body){
        com.oxchains.themis.common.model.Page<UserTrust> res =new com.oxchains.themis.common.model.Page(body.getPageNo(),body.getPageSize());
        Pageable pager=new PageRequest((body.getPageNo()-1)*body.getPageSize(),body.getPageSize(),new Sort(Sort.Direction.ASC,"fromUserId"));
        Page<UserRelation> page = userRelationDao.findByToUserIdAndStatus(body.getUserId(),Status.TrustStatus.TRUST.getStatus(),pager);
        res.setTotalCount((int)page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        List<UserTrust> list = new ArrayList<>();
        Iterator<UserRelation> it = page.iterator();
        UserTrust trustu = null;
        while (it.hasNext()){
            UserRelation relation = it.next();
            trustu = new UserTrust();
            User u = userDao.findOne(relation.getFromUserId());
            int txToNum = orderDao.countByBuyerIdOrSellerId(body.getUserId(),relation.getFromUserId()) + orderDao.countByBuyerIdOrSellerId(relation.getFromUserId(),body.getUserId());
            trustu.setTxToNum(txToNum);
            trustu.setFromUserId(relation.getFromUserId());
            trustu.setFromUserName(u.getLoginname());
            trustu.setToUserId(relation.getToUserId());
            trustu.setToUserName(relation.getToUserName());

            UserTxDetail detail = findUserTxDetailByUserId(relation.getFromUserId());

            trustu.setTxNum(detail.getTxNum());
            trustu.setGoodDesc(detail.getGoodDesc());
            trustu.setBadDesc(detail.getBadDesc());
            trustu.setFirstBuyTime(detail.getFirstBuyTime());
            trustu.setBelieveNum(detail.getBelieveNum());
            trustu.setBuyAmount(detail.getBuyAmount());
            trustu.setSellAmount(detail.getSellAmount());
            list.add(trustu);
        }
        res.setResult(list);

        return RestResp.success(res);
    }

    private UserTxDetail findUserTxDetailByUserId(Long userId){
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(userId);
        if(null == userTxDetail){
            return null;
        }
        List<Order> orders = orderDao.findByBuyerIdOrSellerId(userId, userId);
        double buyAmount = 0d;
        double sellAmount = 0d;
        for (Order order : orders) {
            if (userId.equals(order.getBuyerId())) {
                buyAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
            }
            if (userId.equals(order.getSellerId())) {
                sellAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
            }
        }
        userTxDetail.setBuyAmount(buyAmount);
        userTxDetail.setSellAmount(sellAmount);

        return userTxDetail;
    }

    public RestResp relation(UserRelation relation){
        UserRelation ur = userRelationDao.findByFromUserIdAndToUserId(relation.getFromUserId(),relation.getToUserId());
        try{
            if(null != ur){
                ur.setStatus(relation.getStatus());
                userRelationDao.save(ur);
            }else {
                userRelationDao.save(relation);
            }
            return RestResp.success("操作成功");
        }catch (Exception e){
            return RestResp.fail("操作失败");
        }
    }

    public RestResp getRelation(UserRelation relation){
        UserRelationInfo userRelationInfo = null;
        User user = userDao.findOne(relation.getToUserId());
        if(null == user){
            return RestResp.fail("无法查询相关用户信息");
        }
        userRelationInfo = new UserRelationInfo(user);
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(relation.getToUserId());
        userRelationInfo.setUserTxDetail(userTxDetail);
        UserRelation ur = userRelationDao.findByFromUserIdAndToUserId(relation.getFromUserId(),relation.getToUserId());
        if(null == ur){
            ur = new UserRelation();
            ur.setFromUserId(relation.getFromUserId());
            ur.setToUserId(relation.getToUserId());
            ur.setStatus(Status.TrustStatus.NONE.getStatus());
        }
        userRelationInfo.setUserRelation(ur);
        return RestResp.success(userRelationInfo);
    }

    public RestResp forgetPwd(RequestBody body){
        User user = userDao.findByLoginname(body.getLoginname());
        user.setPassword(EncryptUtils.encodeSHA256("123456"));
        userDao.save(user);
        try{
            String[] to = {body.getEmail()};
            mailService.send(new Email(to,"密码重置","密码重置为:123456,请尽快登录修改!"));
            return RestResp.success("操作成功");
        }catch (Exception e){
            logger.error("操作失败: {}",e);
            return RestResp.fail("操作失败");
        }
    }

    public RestResp getArbitrations(){
        List<User> list = userDao.findByRoleId(UserConstants.UserRole.ARBITRATION.getRoleId());
        if(null != list && list.size()>0 ){
            for(int i= 0; i < list.size(); i++){
                list.get(i).setPassword(null);
            }
        }
        return RestResp.success(list);
    }

    public RestResp getUser(Long id){
        User user = userDao.findOne(id);
        if(user != null){
            user.setPassword(null);
        }
        return RestResp.success(user);
    }

}
