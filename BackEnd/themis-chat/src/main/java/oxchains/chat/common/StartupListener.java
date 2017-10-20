package oxchains.chat.common;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import oxchains.chat.websocket.WebSocketServer;

/**
 * Created by xuqi on 2017/10/19.
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(new WebSocketServer()).start();
    }
}
