package storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import okhttp3.*;
import org.bson.Document;
import storm.model.IPApiResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static config.ConstantConfig.*;

public class FindRegionUsingIPAddressBolt implements IRichBolt {

    private OutputCollector outputCollector;
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    private String collection;
    private OkHttpClient httpClient;
    JSONObject eventJson = null;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        this.outputCollector = outputCollector;
        this.mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT_NUMBER);
        this.mongoDB = mongoClient.getDatabase(MONGO_DB_NAME);
        this.collection = MONGO_COLLECTION_NAME;
    }

    public void execute(Tuple tuple) {

        Fields fields = tuple.getFields();
        eventJson = (JSONObject) JSONSerializer.toJSON((String) tuple.getValueByField(fields.get(0)));
        String ipAddress = (String) eventJson.get("ipAddress");
        String userId = (String) eventJson.get("userId");
        String productId = (String) eventJson.get("productId");
        String productCategory = (String) eventJson.get("productCategory");
        String url = "http://ip-api.com/json/" + ipAddress;

        try {
            Response response = executeGETRequest(url, "GET");
            IPApiResponse ipApiResponse = new ObjectMapper().readValue(new String(response.body().bytes()), IPApiResponse.class);

            Document document = new Document();
            document.append("ipAddress", ipAddress);
            document.append("userId", userId);
            document.append("productId", productId);
            document.append("productCategory", productCategory);
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
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }
}
