# Flink Workshop

This workshop's theme is "working with state".

> Here we can see things that AWS hides from you in the name of convenience,
> at the expense of transparency and control.

## Cut the Hassle and Make it Run!

We have numerous ways to run a Flink application.
From a humble `main()` function invocation, a single-node local cluster,
a resource-manager-equipped-cluster (YARN/Kubernetes) deployment, to a serverless (AWS Managed Flink) deployment.
The more intermediates we have, the more challenging we can understand the core mechanisms of the Flink system.

In this workshop, we keep it simple to highlight these core mechanisms.

### Run a Flink application like a normal Java Application

This is the most straightforward way to run a Flink application, and a good way to locally debug your application.

```kotlin
// include these dependencies to the runtime classpath
// - flink-clients
// - flink-runtime
// - flink-table-runtime
// - flink-table-planner-loader
// and execute the `main()` as any other java program

val flinkVer = "1.17.1"
val flinkDomain = "org.apache.flink"
dependencies {
    // use ./gradlew run -Plocal=1 to run it like a usual java executable
    runtimeOnly("${flinkDomain}:flink-runtime:${flinkVer}")
    runtimeOnly("${flinkDomain}:flink-table-runtime:${flinkVer}")
    runtimeOnly("${flinkDomain}:flink-table-planner-loader:${flinkVer}")
    // need flink-clients to run it like a usual java executable
    // https://stackoverflow.com/questions/63600971/no-executorfactory-found-to-execute-the-application-in-flink-1-11-1
    runtimeOnly("${flinkDomain}:flink-clients:${flinkVer}")
}
```

### Setup a single node Flink cluster

Leaving the high-available and scalability aside,
a single node cluster is enough to get exposed to all essential features of Flink.

Download a pre-built Flink distribution from [here](https://flink.apache.org/downloads.html).

```bash
# kick-off a single node Flink cluster
<flink-dist>/bin/start-cluster.sh

# visit the Flink dashboard
# at http://localhost:8081

# shutdown the cluster
<flink-dist>/bin/stop-cluster.sh
```

## A Minimum-Viable Stateful Flink Application

See application code at `xyz.threadlite.flinkworkshotp.statefulapp.MinimumStatefulApp`.

You can inspect application's output by following taskmanager's log file
at `<flink-dist>/log/flink-<user>-taskexecutor-<host>.out`.

```bash
