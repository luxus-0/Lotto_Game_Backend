FROM openjdk:23-slim
ADD target/Lotto_Game.jar .
EXPOSE 8080
CMD java -jar Lotto_Game.jar