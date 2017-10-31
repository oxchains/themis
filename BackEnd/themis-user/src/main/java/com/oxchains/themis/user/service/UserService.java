package com.oxchains.themis.user.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ConstantUtils;
import com.oxchains.themis.common.util.EncryptUtils;
import com.oxchains.themis.repo.dao.OrderDao;
import com.oxchains.themis.repo.dao.UserTxDetailDao;
import com.oxchains.themis.repo.entity.Order;
import com.oxchains.themis.repo.entity.UserTxDetail;
import com.oxchains.themis.user.auth.JwtService;
import com.oxchains.themis.user.dao.RoleDao;
import com.oxchains.themis.user.dao.UserDao;
import com.oxchains.themis.user.domain.Role;
import com.oxchains.themis.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        //注册比特币账户
        //String address = accountService.enrollAccount(user.getLoginname());

        return RestResp.success("操作成功");
    }

    public RestResp updateUser(User user) {
        User u = userDao.findByLoginname(user.getLoginname());
        u.setUsername(user.getUsername());
        user = userDao.save(u);
        if (user == null) {
            return RestResp.fail("操作失败");
        }
        return RestResp.success("操作成功");
    }

    public RestResp login(User user) {
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        Optional<User> optional = findUser(user);
        return optional.map(u -> {
            /*if (u.getLoginStatus() != 0 ){
                return RestResp.fail("用户已经登录");
            }*/
            String token = "Bearer " + jwtService.generate(user);
            Role role = roleDao.findById(u.getRoleId());
            UserTxDetail userTxDetail = userTxDetailDao.findByUserId(u.getId());
            List<Order> orders = orderDao.findByBuyerIdOrSellerId(u.getId(), u.getId());
            double buyAmount = 0d;
            double sellAmount = 0d;
            for (Order order : orders) {
                if (u.getId().equals(order.getBuyerId())) {
                    buyAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
                }
                if (u.getId().equals(order.getSellerId())) {
                    sellAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
                }
            }
            userTxDetail.setBuyAmount(buyAmount);
            userTxDetail.setSellAmount(sellAmount);

            logger.info("token = " + token);
            User userInfo = new User(u);
            userInfo.setRole(role);
            userInfo.setPassword(null);
            userInfo.setToken(token);

            userInfo.setUserTxDetail(userTxDetail);
            //u.setLoginStatus(1);
            //userDao.save(u);

            ConstantUtils.USER_TOKEN.put(u.getLoginname(), token);
            return RestResp.success("登录成功", userInfo); //new UserToken(u.getUsername(),token)
        }).orElse(RestResp.fail("登录失败"));
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

}
