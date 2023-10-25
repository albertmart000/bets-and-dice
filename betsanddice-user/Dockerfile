FROM openjdk:17-jdk-alpine
WORKDIR /app/
COPY ./build/libs/betsanddice-user-1.0-SNAPSHOT.jar /app
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "betsanddice-user-0.0.1-SNAPSHOT.jar"]