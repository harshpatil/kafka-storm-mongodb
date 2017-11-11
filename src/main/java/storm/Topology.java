package storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.bolt.FindRegionUsingIPAddressBolt;
import storm.kafka.*;

import static config.ConstantConfig.TOPIC;

public class Topology {

    public static void main(String[] args) throws Exception {

        Config config = new Config();
        config.setDebug(true);

        BrokerHosts hosts = new ZkHosts("localhost:2181");
        String topic = TOPIC;
        String zkRoot = "/kafka";
        String groupId = "mygroup";
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, groupId);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        TopologyBuilder topologyBuilder = new TopologyBuilder();
//        topologyBuilder.setSpout("producViewEventReader", new ProductViewEventReaderSpout());
        topologyBuilder.setSpout("producViewEventReader", kafkaSpout);
        topologyBuilder.setBolt("productEventWriteRegion", new FindRegionUsingIPAddressBolt())
                .shuffleGrouping("producViewEventReader");


        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        }
        else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("testProductViewEvent-topology", config, topologyBuilder.createTopology());
//            Thread.sleep(10000);
//            cluster.killTopology("testProductViewEvent-topology");
//            cluster.shutdown();
        }
    }
}
