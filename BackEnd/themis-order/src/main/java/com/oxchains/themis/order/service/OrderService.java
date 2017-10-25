package com.oxchains.themis.order.service;

import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.order.entity.Notice;
import com.oxchains.themis.order.entity.Orders;
import com.oxchains.themis.order.repo.NoticeRepo;
import com.oxchains.themis.order.repo.OrderArbitrateRepo;
import com.oxchains.themis.order.repo.OrderRepo;
import com.oxchains.themis.order.repo.UserRepo;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by xuqi on 2017/10/23.
 */
@Service
public class OrderService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate;
    private OrderRepo orderRepo;
    private NoticeRepo noticeRepo;
    private UserRepo userRepo;
    private OrderArbitrateRepo orderArbitrateRepo;

    public OrderService(@Autowired OrderRepo orderRepo,@Autowired NoticeRepo noticeRepo,@Autowired UserRepo userRepo,@Autowired OrderArbitrateRepo orderArbitrateRepo,@Autowired RestTemplate restTemplate) {
        this.orderRepo = orderRepo;
        this.noticeRepo = noticeRepo;
        this.userRepo = userRepo;
        this.orderArbitrateRepo = orderArbitrateRepo;
        this.restTemplate = restTemplate;
    }
   /* public void souts(){
        String s = restTemplate.
    };*/
    /*
    * 查询所有订单
    * */
    public List<Orders> findOrders(){
        return IteratorUtils.toList(orderRepo.findAll().iterator());
    }
    /*
    * 下订单
    * */
    public Orders addOrders(Orders orders){
        Orders orders1 =null;
        try {
            orders.setCreateTime(DateUtil.getPresentDate());
            orders.setId(DateUtil.getOrderId());
            Notice notice = noticeRepo.findOne(orders.getNoticeId());
            orders.setOrderStatus(1L);
            orders.setArbitrate(0);
            orders1 =orderRepo.save(orders);

            String[] userIds = orders.getUserIds().split(",");
            //根据三个仲裁者的公私匙  生成一对公私匙
            // 把公匙 给用户中心 生成协商地址

        }catch (Exception e){
            LOG.debug("add orders faild :",e.getMessage());
        }
        return orders1;
    }
    public Orders findOrdersDetails(String id){
        Orders o = orderRepo.findOne(id);
        return o;
    }

    /*
   * 根据id查询自己已完成的的订单
   * */
    public List<Orders> findCompletedOrdersById(Long id){
        List<Orders>  list= null;
        try {
            list = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatus(id,id,5L);
            for (Orders o:list) {
                if(o.getBuyerId()==id){
                    o.setOrderType("购买");
                    o.setFriendUsername(userRepo.findOne(o.getSellerId()).getLoginname());
                }
                else{
                    o.setOrderType("出售");
                    o.setFriendUsername(userRepo.findOne(o.getBuyerId()).getLoginname());
                }
            }
        } catch (Exception e) {
            LOG.debug("query complete order faild :",e.getMessage());
        }
        return list;
    }
    /*
   * 根据id查询自己未完成的的订单
   * */
    public List<Orders> findNoCompletedOrdersById(Long id){
        List<Orders>  list= null;
        try {
            list = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatusIsNot(id,id,5L,6L);
            for (Orders o:list) {
                if(o.getBuyerId()==id){
                    o.setOrderType("购买");

                }
                else{
                    o.setOrderType("出售");
                }
            }
        } catch (Exception e) {
            LOG.debug("query noComplete order faild :",e.getMessage());
        }
        return list;
    }
    /*
    * 取消订单
    * */
    public Orders cancelOrders(String id,Long userId){

        Orders orders1 = null;
        try {
            Orders orders = orderRepo.findOne(id);
            if(orders.getOrderStatus()==1){
                //当订单状态为1 时 买家已拍下 但商家还未确认 可以直接取消订单 不采取任何操作
                orders.setOrderStatus(6L);
            }
            if(orders.getOrderStatus()==2){
                //当订单状态为2 买家还未付款 时调用接口让 卖家的BTB 从协议地址回到卖家账户
                orders.setOrderStatus(6L);
            }
            if(orders.getOrderStatus()==3){
                if(userId==orders.getBuyerId()){
                    //买家取消订单 证明收到退款 调用接口让BTC回到卖家地址 状态改为6
                    orders.setOrderStatus(6L);
                }
                if(userId==orders.getSellerId()){
                    //卖家取消订单 状态改为7 等待买家收到退款
                    orders.setOrderStatus(7L);
                }
                orders.setOrderStatus(7L);
            }
            orders1 = orderRepo.save(orders);
        } catch (Exception e) {
            LOG.debug("cancel orders faild :",e.getMessage());
        }
        return orders1;
    }
    /*
    * 发布公告人确认订单
    * */
    public Orders confirmOrders(String id){
        Orders o = null;
        try {
            o = orderRepo.findOne(id);
            if(o.getOrderStatus()==1){
                o.setOrderStatus(2L);
                o = orderRepo.save(o);
                if(o.getOrderStatus()==2){
                    //掉接口把卖家的BTB转移到协商地址
                }
            }
        } catch (Exception e) {
            LOG.debug("confirm order faild :",e.getMessage());
        }
        return o;
    }
    /*
    * 查询发布公告的人的所有待确认的订单
    * */
    public List<Orders> findNotConfirmOrders(Long id){
        List<Orders> list = null;
        try {
            //出售公告
            Notice seller = noticeRepo.findNoticeByUserIdAndTxStatusIsNotAndNoticeType(id,2,1L);
            //购买公告
            Notice buyer = noticeRepo.findNoticeByUserIdAndTxStatusIsNotAndNoticeType(id,2,2L);

            list = new ArrayList<>();
            if(seller != null){
                List<Orders> list1 = orderRepo.findOrdersByNoticeIdAndOrderStatus(seller.getId(),1L);
                for (Orders o:
                     list1) {
                    o.setOrderType("出售");
                }
                list.addAll(list1);
            }
            if(buyer != null){
                List<Orders> list1 = orderRepo.findOrdersByNoticeIdAndOrderStatus(buyer.getId(),1L);
                for (Orders o:
                        list1) {
                    o.setOrderType("购买");
                }
                list.addAll(list1);
            }
        } catch (Exception e) {
            LOG.debug("query no confirm orders faild :",e.getMessage());
        }
        return list;
    }

    public Orders confirmReceiveRefund(String id){
        try {
            Orders orders = orderRepo.findOne(id);
            if(orders.getOrderStatus()==7L){
                //掉一个接口让BTC从协商地址回到卖家
                orders.setOrderStatus(6L);
                orders = orderRepo.save(orders);
                return orders;
            }
        } catch (Exception e) {
            LOG.debug("confirm receive refund faild :",e.getMessage());
        }
        return null;
    }
    public Orders arbitrateOrder(String id){
        Orders orders1 = null;
        try {
            Orders orders = orderRepo.findOne(id);
            if(orders.getOrderStatus()==3||orders.getOrderStatus()==7){
                orders.setArbitrate(1);
                orders1 = orderRepo.save(orders);
            }
            return  orders1;
        } catch (Exception e) {
            LOG.debug("apply for arbitrate order faild :",e.getMessage());
        }
        return null;
    }
    /*
   * 根据仲裁者id查找哪些订单可以被自己仲裁的订单列表
   * */
    public List<Orders> findArbitrareOrderById(Long id){
        return null;
    }
}
