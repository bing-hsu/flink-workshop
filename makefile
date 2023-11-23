clean:
	@./gradlew clean

cleanData:
	@rm -rf ./private/flink-tmp/flink-fs-source/*.txt

runLocal:
	@./gradlew run -Pmain=MinimumApp -Plocal=1 2>/dev/null

startFlinkCluster:
	@./private/flink/bin/start-cluster.sh

stopFlinkCluster:
	@./private/flink/bin/stop-cluster.sh


buildMinimumApp:
	@./gradlew shadowjar -Pmain=MinimumApp -Pversion=minimum &>/dev/null

buildStateful: buildMinimumStatefulAppV1 buildMinimumStatefulAppV2

buildMinimumStatefulAppV1:
	@./gradlew shadowjar -Pmain=MinimumStatefulAppV1 -Pversion=minimum-stateful-v1

buildMinimumStatefulAppV2:
	@./gradlew shadowjar -Pmain=MinimumStatefulAppV2 -Pversion=minimum-stateful-v2


buildE2E:
	@./gradlew shadowjar -Pmain=E2EConsistencyApp -Pversion=e2e
