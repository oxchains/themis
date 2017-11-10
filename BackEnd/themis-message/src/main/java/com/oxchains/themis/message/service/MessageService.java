package com.oxchains.themis.message.service;

import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.common.MessageConst;
import com.oxchains.themis.message.dao.MessageDao;
import com.oxchains.themis.message.dao.MessageTextDao;
import com.oxchains.themis.message.dao.OrderDao;
import com.oxchains.themis.message.dao.UserDao;
import com.oxchains.themis.message.domain.Message;
import com.oxchains.themis.message.domain.MessageText;
import com.oxchains.themis.message.domain.Orders;
import com.oxchains.themis.message.domain.User;
import com.oxchains.themis.message.rest.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoxuri
 * @create 2017-11-06 15:02
 **/
@Service
@Scope(value = "prototype")
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private static Integer COUNT = 0;
    private static Integer RESULT = 0;

    @Resource private MessageDao messageDao;
    @Resource private MessageTextDao messageTextDao;
    @Resource private OrderDao orderDao;
    @Resource private UserDao userDao;

    /**
     * 测试，快速添加数据
     * @param messageText
     * @return
     */
    @Deprecated
    public RestResp sendGlobalMessage(MessageText messageText){
        try {
            if (messageText.getMessage() == null && messageText.getMessageType() == null && messageText.getUserGroup() == null){
                return RestResp.fail("必填项不能为空");
            }

            // 保存messageText
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            messageText.setPostDate(currentTime);
            messageText.setSenderId(0L);
            messageText.setMessageType(MessageType.PUBLIC);
            messageText.setUserGroup(4L);
            MessageText mt = messageTextDao.save(messageText);

            return RestResp.success("操作成功", mt);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：发送系统消息异常", e.getMessage());
        }
        return RestResp.success("操作失败");
    }

    public RestResp queryGlobalMsg(Long userId, Integer pageNum, Integer pageSize){
        try {
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageType.GLOBAL, pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageType.GLOBAL);
                message.setMessageText(messageText);

                // 点击系统消息按钮，将所有返回数据的状态修改为已读，接受者id修改为自己的id
                message.setReadStatus(MessageReadStatus.READ);
                message.setReceiverId(userId);
                messageDao.save(message);

                mList.add(message);
            }

            MessageDTO messageDTO = new MessageDTO<>();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setTotalPage(page.getTotalPages());
            messageDTO.setPageNum(pageNum);
            messageDTO.setPageSize(pageSize);

            return RestResp.success("操作成功", messageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读私信信息失败", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryPrivateMsg(Long userId, Integer pageNum, Integer pageSize){
        try {
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageType.PRIVATE_LETTET, pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageType.PRIVATE_LETTET);

                // 点击私信按钮，将所有返回数据的状态修改为已读，接受者id修改为自己的id
                message.setReadStatus(MessageReadStatus.READ);
                message.setReceiverId(userId);

                // 获取订单相关信息
                String orderId = messageText.getOrderId();
                Orders orders = orderDao.findOne(orderId);
                Long buyerId = orders.getBuyerId();
                Long sellerId = orders.getSellerId();
                if (userId.equals(buyerId)){
                    messageText.setPartnerId(sellerId);
                    User user = userDao.findOne(sellerId);
                    messageText.setFriendUsername(user.getLoginname());
                }else if (userId.equals(sellerId)){
                    messageText.setPartnerId(buyerId);
                    User user = userDao.findOne(buyerId);
                    messageText.setFriendUsername(user.getLoginname());
                }else {
                    return RestResp.fail("站内信：获取订单信息失败");
                }

                message.setMessageText(messageText);
                messageDao.save(message);

                mList.add(message);
            }

            MessageDTO messageDTO = new MessageDTO<>();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setTotalPage(page.getTotalPages());
            messageDTO.setPageNum(pageNum);
            messageDTO.setPageSize(pageSize);

            return RestResp.success("操作成功", messageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取已读私信信息失败");
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryNoticeMsg(Long userId, Integer pageNum, Integer pageSize){
        try {
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
            // Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageType.PUBLIC, pageable);
             Page<Message> page = messageDao.findByReceiverIdAndMessageTypeOrReceiverId(userId, MessageType.PUBLIC, 0L, pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageType.PUBLIC);
                message.setMessageText(messageText);

                // 点击公告按钮，将所有返回数据的状态修改为已读，接受者id修改为自己的id
                message.setReadStatus(MessageReadStatus.READ);
                message.setReceiverId(userId);
                messageDao.save(message);
                mList.add(message);
            }

            MessageDTO messageDTO = new MessageDTO<>();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setTotalPage(page.getTotalPages());
            messageDTO.setPageNum(pageNum);
            messageDTO.setPageSize(pageSize);

            return RestResp.success("操作成功", messageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读公共信息失败");
        }
        return RestResp.fail("操作失败");
    }

    public RestResp unReadCount(Long userId, Integer tip){
        try {
            int count = 0;
            Integer result = invokeDb(userId, tip, count);
            return RestResp.success("操作成功", result);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读信息数量异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public Integer invokeDb(Long userId, Integer tip, Integer count) throws InterruptedException {

        // 私信条数
        List<Message> unPrivateMessageList = messageDao.findByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PRIVATE_LETTET);
        // 系统信息
        List<Message> unGlobalMessageList = messageDao.findByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.GLOBAL);

        // 公告信息
        // 1，先找到roleid，然后得到角色userGroup，然后根据msgType和up得到id
        User user = userDao.findOne(userId);
        Long userGroup = user.getRoleId();
        List<MessageText> messageTextList = messageTextDao.findByMessageTypeAndUserGroup(MessageType.PUBLIC, userGroup);
        Message message = new Message();
        for (MessageText mt : messageTextList) {
            Long mtId = mt.getId();
            List<Message> newUnNoticeMessageList = messageDao.findByMessageTextIdAndReceiverId(mtId, 0L);
            if (newUnNoticeMessageList.size() == 0){
                // 将信息保存到message表中
                message.setMessageTextId(mtId);
                message.setReadStatus(MessageReadStatus.UN_READ);
                message.setReceiverId(0L);
                message.setMessageType(MessageType.PUBLIC);
                messageDao.save(message);
            }
        }
        // 新的公告信息
        List<Message> newUnNoticeMessageList = messageDao.findByReceiverIdAndReadStatusAndMessageType(0L, MessageReadStatus.UN_READ, MessageType.PUBLIC);

        // 所有未读公告
        RESULT = unPrivateMessageList.size() + unGlobalMessageList.size() + newUnNoticeMessageList.size();

        List<Message> messageList = messageDao.findByReceiverIdAndReadStatus(userId, MessageReadStatus.UN_READ);
        if (RESULT != 0){
            if (tip == 1){
                COUNT = RESULT;
                return COUNT;
            } else {
                if (COUNT.equals(RESULT)){
                    Thread.sleep(2000);
                    // 前台请求15以上，返回的结果还是一样，就返回之前的数量，不走递归
                    if (count >= MessageConst.Constant.FIFTEEN.getValue()){
                        return COUNT;
                    }
                    return invokeDb(userId, tip, ++count);
                }else {
                    COUNT = RESULT;
                    return COUNT;
                }
            }
        }else {
            Thread.sleep(2000);
            if (count >= MessageConst.Constant.FIFTEEN.getValue()){
                return MessageConst.Constant.ZERO.getValue();
            }
            return invokeDb(userId, tip, ++count);
        }
    }
}
