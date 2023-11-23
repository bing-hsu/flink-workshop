package xyz.threadlite.flinkworkshop.statefulapp;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MinimumStatefulAppV1 {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: E2EConsistencyApp <printer-prefix>");
            System.exit(1);
        }

        var env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        // Checkpointing is for Flink to recover from unexpected downtime;
        // it is an internal save of state, and most of the time you don't need to worry about it
        // after setting its backend and storage path.
        //
        // This contrasts with "savepoints", which are user-initiated saves of state, and are
        // triggered externally (e.g., via the Flink CLI or REST API)
        env.enableCheckpointing(1000);
        env.getCheckpointConfig().setCheckpointStorage("file:///tmp/flink-tmp/flink-checkpoints");


        var src = env.addSource(new PulseGenerator())
                .keyBy(x -> 0)
                .flatMap(new StatefulCounterAddOne())
                .uid("stateful-counter");

        src.print(args[0]);

        env.execute();
    }
}
