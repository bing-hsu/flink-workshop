package xyz.threadlite.flinkworkshop.statefulapp;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class StatefulCounterDouble extends RichFlatMapFunction<Integer, Integer> {

    private transient ValueState<Integer> current;

    private static ValueStateDescriptor<Integer> currentDescriptor =
            new ValueStateDescriptor<>("current", Integer.class);

    @Override
    public void open(Configuration parameters) {
        current = getRuntimeContext().getState(currentDescriptor);
    }


    @Override
    public void flatMap(Integer value, Collector<Integer> out) throws Exception {
        if (current.value() == null) {
            current.update(1);
            out.collect(current.value());
            return;
        }

        current.update(current.value() * 2);
        out.collect(current.value());
    }
}
