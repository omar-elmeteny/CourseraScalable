FROM eclipse-temurin:21
RUN mkdir /opt/app

ENV SERVER_PORT=8080
EXPOSE 8080

COPY target/coursera-webserver-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/coursera-webserver-0.0.1-SNAPSHOT.jar"]