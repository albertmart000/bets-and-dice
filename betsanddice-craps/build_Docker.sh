#!/bin/sh

fileConfig=.env.dev;
now="$(date +'%d-%m-%Y %H:%M:%S:%3N')"
base_dir=`pwd`

if [ -f "$fileConfig" ]
then
  while IFS='=' read -r key value
  do
    key=$(echo "$key" | tr '.' '_')
    eval "${key}"='${value}'
  done < "$fileConfig"

  echo " Date: ""${now}"
  echo " REGISTRY_NAME=""${REGISTRY_NAME}"

  else
    echo "$fileConfig not found."
  fi

./gradlew :betsanddice-craps:clean && ./gradlew :betsanddice-craps:build

docker build -t="${REGISTRY_NAME}":betsanddice-craps-"${CRAPS_TAG}" .

if [ "${ENV}" = "dev" ]
then
docker push "${REGISTRY_NAME}":betsanddice-craps-"${CRAPS_TAG}"
fi