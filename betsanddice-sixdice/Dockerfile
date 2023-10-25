FROM openjdk:17-jdk-alpine
WORKDIR /app/
COPY ./build/libs/betsanddice-sixdice-1.0-SNAPSHOT.jar /app
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "betsanddice-sixdice-0.0.1-SNAPSHOT.jar"]