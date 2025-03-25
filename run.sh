#!/bin/bash

if [ -n "$1" ]; then
    export GIT_API_TOKEN="$1"
    echo "export GIT_API_TOKEN=\"$1\"" >> ~/.bashrc
elif [ -z "$GIT_API_TOKEN" ]; then
    echo "Insert token after run.sh command or set GIT_API_TOKEN in the environment"
    exit 1
fi

echo "Setting up db"
cd docker || exit 1
docker-compose up -d
cd ..

echo "Setting up application"
./mvnw clean package -DskipTests  
java -jar target/*.jar  