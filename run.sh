#!/bin/bash

echo "Setting up db"
cd docker || exit 1
docker-compose up -d
cd ..

echo "Setting up application"
./mvnw clean package -DskipTests  
java -jar target/*.jar  