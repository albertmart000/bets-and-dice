#!/bin/sh

fileConfig=config/.env.dev
now=$(date +'%d-%m-%Y %H:%M:%S:%3N')
base_dir=$(pwd)

if [ -f "$fileConfig" ]; then
  while IFS='=' read -r key value; do
    key=$(echo "$key" | tr '.' '_')
    export "${key}=${value}"
  done < "$fileConfig"

  echo "Date: $now"
  echo "REGISTRY_NAME: $REGISTRY_NAME"

 ./gradlew :betsanddice-user:clean && ./gradlew :betsanddice-user:build

  if [ "$ENV" = "dev" ]; then
    docker push "$REGISTRY_NAME:betsanddice-user-$USER_TAG"
  fi
else
  echo "$fileConfig not found."
fi