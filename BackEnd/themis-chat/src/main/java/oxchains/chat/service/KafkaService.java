package oxchains.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    public void send(String message) {
        try {
            kafkaTemplate.send(topic,message);
        }catch (Exception r){
            LOG.debug("faild to send message : ",r.getMessage());
        }
    }
}
