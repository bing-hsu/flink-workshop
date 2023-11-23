package xyz.threadlite.flinkworkshop.statefulapp;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntFuncsSupplier {
    static class AddOneSupplier implements Supplier<Function<Integer, Integer>>, Serializable {
        @Override
        public Function<Integer, Integer> get() {
            return integer -> integer + 1;
        }
    }

    static class DoubleSupplier implements Supplier<Function<Integer, Integer>>, Serializable {
        @Override
        public Function<Integer, Integer> get() {
            return integer -> integer * 2;
        }
    }
}
