//package storm.spout;
//
//import backtype.storm.spout.SchemeAsMultiScheme;
//import backtype.storm.spout.SpoutOutputCollector;
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.IRichSpout;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.tuple.Fields;
//import backtype.storm.tuple.Values;
//
//import storm.kafka.BrokerHosts;
//import storm.kafka.KafkaSpout;
//import storm.kafka.SpoutConfig;
//import storm.kafka.StringScheme;
//import storm.kafka.ZkHosts;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static config.ConstantConfig.TOPIC;
//
//
//public class ProductViewEventReaderSpout implements IRichSpout {
//
//    private SpoutOutputCollector collector;
//
//    private TopologyContext topologyContext;
//    private Integer index = 0;
//
//    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
//        this.topologyContext = topologyContext;
//        this.collector = spoutOutputCollector;
//
//        BrokerHosts hosts = new ZkHosts("localhost:2181");
//        String topic = TOPIC;
//        String zkRoot = "/kafka";
//        String groupId = "mygroup";
//        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, groupId);
//        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
//        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
//    }
//
//    public void close() {
//
//    }
//
//    public void activate() {
//
//    }
//
//    public void deactivate() {
//
//    }
//
//    public void nextTuple() {
//
////        if(this.index <= 5) {
////            Integer localIndex = 0;
////            while(localIndex++ < 5 && this.index++ < 5) {
////                String ipAddress = "198.80.152.11";
////                String userId = "USER"+ localIndex;
////                String productId = "PIDTSHIRT1234";
////                String productCategory = "tshirt";
////
////                this.collector.emit(new Values(ipAddress, userId, productId, productCategory));
////            }
////        }
//
////        this.collector.emit(new Values(ipAddress, userId, productId, productCategory));
//    }
//
//    public void ack(Object o) {
//
//    }
//
//    public void fail(Object o) {
//
//    }
//
//    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
//        outputFieldsDeclarer.declare(new Fields("ipAddress", "userId", "productId", "productCategory"));
//    }
//
//    public Map<String, Object> getComponentConfiguration() {
//        return null;
//    }
//}
