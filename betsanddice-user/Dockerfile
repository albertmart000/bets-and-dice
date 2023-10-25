ARG MICRO_NAME=betsanddice-user
FROM gradle:jdk17-alpine AS build-image
ARG MICRO_NAME
WORKDIR /app/$MICRO_NAME
COPY --chown=gradle:gradle build.gradle settings.gradle /app/$MICRO_NAME
COPY --chown=gradle:gradle src /app/$MICRO_NAME/src
COPY --chown=gradle:gradle config /app/$MICRO_NAME/config
RUN gradle --no-daemon build
FROM openjdk:17-jdk-alpine AS builder
RUN mkdir ./logs
ARG BUILD_FOLDER=/app/$MICRO_NAME/build/libs
COPY --from=build-image $BUILD_FOLDER/betsanddice-user-1.0-SNAPSHOT.jar /app
EXPOSE 8094
CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]




