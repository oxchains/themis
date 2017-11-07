package com.oxchains.themis.arbitrate.service;

import com.oxchains.themis.arbitrate.common.ParamType;
import com.oxchains.themis.arbitrate.common.Pojo;
import com.oxchains.themis.arbitrate.common.messageCommon.MessageReadStatus;
import com.oxchains.themis.arbitrate.common.messageCommon.MessageType;
import com.oxchains.themis.arbitrate.entity.OrderArbitrate;
import com.oxchains.themis.arbitrate.entity.Orders;
import com.oxchains.themis.arbitrate.entity.message.Message;
import com.oxchains.themis.arbitrate.entity.message.MessageText;
import com.oxchains.themis.arbitrate.repo.NoticeRepo;
import com.oxchains.themis.arbitrate.repo.OrderArbitrateRepo;
import com.oxchains.themis.arbitrate.repo.UserRepo;
import com.oxchains.themis.arbitrate.repo.message.MessageRepo;
import com.oxchains.themis.arbitrate.repo.message.MessageTextRepo;
import com.oxchains.themis.common.util.DateUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huohuo on 2017/11/7.
 * @author huohuo
 */
@Service
public class MessageService {
    @Resource
    private MessageRepo messageRepo;
    @Resource
    private MessageTextRepo messageTextRepo;
    @Resource
    private UserRepo userRepo;
    @Resource
    private NoticeRepo noticeRepo;
    @Resource
    private OrderArbitrateRepo orderArbitrateRepo;
    public static final Integer BUYER_SUCCESS = 1;
    public static final Integer SELLER_SUCCESS = 2;

    public void postOrderMessage(Orders orders){
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_CONFIRM.getStatus()){
            Long userId = noticeRepo.findOne(orders.getNoticeId()).getUserId();
            String username = userRepo.findOne(userId).getLoginname();
            String message = "恭喜你的公告已经被用户【 "+username+" 】拍下，交易数量 【 "+orders.getAmount()+" 】 订单号 【 "+orders.getId()+" 】请及时处理";
            MessageText messageText = new MessageText(0L,message, MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(userId,save.getId(), MessageReadStatus.UN_READ);
            messageRepo.save(message1);
        }
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_PAY.getStatus()){
            String username = userRepo.findOne(orders.getSellerId()).getLoginname();
            String message = "恭喜你的订单已经被商家 【 "+username+" 】确认,订单编号【 "+orders.getId()+" 】 请您及时处理";
            MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(orders.getBuyerId(),save.getId(),MessageReadStatus.UN_READ);
            messageRepo.save(message1);
        }
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_SEND.getStatus()){
            String username = userRepo.findOne(orders.getBuyerId()).getLoginname();
            String message = "恭喜你的订单已经被买家 【 "+username+" 】 确认付款，订单编号【 "+orders.getId()+" 】,请您及时处理";
            MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(orders.getSellerId(),save.getId(),MessageReadStatus.UN_READ);
            messageRepo.save(message1);
        }
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_RECIVE.getStatus()){
            String username = userRepo.findOne(orders.getSellerId()).getLoginname();
            String message = "恭喜你的订单已经被卖家 【 "+username+" 】 确认释放BTC，订单编号【 "+orders.getId()+" 】,请您及时处理";
            MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(orders.getBuyerId(),save.getId(),MessageReadStatus.UN_READ);
            messageRepo.save(message1);
        }
    }
    public void postCancelMessage(Orders orders,Long userId){
        String username = userRepo.findOne(userId).getLoginname();
        Long receiverId = null;
        String orderTyep = null;
        if(orders.getBuyerId() == userId.longValue()){
            receiverId = orders.getSellerId();
            orderTyep = "买家";
        }
        if(orders.getSellerId() == userId.longValue()){
            receiverId = orders.getBuyerId();
            orderTyep = "卖家";
        }
        String message = null;
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.CANCEL.getStatus()){
             message = "你的订单已经被"+orderTyep+"【 "+username+" 】取消，订单编号【 "+orders.getId()+" 】";
        }
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_REFUND.getStatus()){
             message = "你的订单已经被"+orderTyep+"【 "+username+" 】申请取消，订单编号【 "+orders.getId()+" 】请及时处理";
        }
        MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText save = messageTextRepo.save(messageText);
        Message message1 = new Message(receiverId,save.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message1);
    }
    public void postRefoundMessage(Orders orders){
        String username = userRepo.findOne(orders.getBuyerId()).getLoginname();
        String message =  "订单编号【 "+orders.getId()+" 】 卖家 【 "+username+" 】 已经确认收到退款，BTC将会在10-30分钟内到账,请查收";
        MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText save = messageTextRepo.save(messageText);
        Message message1 = new Message(orders.getSellerId(),save.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message1);
    }
    public void postArbitrateMessage(Orders orders,Long userId,Integer successId){
        String username = userRepo.findOne(userId).getLoginname();
        Long successUserId = null;
        Long faildUserId = null;
        if(successId == BUYER_SUCCESS.intValue()){
            successUserId = orders.getBuyerId();
            faildUserId = orders.getSellerId();
        }
        else{
            successUserId = orders.getSellerId();
            faildUserId = orders.getBuyerId();
        }
        String successMessage = "恭喜，仲裁者【"+username+"】在订单【"+orders.getId()+"】中判断您为胜利方，获得密匙碎片一枚";
        String faildMessage = "很遗憾，仲裁者【"+username+"】在订单【"+orders.getId()+"】中判断对方为胜利方,对方获得密匙碎片一枚";
        MessageText successMessageText = new MessageText(0L,successMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText successSave = messageTextRepo.save(successMessageText);
        Message message1 = new Message(successUserId,successSave.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message1);

        MessageText messageText2 = new MessageText(0L,faildMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText faildSave = messageTextRepo.save(messageText2);
        Message message2 = new Message(faildUserId,faildSave.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message2);
    }
    public void postArbitrateFinish(Orders orders){
        Long successId = null;
        Long falidId = null;
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.CANCEL.getStatus()){
            successId = orders.getSellerId();
            falidId = orders.getBuyerId();
        }
        if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.FINISH.getStatus()){
            successId = orders.getBuyerId();
            falidId = orders.getSellerId();
        }
        String successMessage = "恭喜:订单编号【"+orders.getId()+"】你是这次仲裁的胜利方,BTC将会在5-30分钟内到达你的账户,请注意查收";
        String faildMessage = "很遗憾,订单编号【"+orders.getId()+"】此次仲裁您未获胜，如有疑问请及时联系我们的客服人员";

        MessageText successMessageText = new MessageText(0L,successMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText successSave = messageTextRepo.save(successMessageText);
        Message message1 = new Message(successId,successSave.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message1);

        MessageText messageText2 = new MessageText(0L,faildMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText faildSave = messageTextRepo.save(messageText2);
        Message message2 = new Message(falidId,faildSave.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message2);
    }
    public void postCommentMessage(Orders orders, Pojo pojo){
        Long receiveId = null;
        String username = null;
        String orderType = null;
        if(orders.getBuyerId().longValue() == pojo.getUserId()){
            orderType = "买家";
            receiveId = orders.getSellerId();
            username = userRepo.findOne(orders.getBuyerId()).getLoginname();
        }
        if(orders.getSellerId().longValue() == pojo.getUserId()){
            orderType = "卖家";
            receiveId = orders.getBuyerId();
            username = userRepo.findOne(orders.getSellerId()).getLoginname();
        }
        String message = "订单编号【"+orders.getId()+"】"+orderType+" 【"+username+"】于"+DateUtil.getPresentDate()+"评价了你 .内容: "+pojo.getContent();
        MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText successSave = messageTextRepo.save(messageText);
        Message message1 = new Message(receiveId,successSave.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message1);
    }
    public void postEvidenceMessage(Orders orders,Long userId){
        String orderType = orders.getBuyerId() == userId.longValue()?"买家":"卖家";
        Long receiverId = orders.getBuyerId() == userId.longValue()?orders.getSellerId():orders.getBuyerId();
        String username = userRepo.findOne(userId).getLoginname();
        String message = "订单编号【"+orders.getId()+"】"+orderType+" 【"+username+"】于"+DateUtil.getPresentDate()+"对订单发起了仲裁,请及时处理";
        MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate());
        MessageText successSave = messageTextRepo.save(messageText);
        Message message1 = new Message(receiverId,successSave.getId(),MessageReadStatus.UN_READ);
        messageRepo.save(message1);

        List<OrderArbitrate> list = orderArbitrateRepo.findOrderArbitrateByOrderId(orders.getId());
        for (OrderArbitrate o : list){
            MessageText st = messageTextRepo.save(messageText);
            Message abritrateMessage = new Message(o.getUserId(),st.getId(),MessageReadStatus.UN_READ);
            messageRepo.save(abritrateMessage);
        }
    }

}
