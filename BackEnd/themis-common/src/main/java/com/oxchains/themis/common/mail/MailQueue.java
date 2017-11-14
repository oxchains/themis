package com.oxchains.themis.common.mail;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author ccl
 * @time 2017-11-13 10:19
 * @name MailQueue
 * @desc:
 */
public class MailQueue {
    static final int QUEUE_MAX_SIZE = 1000;
    static BlockingDeque<Email> blockingDeque = new LinkedBlockingDeque<>(QUEUE_MAX_SIZE);
    private MailQueue(){}

    private static class SingletonHolder{
        private static MailQueue queue = new MailQueue();
    }

    public static MailQueue getMailQueue(){
        return SingletonHolder.queue;
    }
    public void produce(Email email) throws InterruptedException{
        blockingDeque.put(email);
    }
    public Email consume() throws InterruptedException{
        return blockingDeque.take();
    }
    public int size(){
        return blockingDeque.size();
    }
}
