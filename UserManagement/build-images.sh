#!/bin/bash
JAVA_HOME=${JAVA_HOME:-"/c/Program Files/Java/jdk-21"}
export JAVA_HOME=${JAVA_HOME}

# Build the project
./mvnw clean install
# Build the user management image

docker build -t bugbusters/coursera-users-web:latest user-management-web
docker build -t bugbusters/coursera-users-service:latest user-management-service