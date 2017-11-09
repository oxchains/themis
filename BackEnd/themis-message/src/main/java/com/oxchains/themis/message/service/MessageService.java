package com.oxchains.themis.message.service;

import ch.qos.logback.core.joran.conditional.ThenAction;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.common.MessageConst;
import com.oxchains.themis.message.dao.MessageDao;
import com.oxchains.themis.message.dao.MessageTextDao;
import com.oxchains.themis.message.domain.Message;
import com.oxchains.themis.message.domain.MessageText;
import com.oxchains.themis.message.rest.dto.MessageDTO;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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

    public RestResp queryPrivateMsgNoRead(Long userId){
        try {
            MessageDTO messageDTO = new MessageDTO();
            List<Message> messageList = messageDao.findByReceiverIdAndReadStatus(userId, MessageConst.ReadStatus.ONE.getStatus());
            if (messageList.size() != MessageConst.ListSize.ZERO.getValue()){
                for (Message msg : messageList) {
                    messageDTO.setMessageType(MessageConst.MessageType.ONE.getStatus());
                    messageDTO.setReadStatus(msg.getReadStatus());
                    messageDTO.setMessageSize(messageList.size());
                }
                return RestResp.success("操作成功", messageDTO);
            }else {
                Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                return queryPrivateMsgNoRead(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读私信信息失败", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryPrivateMsgYesRead(Long userId){
        try {
            MessageDTO messageDTO = new MessageDTO();
            List<Message> messageList = messageDao.findByReceiverIdAndReadStatus(userId, MessageConst.ReadStatus.TWO.getStatus());
            if (messageList.size() != MessageConst.ListSize.ZERO.getValue()){
                List<MessageText> mtList = new ArrayList<>();
                for (Message msg : messageList) {
                    List<MessageText> messageTextList = messageTextDao.findByIdAndMessageType(msg.getMessageTextId(), MessageConst.MessageType.ONE.getStatus());
                    mtList.addAll(messageTextList);
                }
                if (mtList.size() != MessageConst.ListSize.ZERO.getValue()){
                    messageDTO.setMessageType(MessageConst.MessageType.ONE.getStatus());
                    messageDTO.setReadStatus(MessageConst.ReadStatus.TWO.getStatus());
                    messageDTO.setMessageSize(mtList.size());
                    messageDTO.setList(mtList);
                    return RestResp.success("操作成功", messageDTO);
                }else {
                    Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                    return queryPrivateMsgYesRead(userId);
                }
            }else {
                Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                return queryPrivateMsgYesRead(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取已读私信信息失败");
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryPublicMsgNoRead(Long userId, Integer userGroup){
        try {
            MessageDTO messageDTO = new MessageDTO();
            List<MessageText> messageTextList = messageTextDao.findByMessageTypeAndUserGroup(MessageConst.MessageType.TWO.getStatus(), userGroup);
            if (messageTextList.size() != MessageConst.ListSize.ZERO.getValue()){
                List<Message> mList = new ArrayList<>();
                List<MessageText> mtList = new ArrayList<>();
                for (MessageText mt : messageTextList) {
                    List<Message> messageList = messageDao.findByMessageTextIdAndReceiverId(mt.getId(), userId);
                    List<MessageText> resultMessageTextList =  messageTextDao.findByIdAndMessageType(mt.getId(), MessageConst.MessageType.TWO.getStatus());
                    mtList.addAll(resultMessageTextList);
                    mList.addAll(messageList);
                }
                if (mList.size() == MessageConst.ListSize.ZERO.getValue()){
                    messageDTO.setList(mtList);
                    messageDTO.setMessageSize(mtList.size());
                    messageDTO.setReadStatus(MessageConst.ReadStatus.ONE.getStatus());
                    messageDTO.setMessageType(MessageConst.MessageType.TWO.getStatus());
                    return RestResp.success("操作成功", messageDTO);
                }else {
                    Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                    return queryPublicMsgNoRead(userId, userGroup);
                }
            }else {
                Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                return queryPublicMsgNoRead(userId, userGroup);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读公共信息失败");
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryPublicMsgYesRead(Long userId){
        try {
            MessageDTO messageDTO = new MessageDTO();
            List<Message> messageList = messageDao.findByReceiverId(userId);
            if (messageList.size() != MessageConst.ListSize.ZERO.getValue()){
                List<MessageText> mtList = new ArrayList<>();
                for (Message m : messageList) {
                    List<MessageText> resultMessageTextList =  messageTextDao.findByIdAndMessageType(m.getMessageTextId(), MessageConst.MessageType.TWO.getStatus());
                    mtList.addAll(resultMessageTextList);
                }
                if (mtList.size() != MessageConst.ListSize.ZERO.getValue()){
                    messageDTO.setList(mtList);
                    messageDTO.setMessageSize(mtList.size());
                    messageDTO.setReadStatus(MessageConst.ReadStatus.TWO.getStatus());
                    messageDTO.setMessageType(MessageConst.MessageType.TWO.getStatus());
                    return RestResp.success("操作成功", messageDTO);
                }else {
                    Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                    return queryPublicMsgYesRead(userId);
                }
            } else {
                Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                return queryPublicMsgYesRead(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取已读公共信息失败", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryGlobalMsgNoRead(Long userId){
        try {
            MessageDTO messageDTO = new MessageDTO();
            List<MessageText> messageTextList = messageTextDao.findByMessageType(MessageConst.MessageType.THREE.getStatus());
            if (messageTextList.size() != MessageConst.ListSize.ZERO.getValue()){
                List<Message> mList = new ArrayList<>();
                List<MessageText> mtList = new ArrayList<>();
                for (MessageText mt : messageTextList) {
                    List<Message> messageList = messageDao.findByMessageTextIdAndReceiverId(mt.getId(), userId);
                    List<MessageText> resultMessageTextList = messageTextDao.findByIdAndMessageType(mt.getId(), MessageConst.MessageType.THREE.getStatus());
                    mList.addAll(messageList);
                    mtList.addAll(resultMessageTextList);
                }
                if (mList.size() == MessageConst.ListSize.ZERO.getValue()){
                    messageDTO.setMessageType(MessageConst.MessageType.THREE.getStatus());
                    messageDTO.setReadStatus(MessageConst.ReadStatus.ONE.getStatus());
                    messageDTO.setList(mtList);
                    messageDTO.setMessageSize(mtList.size());
                    return RestResp.success("操作成功", messageDTO);
                }else {
                    Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                    return queryGlobalMsgNoRead(userId);
                }
            }else {
                Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                return queryGlobalMsgNoRead(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取未读系统信息失败", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryGlobalMsgYesRead(Long userId){
        try {
            MessageDTO messageDTO = new MessageDTO();
            List<MessageText> mtList = new ArrayList<>();
            List<Message> messageList = messageDao.findByReceiverId(userId);
            if (messageList.size() != MessageConst.ListSize.ZERO.getValue()){
                for (Message m : messageList) {
                    List<MessageText> messageTextList = messageTextDao.findByIdAndMessageType(m.getMessageTextId(), MessageConst.MessageType.THREE.getStatus());
                    mtList.addAll(messageTextList);
                }
                if (mtList.size() != MessageConst.ListSize.ZERO.getValue()){
                    messageDTO.setList(mtList);
                    messageDTO.setMessageSize(mtList.size());
                    messageDTO.setReadStatus(MessageConst.ReadStatus.TWO.getStatus());
                    messageDTO.setMessageType(MessageConst.MessageType.THREE.getStatus());
                    return RestResp.success("操作成功", messageDTO);
                }else {
                    Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                    return queryGlobalMsgYesRead(userId);
                }
            }else {
                Thread.sleep(MessageConst.Constant.FIVE_THOUSAND.getValue());
                return queryGlobalMsgYesRead(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("站内信：获取已读系统信息失败", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }
}
