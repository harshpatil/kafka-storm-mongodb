package storm;

import backtype.storm.topology.IRichBolt;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.tuple.Fields;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;


/**
 * Created by HarshPatil on 11/9/17.
 */
public class CallLogCounterBolt implements IRichBolt {

    Map<String, Integer> counterMap;
    private OutputCollector collector;
    private MongoDatabase mongoDB;
    private MongoClient mongoClient;
    private String collection;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.counterMap = new HashMap<String, Integer>();
        this.collector = collector;
        this.mongoClient = new MongoClient("localhost", 27017);
        this.mongoDB = mongoClient.getDatabase("bigDataProject");
        this.collection = "callLog";
    }

    @Override
    public void execute(Tuple tuple) {
        String call = tuple.getString(0);
        Integer duration = tuple.getInteger(1);

        if(!counterMap.containsKey(call)){
            counterMap.put(call, 1);
        }else{
            Integer c = counterMap.get(call) + 1;
            counterMap.put(call, c);
        }

        Document document = new Document();
        document.append("from", call);
        document.append("to", call);;
        try{
            mongoDB.getCollection(collection).insertOne(document);
        }catch(Exception e) {
            e.printStackTrace();
            collector.fail(tuple);
        }



        // Connect to mongodb
//        Mongo mongo = new Mongo("localhost", 27017);
//        MongoClient mongo = new MongoClient("localhost", 27017);
//        DB db = mongo.getDB("bigDataProject");
//        MongoDatabase db = mongo.getDatabase("bigDataProject");
//        DBCollection collection = db.getCollection("callLog");
//        BasicDBObject document = new BasicDBObject();
//        document.append("from", call);
//        document.append("to", call);
//        collection.insert(document);
//        mongo.close();

        collector.ack(tuple);
    }

    @Override
    public void cleanup() {
        for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
            System.out.println(entry.getKey()+" : " + entry.getValue());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("call"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
