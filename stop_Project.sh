#!/bin/bash

cd docker/default

docker system prune -f
docker-compose down