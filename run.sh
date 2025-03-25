#!/bin/bash

source ~/.bashrc

if [ -z "$GIT_API_TOKEN" ]; then
    read -p "Please enter your GitHub API Token: " token
    export GIT_API_TOKEN="$token"
    echo "export GIT_API_TOKEN=\"$token\"" >> ~/.bashrc
fi

echo "Setting up db"
cd docker || exit 1
docker-compose up -d
cd ..

echo "Setting up application"
./mvnw clean package -DskipTests  
java -jar target/*.jar  