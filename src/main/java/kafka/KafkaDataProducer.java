//package kafka;
//
//import com.twitter.bijection.Injection;
//import com.twitter.bijection.avro.GenericAvroCodecs;
//import org.apache.avro.Schema;
//import org.apache.avro.generic.GenericData;
//import org.apache.avro.generic.GenericRecord;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//
//import java.util.Properties;
//
//import static config.ConstantConfig.BOOTSTRAP_SERVERS;
//import static config.ConstantConfig.PRODUCT_VIEW_EVENT;
//import static config.ConstantConfig.TOPIC;
//
//public class KafkaDataProducer {
//
//    public static void main(String[] args) throws InterruptedException {
//
//        Properties properties = new Properties();
//        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
//        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
//
////        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
////        for (int i = 0; i < 100; i++) {
////            ProducerRecord<String, String> record = new ProducerRecord(TOPIC, "Harsh-Patil-" + i);
////            producer.send(record);
////            Thread.sleep(500);
////        }
////        producer.close();
//
//        Schema.Parser parser = new Schema.Parser();
//        Schema schema = parser.parse(PRODUCT_VIEW_EVENT);
//        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
//
//        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);
//        for(int i=0; i<5; i++){
//
//            GenericData.Record avroRecord = new GenericData.Record(schema);
//            avroRecord.put("ipAddress", "198.80.152.11");
//            avroRecord.put("userId","USER"+ i);
//            avroRecord.put("productId", "PIDTSHIRT1234");
//            avroRecord.put("productCategory", "tshirt");
//
//            byte[] bytes = recordInjection.apply(avroRecord);
//            ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC, bytes);
//            producer.send(record);
//            Thread.sleep(500);
//        }
//
//    }
//}
