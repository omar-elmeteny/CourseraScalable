DOCKER_IMAGE := .

test:
	mvn test

site:
	mvn site

clean:
	mvn checkstyle:checkstyle

docs:
	mvn javadoc:javadoc

run-discovery-server:
	cd ./descovery-server && start /B mvn spring-boot:run

run-data-insertions:
	cd ./data-insertions && start /B mvn spring-boot:run

run-authentication:
	cd ./authentication-service && start /B mvn spring-boot:run

run-authorization:
	cd ./authorization-service && start /B mvn spring-boot:run

run-profile-management-service:
	cd ./profile-management-service && start /B mvn spring-boot:run

run-user-activity-service:
	cd ./user-activity-service && start /B mvn spring-boot:run

# Define a target to run all services in parallel

run-all-services: run-discovery-server run-data-insertions run-authentication run-authorization run-profile-management-service
	@echo "All services started."

.PHONY: run-all-services run-discovery-server run-data-insertions run-authentication run-authorization run-profile-management-service

docker-compose:
	 docker compose up --build --remove-orphans

docker-test: docker-build
	sudo docker run -it ${DOCKER_IMAGE}

docker-build:
	sudo docker build -t ${DOCKER_IMAGE} .


