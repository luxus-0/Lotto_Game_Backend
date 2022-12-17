<<<<<<< HEAD
FROM openjdk:18
ADD target/LukaszLottoExcelent-1.0-SNAPSHOT.jar .
EXPOSE 9090
=======
FROM openjdk:18.0.1
ADD target/LukaszLottoExcelent-1.0-SNAPSHOT.jar .
EXPOSE 8000
>>>>>>> origin/master
CMD java -jar LukaszLottoExcelent-1.0-SNAPSHOT.jar