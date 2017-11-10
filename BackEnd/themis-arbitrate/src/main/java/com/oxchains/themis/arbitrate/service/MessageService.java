package com.oxchains.themis.arbitrate.service;

import com.oxchains.themis.arbitrate.common.ParamType;
import com.oxchains.themis.arbitrate.entity.OrderArbitrate;
import com.oxchains.themis.arbitrate.entity.Orders;
import com.oxchains.themis.arbitrate.repo.NoticeRepo;
import com.oxchains.themis.arbitrate.repo.OrderArbitrateRepo;
import com.oxchains.themis.arbitrate.repo.UserRepo;
import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.repo.dao.MessageRepo;
import com.oxchains.themis.repo.dao.MessageTextRepo;
import com.oxchains.themis.repo.entity.Message;
import com.oxchains.themis.repo.entity.MessageText;
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
        MessageText successMessageText = new MessageText(0L,successMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate(),orders.getId());
        MessageText successSave = messageTextRepo.save(successMessageText);
        Message message1 = new Message(successUserId,successSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
        messageRepo.save(message1);

        MessageText messageText2 = new MessageText(0L,faildMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate(),orders.getId());
        MessageText faildSave = messageTextRepo.save(messageText2);
        Message message2 = new Message(faildUserId,faildSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
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

        MessageText successMessageText = new MessageText(0L,successMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate(),orders.getId());
        MessageText successSave = messageTextRepo.save(successMessageText);
        Message message1 = new Message(successId,successSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
        messageRepo.save(message1);

        MessageText messageText2 = new MessageText(0L,faildMessage,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate(),orders.getId());
        MessageText faildSave = messageTextRepo.save(messageText2);
        Message message2 = new Message(falidId,faildSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
        messageRepo.save(message2);
    }
    public void postEvidenceMessage(Orders orders,Long userId){
        String orderType = orders.getBuyerId() == userId.longValue()?"买家":"卖家";
        Long receiverId = orders.getBuyerId() == userId.longValue()?orders.getSellerId():orders.getBuyerId();
        String username = userRepo.findOne(userId).getLoginname();
        String message = "订单编号【"+orders.getId()+"】"+orderType+" 【"+username+"】于"+DateUtil.getPresentDate()+"对订单发起了仲裁,请及时处理";
        MessageText messageText = new MessageText(0L,message,MessageType.PRIVATE_LETTET,0L,DateUtil.getPresentDate(),orders.getId());
        MessageText successSave = messageTextRepo.save(messageText);
        Message message1 = new Message(receiverId,successSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
        messageRepo.save(message1);

        List<OrderArbitrate> list = orderArbitrateRepo.findByOrderId(orders.getId());
        for (OrderArbitrate o : list){
            MessageText st = messageTextRepo.save(messageText);
            Message abritrateMessage = new Message(o.getUserId(),st.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(abritrateMessage);
        }
    }

}
