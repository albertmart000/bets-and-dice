#!/bin/sh
#  Start parameters:
#  1.-fileConfig
#  Example: ./betsanddice-user/build_Docker.sh conf/.env.dev

# Init variables
fileConfig=$1;
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
    key=$(echo $key | tr '.' '_')
    eval ${key}='${value}'
  done < "$fileConfig"

  echo " Date: "${now}
  echo " ======================================================"
  echo ""
  echo "                 Initializing variables "
  echo ""
  echo " ======================================================"
  echo " REGISTRY_NAME="${REGISTRY_NAME}

  else
    echo "$fileConfig not found."
  fi

./gradlew :betsanddice-user:build

cd betsanddice-user
#docker build -t=${REGISTRY_NAME}betsanddice-user-${USER_TAG} .
docker build -t usuarios . -f ./Dockerfile
 .

#upload image to DockerHub
#upload_image="pre"
#if [ ${ENV} = "$upload_image" ];
#then
#  docker push ${REGISTRY_NAME}:${USER_TAG}
#fi
