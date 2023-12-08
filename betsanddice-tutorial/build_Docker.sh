#!/bin/sh
#  Start parameters:
#  1.-fileConfig
#  Example: ./betsanddice-tutorial/build_Docker.sh .env.dev

# Init variables
fileConfig=.env.dev;
now="$(date +'%d-%m-%Y %H:%M:%S:%3N')"
# shellcheck disable=SC2034
# shellcheck disable=SC2006
base_dir=`pwd`

# Load environment variables
if [ -f "$fileConfig" ]
then
  echo ""
  echo " Loading config from $fileConfig"
  echo ""

  # shellcheck disable=SC2034
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
./gradlew :betsanddice-tutorial:build

# shellcheck disable=SC2164
cd betsanddice-tutorial

docker build -t="${REGISTRY_NAME}":betsanddice-tutorial-"${TUTORIAL_TAG}" .

#if [ "${ENV}" = "dev" ]
#then
#docker push "${REGISTRY_NAME}":betsanddice-tutorial-"${TUTORIAL_TAG}" .
#fi