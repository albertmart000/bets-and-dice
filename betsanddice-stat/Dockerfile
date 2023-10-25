FROM openjdk:17-jdk-alpine
WORKDIR /app/
COPY ./build/libs/betsanddice-stat-1.0-SNAPSHOT.jar /app
EXPOSE 8096
ENTRYPOINT ["java", "-jar", "betsanddice-stat-0.0.1-SNAPSHOT.jar"]