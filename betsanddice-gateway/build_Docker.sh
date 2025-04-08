#!/bin/sh

fileConfig=../config/.env.dev
source "$fileConfig"

echo "Date: $(date +'%d-%m-%Y %H:%M:%S:%3N')"
echo "REGISTRY_NAME: $REGISTRY_NAME"

./gradlew :betsanddice-gateway:clean && ./gradlew :betsanddice-gateway:build

docker build -t "$REGISTRY_NAME:betsanddice-gateway-$GATEWAY_TAG" .

if [ "$ENV" = "dev" ]; then
  docker push "$REGISTRY_NAME:betsanddice-craps-$GATEWAY_TAG"
fi