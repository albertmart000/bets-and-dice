#!/bin/bash

cd docker
cd default

docker image prune -f
docker volume prune -f
docker container prune -f
docker network prune -f
docker system prune -f
docker-compose down