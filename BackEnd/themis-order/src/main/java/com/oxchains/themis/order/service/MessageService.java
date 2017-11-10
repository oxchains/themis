package com.oxchains.themis.order.service;
import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.order.common.MessageCopywrit;
import com.oxchains.themis.order.entity.Orders;
import com.oxchains.themis.order.repo.NoticeRepo;
import com.oxchains.themis.order.repo.OrderArbitrateRepo;
import com.oxchains.themis.order.repo.UserRepo;
import com.oxchains.themis.repo.dao.MessageRepo;
import com.oxchains.themis.repo.dao.MessageTextRepo;
import com.oxchains.themis.repo.entity.Message;
import com.oxchains.themis.repo.entity.MessageText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
/**
 * Created by huohuo on 2017/11/7.
 * @author huohuo
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class MessageService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
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
    //添加订单的站内信
    public void postAddOrderMessage(Orders orders,Long userId,Long noticeUserId){
        try {
            String username = userRepo.findOne(userId).getLoginname();
            String noticeMessage = MessageFormat.format(MessageCopywrit.ADD_ORDERS_NOTICE,username,orders.getId());
            String placeMessage = MessageFormat.format(MessageCopywrit.ADD_ORDERS_PLACE,orders.getId());
            //发布公告人的通知
            MessageText messageText = new MessageText(0L,noticeMessage, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(noticeUserId,save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message1);
            //下订单人的通知
            MessageText messageText1 = new MessageText(0L,placeMessage, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save1 = messageTextRepo.save(messageText);
            Message message2 = new Message(userId,save1.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message2);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post add orders faild : {}",e.getMessage(),e);
        }
    }
    //卖家上传公私钥的站内信
    public void postAddAddressKey(Orders orders){
        try {
            String message = MessageFormat.format(MessageCopywrit.SAVE_ADDRESS_KEY,orders.getId());
            MessageText messageText = new MessageText(0L,message, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message1);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post add public key private key faild : {}",e.getMessage(),e);
        }
    }
    //卖家上传交易id的站内信
    public void postUploadTxId(Orders orders){
        try {
            String message = MessageFormat.format(MessageCopywrit.UPLOAD_TXID,orders.getId());
            MessageText messageText = new MessageText(0L,message, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message1 = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message1);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post upload tx id faild : {}",e.getMessage(),e);
        }
    }
    //订单确认的站内信
    public void postConfirmOrder(Orders orders){
        try {
            String message = MessageFormat.format(MessageCopywrit.CONFIRM_ORDER,orders.getId());

            MessageText messageText = new MessageText(0L,message, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);

            Message message1 = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message1);

            Message message2 = new Message(orders.getBuyerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message2);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post confirm orders faild : {}",e.getMessage(),e);
        }
    }
    /*
    *卖家确认付款的站内信
    * */
    public void postConfirmSendMoney(Orders orders){
        try {
            String messageContent = MessageFormat.format(MessageCopywrit.CONFIRM_SEND_MONEY,orders.getId());

            MessageText messageText = new MessageText(0L,messageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);

            Message message = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);

            Message message1 = new Message(orders.getBuyerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post buyer confirm pay faild : {}",e.getMessage(),e);
        }

    }
    /*
    * 卖家释放BTC的站内信
    * */
    public void postReleaseBtc(Orders orders){
        try {
            String messageContent = MessageFormat.format(MessageCopywrit.REALEAS_BTC,orders.getId());

            MessageText messageText = new MessageText(0L,messageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);

            Message message = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);

            Message message1 = new Message(orders.getBuyerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post seller release BTC faild : {}",e.getMessage(),e);
        }

    }
    /*
    * 确认收货
    * */
    public void postConfirmReceive(Orders orders){
        try {
            String messageContent = MessageFormat.format(MessageCopywrit.CONFIRM_RECEIVE_BTV,orders.getId());

            MessageText messageText = new MessageText(0L,messageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);

            Message message = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);

            Message message1 = new Message(orders.getBuyerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post confirm receiver faild : {}",e.getMessage(),e);
        }

    }
    /*
    * 取消订单
    * */

    public void postCancelOrder(Orders orders,Long userId){
        try {
            String byMessageContent = MessageFormat.format(MessageCopywrit.BY_CANCEL_ORDERS,orders.getId());
            MessageText byMessageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText bySave = messageTextRepo.save(byMessageText);
            Message byMessage = new Message(orders.getSellerId().longValue()==userId?orders.getBuyerId():orders.getSellerId(),bySave.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(byMessage);


            String messageContent = MessageFormat.format(MessageCopywrit.CANCEL_ORDERS,orders.getId());
            MessageText messageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message = new Message(userId,save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post cancel orders faild : {}",e.getMessage(),e);
        }

    }
    /*
    * 取消订单后等待买家收到退款
    * */
    public void postRefund(Orders orders,Long userId){
        try {
            String byMessageContent = MessageFormat.format(MessageCopywrit.CANCEL_REFUND,orders.getId());
            MessageText byMessageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText bySave = messageTextRepo.save(byMessageText);
            Message byMessage = new Message(orders.getSellerId().longValue()==userId?orders.getBuyerId():orders.getSellerId(),bySave.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(byMessage);

            String messageContent = MessageFormat.format(MessageCopywrit.CANCEL_WAIT_REFUND,orders.getId());
            MessageText messageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message = new Message(userId,save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post cancle order to wait refund  faild : {}",e.getMessage(),e);
        }
    }

    //收到退款的通知
    public void postRefoundMoney(Orders orders,Long userId){
        try {
            String byMessageContent  = MessageFormat.format(MessageCopywrit.BY_REFUND_MONEY,orders.getId());
            MessageText byMessageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText bySave = messageTextRepo.save(byMessageText);
            Message byMessage = new Message(orders.getSellerId().longValue()==userId?orders.getBuyerId():orders.getSellerId(),bySave.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(byMessage);


            String messageContent = MessageFormat.format(MessageCopywrit.REFUND_MONEY,orders.getId());
            MessageText messageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message = new Message(userId,save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post refund money faild : {}",e.getMessage(),e);
        }
    }
    //提交评价的通知
    public void postCommentMessage(Orders orders, Long userId){
        try {
            String byMessageContent  = MessageFormat.format(MessageCopywrit.BY_COMMENT_ORDERS,orders.getId());
            MessageText byMessageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText bySave = messageTextRepo.save(byMessageText);
            Message byMessage = new Message(orders.getSellerId().longValue()==userId?orders.getBuyerId():orders.getSellerId(),bySave.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(byMessage);

            String messageContent = MessageFormat.format(MessageCopywrit.COMMENT_ORDERS,orders.getId());
            MessageText messageText = new MessageText(0L,byMessageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message = new Message(userId,save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post comment messaage faild : {}",e.getMessage(),e);
        }
    }
    /*
    * 双方评价完成 订单完成
    * */
    public void postFinishOrders(Orders orders){
        try {
            String messageContent = MessageFormat.format(MessageCopywrit.FINISH_ORDERS,orders.getId());
            MessageText messageText = new MessageText(0L,messageContent, MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);

            Message messageBuyer = new Message(orders.getBuyerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(messageBuyer);
            Message messageSeller = new Message(orders.getSellerId(),save.getId(), MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(messageSeller);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post finsh orders faild : {}",e.getMessage(),e);
        }
    }
}
