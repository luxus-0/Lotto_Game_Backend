LOTTO GAME BACKEND WITH FRONTEND

<img scr = "https://github.com/kalqa/LukaszLottoExcelent/blob/master/src/main/resources/Lotto_architecture.jpg">
<img scr = "https://github.com/kalqa/LukaszLottoExcelent/blob/master/src/main/resources/Lotto_architecture2.jpg">


> VERSION: 1.0.0 <br>
> AUTHOR: ŁUKASZ NOWOGÓRSKI <br>
<b>MAIL: nowogorski.lukasz0@gmail.com<b>

## Specification

- Spring Boot
- Modular monolith hexagonal architecture
- Facade design pattern
- MongoDb database NoSql
- JUnit with Mock and Integration test and Wiremock
- RestApi
- Redis cache for optimized results queries
- Docker generate contener
- Basic html and Javascript file

## Tech

Lotto|Web is developed using following technologies: <br>

Core: <br>
![image](https://img.shields.io/badge/17-Java-orange?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring) &nbsp;
![image](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white) &nbsp;

Testing:<br>
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/Testcontainers-9B489A?style=for-the-badge) &nbsp;

Front:<br>
![image](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white) &nbsp;
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) &nbsp;

## C3 diagram

NOT FINISHED YET

## Installation and run
[Docker](https://www.docker.com/products/docker-desktop/) to run.
  
-Dockerfile **[docker-file](https://github.com/kalqa/LukaszLottoExcelent/blob/master/Dockerfile/DockerFile)** 
 Run DockerFile generate Jar file.

-Docker-compose **[docker-compose](https://github.com/kalqa/LukaszLottoExcelent/blob/master/docker-compose.yml)** 
 RUN IN Powershell or IntelliJ command -> docker compose-up
  

After that build a container docker.

## Rest-API Endpoints

We have two endpoints: 
1.Input numbers from user
2.Result lotto search by UUID

GO TO PATH WITH REST API -> https://github.com/kalqa/LukaszLottoExcelent/tree/master/src/main/java/pl/lotto/infrastructure/api
