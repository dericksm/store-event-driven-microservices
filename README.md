# spring-cloud-stream-kafka-event-driven-microservices

The goal of this project is to implement a Microservices Architecture(if we could even say that this project has an architecture) that produces messages to Kafka topics
using `Spring Cloud Stream`.  
The messages are being send to the topics through `Stream Bridge` and the consumers are directly mapped with `Apache Kafka Binder`.  
This is also my first project using `Spring Webflux` so a lot of things might be strange.  

## 
## Requisites

- [`Java 11+`](https://www.oracle.com/java/technologies/downloads/#java11)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)


## Start Kafka

 Open the project root folder and run:
  ```
  docker-compose up -d
  ```
