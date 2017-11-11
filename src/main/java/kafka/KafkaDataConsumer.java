//package kafka;
//
//import com.twitter.bijection.Injection;
//import com.twitter.bijection.avro.GenericAvroCodecs;
//import org.apache.avro.Schema;
//import org.apache.avro.generic.GenericRecord;
//import org.apache.kafka.clients.consumer.*;
//import java.util.Arrays;
//import java.util.Properties;
//
//import static config.ConstantConfig.BOOTSTRAP_SERVERS;
//import static config.ConstantConfig.PRODUCT_VIEW_EVENT;
//import static config.ConstantConfig.KAFKA_TOPIC;
//
//public class KafkaDataConsumer {
//
//    private static Injection<GenericRecord, byte[]> recordInjection;
//
//    static {
//        Schema.Parser parser = new Schema.Parser();
//        Schema schema = parser.parse(PRODUCT_VIEW_EVENT);
//        recordInjection = GenericAvroCodecs.toBinary(schema);
//    }
//    public static void main(String[] args) {
//
//        Properties props = new Properties();
//        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
//        props.put("group.id", "mygroup");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Arrays.asList(KAFKA_TOPIC));
//
//        boolean running = true;
//        while (running) {
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println(record.value());
//            }
//        }
//
//        consumer.close();
//    }
//
//}
