#!/bin/sh

#Run Consul:
#docker run -d --name=consul-server -p 8500:8500 -p 8600:8600/udp consul:1.15.3 agent -server -node=server-1 -bootstrap-expect=1 -ui -bind 127.0.0.1 -client 0.0.0.0

# Build images micros
directories=("betsanddice-user" "betsanddice-stat" "betsanddice-tutorial" "betsanddice-craps")
for dir in "${directories[@]}"; do
    # shellcheck disable=SC2164
    (cd "$dir" && bash build_Docker.sh)
done

docker image prune -f
# shellcheck disable=SC2164
cd docker
docker-compose up -d