# serverless-graalvm

Simple demo API showing how to use Momento GRPC SDK from GraalVM inside of AWS Lambda.

### Instructions to run Demo
1. Replace Hard Coded Momento Token in Handler.java remember to use token for `us-west-2` since that's where we will be deploying in step 4 below.

2. Start docker container used to build native image for lambda runtime
```console
docker run \
    -v ~/.m2/:/root/.m2/ \
    -v $(pwd):/build \
    -it marksailes/al2-graalvm
```

3. Switch to `build` dir inside container and run native build then exit container (build artifacts will be output to `target` dir on host machine)
```console
cd /build
mvn clean install -P native-image
exit
```

4. Switch to `infra` folder and trigger deploy
```console
cd infra
AWS_REGION=us-west-2 npm run cdk deploy
```
5. Use momento cli to set a dummy value in cache service will retrieve
```console
momento cache set -k test_key -v world -n default
```
6. Benchmark your new API 
```console
cd ../bench
./start.sh
```