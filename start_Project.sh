#!/bin/sh

# Build images micros

microservices=("betsanddice-user" "betsanddice-stat" "betsanddice-tutorial" "betsanddice-craps")
for micro in "${microservices[@]}"; do
    # shellcheck disable=SC2164
    (cd "$micro" && bash build_Docker.sh)
done