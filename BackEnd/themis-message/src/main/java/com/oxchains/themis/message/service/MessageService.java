package com.oxchains.themis.message.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.common.MessageConst;
import com.oxchains.themis.message.dao.MessageDao;
import com.oxchains.themis.message.dao.MessageTextDao;
import com.oxchains.themis.message.dao.OrderDao;
import com.oxchains.themis.message.domain.Message;
import com.oxchains.themis.message.domain.MessageText;
import com.oxchains.themis.message.domain.Orders;
import com.oxchains.themis.message.rest.dto.MessageDTO;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author luoxuri
 * @create 2017-11-06 15:02
 **/
@Service
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Resource
    private MessageDao messageDao;
    @Resource
    private MessageTextDao messageTextDao;
    @Resource
    private OrderDao orderDao;

    public RestResp sendGlobalMessage(MessageText messageText){
        try {
            if (messageText.getMessage() == null && messageText.getMessageType() == null && messageText.getUserGroup() == null){
                return RestResp.fail("必填项不能为空");
            }
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            messageText.setPostDate(currentTime);
            MessageText mt = messageTextDao.save(messageText);
            Message message = new Message();
            message.setMessageTextId(mt.getId());
            message.setReadStatus(MessageConst.ReadStatus.ONE.getStatus());
            messageDao.save(message);
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
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageConst.MessageType.ONE.getStatus(), pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageConst.MessageType.ONE.getStatus());
                message.setMessageText(messageText);
                mList.add(message);
            }
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            MessageDTO messageDTO = new MessageDTO<>();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setTotalPage(page.getTotalPages());
            messageDTO.setPageNum(pageNum);
            messageDTO.setPageSize(pageSize);
            messageDTO.setTime(currentTime);
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
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageConst.MessageType.TWO.getStatus(), pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageConst.MessageType.TWO.getStatus());
                message.setMessageText(messageText);
                mList.add(message);
            }
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            MessageDTO messageDTO = new MessageDTO<>();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setTotalPage(page.getTotalPages());
            messageDTO.setPageNum(pageNum);
            messageDTO.setPageSize(pageSize);
            messageDTO.setTime(currentTime);
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
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, MessageConst.MessageType.THREE.getStatus(), pageable);
            Iterator<Message> it = page.iterator();
            List<Message> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), MessageConst.MessageType.THREE.getStatus());
                message.setMessageText(messageText);
                mList.add(message);
            }
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            MessageDTO messageDTO = new MessageDTO<>();
            messageDTO.setPageList(mList);
            messageDTO.setRowCount(page.getTotalElements());
            messageDTO.setTotalPage(page.getTotalPages());
            messageDTO.setPageNum(pageNum);
            messageDTO.setPageSize(pageSize);
            messageDTO.setTime(currentTime);
            return RestResp.success("操作成功", messageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读公共信息失败");
        }
        return RestResp.fail("操作失败");
    }

    public RestResp unReadCount(Long userId){
        try {
            List<Message> messageList = messageDao.findByReceiverIdAndReadStatus(userId, 1);
            if (messageList.size() != 0){
               return RestResp.success("操作成功", messageList.size());
            } else {
                Thread.sleep(2000);
                return unReadCount(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读信息数量异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }
}
