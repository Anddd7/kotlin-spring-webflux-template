# kotlin-spring-webflux

## Set up
1. enable git hooks
```bash
git config --local core.hooksPath .githooks/ 
chmod 700 .githooks/pre-push
```


## Test

```
./gradlew test
./gradlew apiTest
./gradlew check
./gradlew jacocoTestReport
```

## Run

```bash
# set up local db
docker-compose -f docker-compose.local.yml up
# run spring application
./gradlew bootRun
# request
curl http://localhost:8080/hello
```

## Deploy
