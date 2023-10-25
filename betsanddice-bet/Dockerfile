FROM openjdk:17-jdk-alpine
WORKDIR /app/
COPY ./build/libs/betsanddice-bet-1.0-SNAPSHOT.jar /app
EXPOSE 8097
ENTRYPOINT ["java", "-jar", "betsanddice-bet-0.0.1-SNAPSHOT.jar"]