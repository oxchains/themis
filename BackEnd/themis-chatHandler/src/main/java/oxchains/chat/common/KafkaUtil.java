package com.oxchains.chat.common;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

/**
 * Created by xuqi on 2017/10/17.
 */
public class KafkaUtil {
    private static Producer producer = null;
    private static String topic = "chatContent";
    static{
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "192.168.1.153:2181");//声明zk
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("metadata.broker.list", "192.168.1.153:2381");// 声明kafka broker
        producer = new Producer<String,String>(new ProducerConfig(properties));
    }

    public static void send(String message){
    producer.send(new KeyedMessage<String,String>(topic,message));
    }
}
