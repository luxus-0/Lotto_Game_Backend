FROM openjdk:19-jdk-alpine
ADD target/Lotto_Game-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar Lotto_Game-1.0-SNAPSHOT.jar