package oxchains.chat.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import oxchains.chat.service.KafkaService;
import oxchains.chat.websocket.WebSocketServer;

import javax.annotation.Resource;

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
