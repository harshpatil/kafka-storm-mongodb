package storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import okhttp3.*;
import org.bson.Document;
import storm.model.IPApiResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by HarshPatil on 11/10/17.
 */
public class FindRegionUsingIPAddressBolt implements IRichBolt {

    private OutputCollector outputCollector;
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    private String collection;
    private OkHttpClient httpClient;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        this.outputCollector = outputCollector;
        this.mongoClient = new MongoClient("localhost", 27017);
        this.mongoDB = mongoClient.getDatabase("UserInsights");
        this.collection = "productViewEvent";
    }

    public void execute(Tuple tuple) {

        String ipAddress = tuple.getString(0);
        String url = "http://ip-api.com/json/" + ipAddress;

        try {
            Response response = executeGETRequest(url, "GET");
            IPApiResponse ipApiResponse = new ObjectMapper().readValue(new String(response.body().bytes()), IPApiResponse.class);
            String userId = tuple.getString(1);
            String productId = tuple.getString(2);
            String productCategory = tuple.getString(3);

            Document document = new Document();
            document.append("ipAddress", ipAddress);
            document.append("userId", userId);
            document.append("productId", productId);
            document.append("productCategoty", productCategory);
            document.append("state", ipApiResponse.getRegionName());
            document.append("stateCode", ipApiResponse.getRegion());
            document.append("country", ipApiResponse.getCountry());
            document.append("countryCode", ipApiResponse.getCountryCode());
            document.append("city", ipApiResponse.getCity());
            document.append("zip", ipApiResponse.getZip());
            document.append("eventDate", System.currentTimeMillis());

            mongoDB.getCollection(collection).insertOne(document);
            outputCollector.ack(tuple);
        }catch(Exception e) {
            e.printStackTrace();
            outputCollector.fail(tuple);
        }
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    private Response executeGETRequest(String path, String method) throws IOException {
        Request request = new Request.Builder()
                                .url(path)
                                .method(method, null)
                                .build();
        return getHttpClient().newCall(request).execute();
    }

    /**
     * Get the http REST client
     *
     * @return
     */
    private OkHttpClient getHttpClient() {
        if (httpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            httpClient = builder.retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }
}
