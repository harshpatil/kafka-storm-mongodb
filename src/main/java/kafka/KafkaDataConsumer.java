package kafka;

import org.apache.kafka.clients.consumer.*;
import java.util.Arrays;
import java.util.Properties;

import static config.ConstantConfig.BOOTSTRAP_SERVERS;
import static config.ConstantConfig.TOPIC;

/**
 * Created by HarshPatil on 11/7/17.
 */
public class KafkaDataConsumer {

    /*public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", "mygroup");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(TOPIC));

        boolean running = true;
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

        consumer.close();
    }*/
}
