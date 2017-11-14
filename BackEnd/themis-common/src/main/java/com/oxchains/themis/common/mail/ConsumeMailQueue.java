package com.oxchains.themis.common.mail;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * @author ccl
 * @time 2017-11-13 10:25
 * @name ConsumeMailQueue
 * @desc:
 */

public class ConsumeMailQueue {
    private static final Logger logger = Logger.getLogger(ConsumeMailQueue.class.getCanonicalName());

    MailService mailService = null;

    public ConsumeMailQueue(){
        mailService = new MailService();
        startThread();
    }
    public void startThread(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("consume-mail-pool-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MICROSECONDS,
                new LinkedBlockingDeque<Runnable>(100),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        executorService.submit(new PollMail(mailService));
        executorService.submit(new PollMail(mailService));
    }
    class PollMail implements Runnable{
        MailService mailService;
        public PollMail(MailService mailService){
            this.mailService = mailService;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    Email mail = MailQueue.getMailQueue().consume();
                    if (mail != null) {
                        logger.info("剩余邮件总数:"+MailQueue.getMailQueue().size());
                        mailService.send(mail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopThread() {
        logger.info("destroy");
    }

}
