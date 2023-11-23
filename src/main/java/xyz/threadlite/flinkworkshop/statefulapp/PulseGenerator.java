package xyz.threadlite.flinkworkshop.statefulapp;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class PulseGenerator implements SourceFunction<Integer> {
    private boolean running = true;

    @Override
    public void run(SourceContext<Integer> ctx) throws Exception {
        while (running) {
            ctx.collect(1);
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
