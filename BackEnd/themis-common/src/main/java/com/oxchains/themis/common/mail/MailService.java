package com.oxchains.themis.common.mail;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author oxchains
 * @time 2017-11-13 10:28
 * @name MailService
 * @desc:
 */
@Service
public class MailService {
    private static final Logger logger = Logger.getLogger(MailService.class.getCanonicalName());

    private JavaMailSender mailSender = new JavaMailSenderImpl();

//    @Resource
//    public Configuration configuration;
//    @Resource
//    private SpringTemplateEngine templateEngine;

    private final String fromUser = "oxchains@oxchains.com";
    //@Resource
    //private RedisTemplate<String> redisTemplate;

    static {
        System.setProperty("mail.mime.splitlongparameters","false");
    }

    public void send(Email email) throws Exception{
        logger.info("发送邮件");
        MailUtil mailUtil = new MailUtil();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromUser);
        mailMessage.setTo(email.getEmail());
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getContent());
        mailUtil.start(mailSender,mailMessage);
    }
    public void sendQueue(Email email) throws Exception{
        MailQueue.getMailQueue().produce(email);
    }
}
