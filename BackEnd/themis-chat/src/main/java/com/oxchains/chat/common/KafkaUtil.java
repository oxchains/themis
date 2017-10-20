package com.oxchains.chat.common;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by xuqi on 2017/10/17.
 */
public class KafkaUtil {
    private static Producer producer = null;
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static String topic = "chatContent";
    private static Properties properties=null;
    static{
        properties = new Properties();
        properties.put("zookeeper.connect", "192.168.1.153:2181");//声明zk
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("metadata.broker.list", "192.168.1.153:2381");// 声明kafka broker
        //producer = new Producer<String,String>(new ProducerConfig(properties));
    }

    public static void send(String message){
        try {
            producer = new Producer<String,String>(new ProducerConfig(properties));
            producer.send(new KeyedMessage<String,String>(topic,message));
            producer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
