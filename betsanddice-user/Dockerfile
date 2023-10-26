FROM gradle:jdk17-alpine AS build-image
ENV APP_HOME=/app/betsanddice-user
WORKDIR $APP_HOME
COPY build.gradle /app
COPY /gradle app/gradle
COPY /build.gradle /app
COPY /gradlew  /app
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build
FROM openjdk:17-jdk-alpine AS builder
WORKDIR $APP_HOME
RUN mkdir ./logs
COPY --from=build-image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar /app
EXPOSE 8094
CMD ["java", "-jar", "$betsanddice-user-1.0-SNAPSHOT.jar"]








