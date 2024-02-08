#!/bin/sh
#  Start parameters:
#  1.-fileConfig
#  Example: ./betsanddice-stat/build_Docker.sh .env.dev

# Init variables
fileConfig=.env.dev;
now="$(date +'%d-%m-%Y %H:%M:%S:%3N')"
base_dir=`pwd`

# Load environment variables
if [ -f "$fileConfig" ]
then
  echo ""
  echo " Loading config from $fileConfig"
  echo ""

  while IFS='=' read -r key value
  do
    key=$(echo "$key" | tr '.' '_')
    eval "${key}"='${value}'
  done < "$fileConfig"

  echo " Date: ""${now}"
  echo " ======================================================"
  echo ""
  echo "                 Initializing variables "
  echo ""
  echo " ======================================================"
  echo " REGISTRY_NAME=""${REGISTRY_NAME}"

  else
    echo "$fileConfig not found."
  fi

#./gradlew :itachallenge-mock:clean &&
./gradlew :betsanddice-stat:build

docker build -t="${REGISTRY_NAME}":betsanddice-stat-"${STAT_TAG}" .

if [ "${ENV}" = "dev" ]
then
docker push "${REGISTRY_NAME}":betsanddice-stat-"${STAT_TAG}"
fi