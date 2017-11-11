package storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import storm.bolt.FindRegionUsingIPAddressBolt;
import storm.spout.ProductViewEventReaderSpout;

/**
 * Created by HarshPatil on 11/10/17.
 */
public class Topology {

    public static void main(String[] args) throws Exception {

        Config config = new Config();
        config.setDebug(true);

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("producViewEventReader", new ProductViewEventReaderSpout());
        topologyBuilder.setBolt("productEventWriteRegion", new FindRegionUsingIPAddressBolt())
                .shuffleGrouping("producViewEventReader");

        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        }
        else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("testProductViewEvent-topology", config, topologyBuilder.createTopology());
            Thread.sleep(10000);
            cluster.killTopology("testProductViewEvent-topology");
            cluster.shutdown();
        }
    }
}
