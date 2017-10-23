package oxchains.chat.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import oxchains.chat.common.JsonUtil;
import oxchains.chat.entity.ChatContent;
import oxchains.chat.repo.MongoRepo;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by xuqi on 2017/10/17.
 */
@Component
public class Listener {
    @Resource
    private MongoRepo mongoRepo;
    public Listener(){};

    @KafkaListener(topics = {"chatContent"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            ChatContent chatContent = (ChatContent) JsonUtil.fromJson((String)message, ChatContent.class);
            mongoRepo.save(chatContent);
        }
    }

}
