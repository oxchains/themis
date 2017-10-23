package com.oxchains.themis.chat.common;

import com.oxchains.themis.chat.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import com.oxchains.themis.chat.websocket.WebSocketServer;

/**
 * Created by xuqi on 2017/10/19.
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${websocket.port}")
    private Integer port;
    private KafkaService kafkaService;
    public StartupListener(@Autowired KafkaService kafkaService){
      this.kafkaService = kafkaService;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(new WebSocketServer(kafkaService,port)).start();
    }
}
