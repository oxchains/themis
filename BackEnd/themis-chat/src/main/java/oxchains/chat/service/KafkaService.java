package oxchains.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by xuqi on 2017/10/20.
 */
@Service
public class KafkaService {
    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    public void send(String message) {
         kafkaTemplate.send(topic,message);
    }
}
