package com.oxchains.themis.message.service;

import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.common.MessageConst;
import com.oxchains.themis.message.dao.MessageDao;
import com.oxchains.themis.message.dao.MessageTextDao;
import com.oxchains.themis.message.rest.dto.MessageDTO;
import com.oxchains.themis.repo.dao.OrderDao;
import com.oxchains.themis.repo.dao.UserDao;
import com.oxchains.themis.repo.entity.Message;
import com.oxchains.themis.repo.entity.MessageText;
import com.oxchains.themis.repo.entity.Order;
import com.oxchains.themis.repo.entity.User;
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
public class MessageService {

    private final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private final Map<Long, Integer> countMap = new HashMap<>();

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
    public RestResp sendNoticeMessage(MessageText messageText){
        try {
            if (messageText.getMessage() == null){
                return RestResp.fail("请填写内容");
            }
            if (messageText.getUserGroup() == null){
                // 默认将公告发送给所有人
                messageText.setUserGroup(4L);
            }

            // 保存messageText
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            messageText.setPostDate(currentTime);
            messageText.setSenderId(0L);
            messageText.setMessageType(MessageType.PUBLIC);
            messageText.setUserGroup(messageText.getUserGroup());
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
            List<Message> mList = new ArrayList<>();
            for (Message message : page.getContent()) {
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
                Order orders = orderDao.findById(orderId);
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
            // 获取自己所在用户组
            User user = userDao.findOne(userId);
            Long userGroup = user.getRoleId();
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageType.PUBLIC, pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageType.PUBLIC);
                messageText.setUserGroup(userGroup);
                messageTextDao.save(messageText);
                message.setMessageText(messageText);
                message.setReadStatus(MessageReadStatus.READ);
                message.setReceiverId(userId);
                messageDao.save(message);

                mList.add(message);
            }
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setPageSize(pageSize);
            messageDTO.setPageNum(pageNum);
            messageDTO.setTotalPage(page.getTotalPages());
            return RestResp.success("操作成功", messageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取公告信息异常", e.getMessage());
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
        // 用户登录后，将所在用户组未读公告信息添加到message表中
        // 1，先找到roleid，然后得到角色userGroup，然后根据msgType和up得到id
        User user = userDao.findOne(userId);
        if (user != null) {
            Long userGroup = user.getRoleId();
            List<MessageText> messageTextList = messageTextDao.findByMessageTypeAndUserGroup(MessageType.PUBLIC, userGroup);

            if (messageTextList.size() != 0) {
                // 所有公告
                Set<Long> set = new HashSet<>();
                for (MessageText mt : messageTextList) {
                    set.add(mt.getId());
                }

                // 移除已读公告的mtId
                List<Message> allPublic = messageDao.findByReceiverIdAndMessageType(userId, MessageType.PUBLIC);
                for (Message m : allPublic) {
                    set.remove(m.getMessageTextId());
                }

                // 添加剩余没有的公告
                Iterator<Long> it = set.iterator();
                Message message = new Message();
                while (it.hasNext()) {
                    message.setMessageTextId(it.next().longValue());
                    message.setReadStatus(MessageReadStatus.UN_READ);
                    message.setReceiverId(userId);
                    message.setMessageType(MessageType.PUBLIC);
                    messageDao.save(message);
                }
            }
        }

        // 所有未读信息
        Integer unReadSize = messageDao.countByReceiverIdAndReadStatus(userId, MessageReadStatus.UN_READ);
        int cacheCount = countMap.getOrDefault(userId, -1);
        if (tip == 1) {
            // 第一次请求获取未读消息
            countMap.put(userId, unReadSize);
            return unReadSize;
        }
        // 旧值和新值一样，则不返回结果
        if (cacheCount == unReadSize) {
            Thread.sleep(2000);
            // 前台请求15以上，返回的结果还是一样，就返回之前的数量，不走递归
            if (count >= MessageConst.Constant.FIFTEEN.getValue()) {
                return unReadSize;
            }
            return invokeDb(userId, tip, ++count);
        }
        // 旧值和新值，则更新缓存，返回结果
        countMap.put(userId, unReadSize);
        return unReadSize;
    }
}
