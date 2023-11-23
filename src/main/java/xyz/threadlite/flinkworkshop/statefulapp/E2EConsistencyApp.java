package xyz.threadlite.flinkworkshop.statefulapp;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

public class E2EConsistencyApp {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: E2EConsistencyApp <printer-prefix> <source-dir>");
            System.exit(1);
        }

        var env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // File Source is stateful.
        // see https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/connectors/datastream/filesystem/
        //
        // If read files are not modified (updated or deleted), then it is also a replay-able source
        //
        // Upon checkpointing or taking a savepoint, the File Source will save the latest timestamp of file it has read.
        // Stateful resume / recovery relies on this timestamp to avoid duplicate reading.
        var fileSource = FileSource.forRecordStreamFormat(
                        new TextLineInputFormat(),
                        new Path(args[1]))
                .monitorContinuously(Duration.ofSeconds(1))
                .build();

        // By setting id as primary key and do nothing on insertion conflict,
        // we can ensure that the sink is idempotent.
        var sqliteSink = JdbcSink.<String>sink(
                "INSERT INTO idempotent_sink(id) VALUES (?) ON CONFLICT DO NOTHING",
                (ps, t) -> ps.setString(1, t),
                JdbcExecutionOptions.builder()
                        .withBatchSize(1)
                        .withBatchIntervalMs(1000)
                        .withMaxRetries(3)
                        .build(),
                new JdbcConnectionOptions
                        .JdbcConnectionOptionsBuilder()
                        .withDriverName("org.sqlite.JDBC")
                        .withUrl("jdbc:sqlite:/tmp/flink-tmp/sink.sqlite")
                        .build()
        );


        var src = env
                .fromSource(fileSource, WatermarkStrategy.noWatermarks(), "file-source")
                .setParallelism(1);

        src.print(args[0]);

        src.addSink(sqliteSink);
        env.execute();
    }

}
