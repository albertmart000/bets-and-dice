#!/bin/bash

for dir in "betsanddice-config" "betsanddice-eureka" "betsanddice-user" "betsanddice-craps" "betsanddice-gateway"; do
  (cd "$dir" && bash build_Docker.sh)
done

cd docker/default

docker-compose up -d