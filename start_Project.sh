#!/bin/bash

directories=("betsanddice-config" "betsanddice-eureka" "betsanddice-user" "betsanddice-craps")
for dir in "${directories[@]}"; do
    (cd "$dir" && bash build_Docker.sh)
done

cd docker
cd default

COMPOSE_FILE="docker-compose.yml"

docker-compose -f $COMPOSE_FILE up -d