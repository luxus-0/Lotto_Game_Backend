FROM openjdk:20-jdk-slim
ADD target/Lotto_Game-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar Lotto_Game-1.0-SNAPSHOT.jar