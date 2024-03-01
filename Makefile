DOCKER_IMAGE := .

test:
	mvn test

site:
	mvn site

clean:
	mvn checkstyle:checkstyle

docs:
	mvn javadoc:javadoc

docker-compose:
	 docker compose up --build --remove-orphans

docker-test: docker-build
	sudo docker run -it ${DOCKER_IMAGE}

docker-build:
	sudo docker build -t ${DOCKER_IMAGE} .


