package xyz.threadlite.flinkworkshop.statefulapp;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MinimumApp {
    public static void main(String[] args) throws Exception {
        var env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        var src = env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9);
        src.print();

        env.execute();
    }
}
