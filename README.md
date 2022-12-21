<img src="https://img.shields.io/badge/LOTTO GAME-%20brightgreen" height="50" />

<b>VERSION: 1.0.0<b><br>
<b>COPYRIGHT: ŁUKASZ NOWOGÓRSKI<b><br>
<b>MAIL: nowogorski.lukasz0@gmail.com<b>
  
VIDEO LOTTO
 
[![Watch the video](https://www.galeriatwierdzazamosc.pl/_cache/shops/510-255/fill/lotto.png)](https://github.com/kalqa/LukaszLottoExcelent/blob/master/src/main/resources/Lotto.mp4)
  
![alt text](https://github.com/kalqa/LukaszLottoExcelent/blob/master/src/main/resources/picture/Lotto_architecture2.jpg?raw=true)
![alt text](https://github.com/kalqa/LukaszLottoExcelent/blob/master/src/main/resources/picture/Lotto_architecture.jpg?raw=true)

## SPECIFICATION

- Spring Boot
- Modular monolith hexagonal architecture
- Facade design pattern
- MongoDb database NoSql
- JUnit with Mock and Integration test and Wiremock
- RestApi
- Redis cache for optimized results queries
- Docker generate contener
- Basic html and Javascript file

## Tech STACK PROJECT

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

IN PROGRESS!!!

## INFORMATION ABOUT DOCKER

Install Docker
[Docker Desktop](https://www.docker.com/products/docker-desktop/)

Run Install docker, when Install finished Run application Docker desktop.

Go to Powershell:
1. GO TO PATH FILE : 
cd C:\Users\Łukasz\Desktop\Lotto<br>
2.BUILD IMAGE DOCKER docker:  
build -t Łukasz/lotto/container_lotto:v1 ./<br>
3.Run container
docker run -d --name lotto:v1 -p 80:80 lotto/container_lotto:v1<br>
  
<div style="text-align:center">
  <img src="https://github.com/kalqa/LukaszLottoExcelent/blob/master/src/main/resources/picture/Rest_api.png" width="400" height="150"/>
  <br><br>
  <div>
<ul>
  <li><a href="http://localhost:8080/numbers" target="_blank" title="INPUT NUMBERS FOR USER">INPUT NUMBERS</a></li>
  <li> <a href="http://localhost:8080/users" target="_blank" title="READ ALL USERS LOTTO">READ USERS</a></li>
  <li><a href="http://localhost:8080/winners" target="_blank" title="READ WINNERS LOTTO">WINNERS</a></li>
  <li><a href="http://localhost:8080/winners/{UUID}" target="_blank" title="READ WINNERS BY UUID">WINNERS/{ID}</a>   </li>
</ul>
</div>
