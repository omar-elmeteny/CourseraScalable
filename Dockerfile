FROM maven:3.8-openjdk-17-slim


WORKDIR /user_managment
COPY pom.xml /user_managment/pom.xml
COPY src/ /user_managment/src

RUN mvn clean install