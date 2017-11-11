package storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by HarshPatil on 11/10/17.
 */
public class ProductViewEventReaderSpout implements IRichSpout {

    private SpoutOutputCollector collector;

    private TopologyContext topologyContext;
    private Integer index = 0;


    public static final String PRODUCT_VIEW_EVENT = "{\n" +
            "\t\"ipAddress\": \"198.23.1.56\",\n" +
            "\t\"userId\": \"USER142012\",\n" +
            "\t\"productId\": \"p1\",\n" +
            "\t\"productCategory\": \"tshirt\"\n" +
            "}";

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.topologyContext = topologyContext;
        this.collector = spoutOutputCollector;
    }

    public void close() {

    }

    public void activate() {

    }

    public void deactivate() {

    }

    public void nextTuple() {

        if(this.index <= 5) {
            Integer localIndex = 0;
            while(localIndex++ < 5 && this.index++ < 5) {
//                String ipAddress = "208.80.152.201";
                String ipAddress = "198.80.152.11";
                String userId = "USER"+ localIndex;
                String productId = "PIDTSHIRT1234";
                String productCategory = "tshirt";

                this.collector.emit(new Values(ipAddress, userId, productId, productCategory));
            }
        }
    }

    public void ack(Object o) {

    }

    public void fail(Object o) {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("ipAddress", "userId", "productId", "productCategory"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
