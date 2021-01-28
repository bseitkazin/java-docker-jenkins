# java-docker-jenkins
Tutorial how to create ci cd pipline using Docker and Jenkins for the Java app

# Installing Jenkins on Windows 10 using Docker-Desktop

## Create network
`docker network create jenkins`

## Docker in Docker
```docker run --name jenkins-docker --rm --detach ^
  --privileged --network jenkins --network-alias docker ^
  --env DOCKER_TLS_CERTDIR=/certs ^
  --volume jenkins-docker-certs:/certs/client ^
  --volume jenkins-data:/var/jenkins_home ^
  docker:dind
```

## Build it
`docker build -t myjenkins-blueocean:1.1 .`

## Run it
```
docker run --name jenkins-blueocean --rm --detach ^
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 ^
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 ^
  --volume jenkins-data:/var/jenkins_home ^
  --volume jenkins-docker-certs:/certs/client:ro ^
  --volume "%HOMEDRIVE%%HOMEPATH%":/home ^
  --publish 8080:8080 --publish 50000:50000 myjenkins-blueocean:1.1
```

## Check the logs to read master password
`docker logs jenkins-blueocean`

# Jenkins

## Simple pipline
```
pipeline {
	agent any
	stages {
		stage("stage1-build") {
			steps {
				echo 'Hello World from SCM repo!'
			}
		}
	}
}
```

## Multistage Pipeline using Docker images
```
pipeline {
    agent none 
    stages {
        stage('Build') {
            agent { docker 'maven:3.6.3-jdk-11' } 
            steps {
                echo 'Hello, Maven'
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Run') {
            agent { docker 'openjdk:11.0.7-jdk-slim' } 
            steps {
                echo 'Hello, JDK'
                sh 'java -jar target/demodocker-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
```