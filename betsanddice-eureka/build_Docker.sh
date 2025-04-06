#!/bin/sh

fileConfig=../config/.env.dev
source "$fileConfig"

echo "Date: $(date +'%d-%m-%Y %H:%M:%S:%3N')"
echo "REGISTRY_NAME: $REGISTRY_NAME"

./gradlew :betsanddice-eureka:clean && ./gradlew :betsanddice-eureka:build

docker build -t "$REGISTRY_NAME:betsanddice-eureka-$EUREKA_TAG" .

if [ "$ENV" = "dev" ]; then
  docker push "$REGISTRY_NAME:betsanddice-eureka-$EUREKA_TAG"
fi