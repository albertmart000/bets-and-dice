FROM openjdk:17-jdk-alpine
WORKDIR /app/
COPY ./build/libs/betsanddice-craps-1.0-SNAPSHOT.jar /app
EXPOSE 8095
ENTRYPOINT ["java", "-jar", "betsanddice-craps-0.0.1-SNAPSHOT.jar"]