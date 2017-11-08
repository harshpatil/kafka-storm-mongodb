package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static config.ConstantConfig.BOOTSTRAP_SERVERS;
import static config.ConstantConfig.TOPIC;

/**
 * Created by HarshPatil on 11/7/17.
 */
public class KafkaDataProducer {

    public static void main(String[] args) throws InterruptedException {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record = new ProducerRecord(TOPIC, "Harsh-Patil-" + i);
            producer.send(record);
            Thread.sleep(500);
        }
        producer.close();
    }
}
