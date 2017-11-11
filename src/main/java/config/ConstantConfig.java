package config;

public class ConstantConfig {

    public final static String ZOOKEEPER_HOST = "localhost:2181";
    public final static String ZOOKEEPER_ROOT = "/kafka";
    public final static String KAFKA_GROUP_ID = "mygroup";
    public final static String KAFKA_TOPIC = "test4";

    public final static String STORM_SPOUT_NAME = "producViewEventReader";
    public final static String STORM_BOLT_NAME = "productEventWriteRegion";


    public final static String MONGO_DB_NAME = "UserInsights";
    public final static String MONGO_COLLECTION_NAME = "productViewEvent";
    public final static String MONGO_HOST = "localhost";
    public final static int MONGO_PORT_NUMBER = 27017;
}
