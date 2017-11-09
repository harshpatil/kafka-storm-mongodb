package storm;

import backtype.storm.tuple.Fields;

//import storm configuration packages
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.StormSubmitter;

/**
 * Created by HarshPatil on 11/9/17.
 */
public class LogAnalyserStorm {

    public static void main(String[] args) throws Exception{

        //Create Config instance for cluster configuration
        Config config = new Config();
        config.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("call-log-reader-spout", new FakeCallLogReaderSpout());

        builder.setBolt("call-log-creator-bolt", new CallLogCreatorBolt())
                .shuffleGrouping("call-log-reader-spout");

        builder.setBolt("call-log-counter-bolt", new CallLogCounterBolt())
                .fieldsGrouping("call-log-creator-bolt", new Fields("call"));

        if (args != null && args.length > 0) {
            config.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }
        else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", config, builder.createTopology());
            Thread.sleep(10000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}
