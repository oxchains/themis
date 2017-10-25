package oxchains.chat.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import oxchains.chat.service.KafkaService;
import oxchains.chat.websocket.WebSocketServer;



/**
 * Created by xuqi on 2017/10/19.
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent>{
    private KafkaService kafkaService;
    public StartupListener(@Autowired KafkaService kafkaService){
      this.kafkaService = kafkaService;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(new WebSocketServer(kafkaService)).start();
    }
}
