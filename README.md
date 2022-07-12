[![API](https://img.shields.io/badge/Java-8-brown.svg?style=flat)](https://www.oracle.com/java/technologies/java8.html)
[![API](https://img.shields.io/badge/Spring-2.6.9-green.svg?style=flat)](https://spring.io/)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e74fee0edb914eb5ba999080adba97bf)](https://www.codacy.com/gh/LedioPapa/search-engine/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LedioPapa/search-engine&amp;utm_campaign=Badge_Grade)
# Scalable document search engine using Spring & Elasticsearch 
## App Architecture
This is a simple CLI application that saves a document comprising an id and a list of tokens, as well as query said documents in a timely manner.

![Architecture Diagram](src/main/resources/architecture.png)

This solution is based on scalability and expandability. In the Docker container there are 4 main parts: 
Two Elasticsearch instances, a load balancer to distribute the load evenly between them and finally 
a spring boot application containing the CLI needed to interact wit the application. Ideally we would need another 
entrypoint, communicating with the app via REST or messaging.
## Requirements
JDK 8+

Apache Maven 3+

Docker

Docker Compose
## Running the Application

Before running anything, you might need to increase the max vm memory size in docker. To do this in windows run the following commands:
```
wsl -d docker-desktop
```
and then: 
```
sysctl -w vm.max_map_count=262144
```

Navigate to the root folder of the project and run the following commands:
Start a cluster of three ElasticSearch nodes with Docker Compose
```
docker-compose up -d
```
Create the application image using Docker Build
```
docker build -t search-engine-app .
```
Start the application using Docker Run
```
docker run --rm -it --network search-engine_elastic search-engine-app
```
