FROM openjdk:18
ADD target/Game_Lotto-v1.0.jar .
EXPOSE 8080
CMD java -jar Game_Lotto-v1.0.jar