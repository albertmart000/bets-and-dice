#!/bin/sh
#  Start parameters:
#  1.-fileConfig
#  Example: ./betsanddice-user/build_Docker.sh conf/.env.dev

# Init variables
fileConfig=$1;
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

./gradlew :betsanddice-user:build

cd betsanddice-user


#docker build -t ${REGISTRY_NAME} ${USER_TAG} .

docker build -t albertmart000/betsanddice:betsanddice-user-1.0-SNAPSHOT . -f ./Dockerfile

#docker build -t usuarios . -f ./Dockerfile
#docker build -t=${REGISTRY_NAME}:user-${USER_TAG} .
# registry/repository[:tag] to pull by tag, or registry/repository[@digest] to pull by diges
#docker build -t containername/tag -f ProjectFolder/Dockerfile.

#docker build -t betsanddice-user-image  -f ./Dockerfile
#docker build -t=${REGISTRY_NAME}:betsanddice-user .
#docker build -t=${REGISTRY_NAME}:betsandd
# ice-user-${USER_TAG} .



#docker build -t betsanddice-user-image . -f ./Dockerfile
#docker build -t=${REGISTRY_NAME}:itachallenge-challenge-${CHALLENGE_TAG} .

#upload image to DockerHub
#if [ ${ENV} = "dev" ] || [ ${ENV} = "pre" ];
#then
#docker build -t=${REGISTRY_NAME}:betsanddice-user-${USER_TAG} .
#fi

docker push albertmart000/betsanddice:betsanddice-user-1.0-SNAPSHOT

#if [ "${ENV}" = "dev" ]
#then
#  docker push albertmart000/betsanddice:betsanddice-user-1.0-SNAPSHOT
#fi

