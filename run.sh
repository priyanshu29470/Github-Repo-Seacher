#!/bin/bash

if [ -z "$1" ]; then
    echo "Insert token after run.sh command"
    exit 1
fi

export GIT_API_TOKEN="$1"

echo "Setting up db"
cd docker || exit 1
docker-compose up -d
cd ..

echo "Setting up application"
./mvnw clean package -DskipTests  
java -jar target/*.jar  