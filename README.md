LOTTO GAME

<img scr = "https://github.com/kalqa/LukaszLottoExcelent/blob/master/architecture/Lotto%20architecture%20-%20%C5%81ukasz%20Nowog%C3%B3rski%20(1).jpg" alt = "Wymagania lotto">
I create this project because I like coding and learn new think.

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
![image](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=whitee) &nbsp;

Deployed on:<br>
![image](https://img.shields.io/badge/Amazon_AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) &nbsp;

## C3 diagram

The C3 diagram blow presents main application components and module dependencies. <br>
Click on image below to review it more readable size.

<a href="https://raw.githubusercontent.com/pjazdzyk/lottery-web/master/architecture/C3_Architecture.png"><img src="architecture/C3_Architecture.png" width="850"/><br></a>

## Installation and run

Lotto|Web requires [Docker](https://www.docker.com/products/docker-desktop/) to run.
Both Winning Numbers microservice and main application (Lottery|Web) are pushed as an images into the Docker Hub.
Docker-compose file **[docker-compose](https://github.com/pjazdzyk/lottery-web/blob/master/src/docker/docker-compose.yml)** 

RUN IN Powershell or IntelliJ command -> docker compose-up
After that build a container docker.

## Rest-API Endpoints

We have two endpoints: 
1.Input numbers from user
2.Result lotto search by UUID

GO TO PATH WITH REST API -> https://github.com/kalqa/LukaszLottoExcelent/tree/master/src/main/java/pl/lotto/infrastructure/api
