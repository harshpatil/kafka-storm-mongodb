package storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.bolt.FindRegionUsingIPAddressBolt;
import storm.kafka.*;

import static config.ConstantConfig.*;

public class Topology {

    public static void main(String[] args) throws Exception {

        Config config = new Config();
        config.setDebug(true);

        BrokerHosts hosts = new ZkHosts(ZOOKEEPER_HOST);
        String topic = KAFKA_TOPIC;
        String zkRoot = ZOOKEEPER_ROOT;
        String groupId = KAFKA_GROUP_ID;
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, groupId);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(STORM_SPOUT_NAME, kafkaSpout);
        topologyBuilder.setBolt(STORM_BOLT_NAME, new FindRegionUsingIPAddressBolt())
                .shuffleGrouping(STORM_SPOUT_NAME);


        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        }
        else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test-topology", config, topologyBuilder.createTopology());
//            Thread.sleep(10000);
//            cluster.killTopology("test-topology");
//            cluster.shutdown();
        }
    }
}
