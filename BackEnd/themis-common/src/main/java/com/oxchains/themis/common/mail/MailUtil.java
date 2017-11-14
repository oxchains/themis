package com.oxchains.themis.common.mail;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ccl
 * @time 2017-11-13 10:41
 * @name MailUtil
 * @desc:
 */
public class MailUtil {
    private Logger logger = Logger.getLogger(MailUtil.class.getCanonicalName());

    private ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1,new BasicThreadFactory.Builder().namingPattern("mail-scheldule-pool-%d").daemon(true).build());

    private final AtomicInteger count = new AtomicInteger(1);


    public void start(final JavaMailSender mailSender, final SimpleMailMessage message) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (count.get() == 2) {
                        service.shutdown();
                        logger.info("the task is down");
                    }
                    logger.info("start send email and the index is " + count);
                    mailSender.send(message);
                    logger.info("send email success");
                }catch (Exception e){
                    logger.log(Level.FINE,"send email fail" , e);
                }

            }
        });
    }
    public void startHtml(final JavaMailSender mailSender,final MimeMessage message) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (count.get() == 2) {
                        service.shutdown();
                        logger.info("the task is down");
                    }
                    logger.info("start send email and the index is " + count);
                    mailSender.send(message);
                    logger.info("send email success");
                }catch (Exception e){
                    logger.log(Level.FINE,"send email fail" , e);
                }

            }
        });
    }
}
