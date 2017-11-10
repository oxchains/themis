package com.oxchains.themis.user.service;

import com.oxchains.themis.common.constant.Status;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.ParamType;
import com.oxchains.themis.common.param.RequestBody;
import com.oxchains.themis.common.util.ConstantUtils;
import com.oxchains.themis.common.util.EncryptUtils;
import com.oxchains.themis.repo.dao.*;
import com.oxchains.themis.repo.entity.*;
import com.oxchains.themis.user.auth.JwtService;
import com.oxchains.themis.user.domain.UserTrust;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

//    @Resource
//    AccountService accountService;

    public RestResp addUser(User user) {
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        Optional<User> optional = findUser(user);
        if (optional.isPresent()) {
            return RestResp.fail("操作失败");
        }
        if(null == user.getCreateTime()){
            user.setCreateTime(new Date());
        }
        if(null == user.getRoleId()){
            user.setRoleId(4L);
        }
        user = userDao.save(user);
        if (user == null) {
            return RestResp.fail("操作失败");
        }

        UserTxDetail userTxDetail = new UserTxDetail(true);
        userTxDetail.setUserId(user.getId());

        userTxDetailDao.save(userTxDetail);

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
        Optional<User> optional = findUser(user);
        return optional.map(u -> {
            if(u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())){
                return RestResp.fail("用户已经登录");
            }
            String token = "Bearer " + jwtService.generate(u);
            Role role = roleDao.findById(u.getRoleId());
            UserTxDetail userTxDetail = findUserTxDetailByUserId(u.getId());

            logger.info("token = " + token);
            User userInfo = new User(u);
            userInfo.setRole(role);
            userInfo.setPassword(null);
            userInfo.setToken(token);

            userInfo.setUserTxDetail(userTxDetail);

            u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
            userDao.save(u);
            ConstantUtils.USER_TOKEN.put(u.getLoginname(), token);

            //new UserToken(u.getUsername(),token)
            return RestResp.success("登录成功", userInfo);
        }).orElse(RestResp.fail("登录失败"));
    }
    public RestResp logout(User user){
        User u = userDao.findByLoginname(user.getLoginname());
        if(null != u && u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())){
            u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
            userDao.save(u);
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
        return optional;
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

}
