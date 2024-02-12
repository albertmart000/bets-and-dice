#!/bin/sh

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