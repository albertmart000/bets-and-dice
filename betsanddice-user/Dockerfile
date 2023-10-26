#ARG MICRO_NAME=betsanddice-user
#FROM openjdk:17-jdk-alpine AS builder
#ARG MICRO_NAME
#WORKDIR /app/$MICRO_NAME
#COPY ./build.gradle /app
#COPY ./$MICRO_NAME/gradle ./gradle
#COPY ./$MICRO_NAME/gradlew .
#COPY ./$MICRO_NAME/gradle .
#RUN ./gradlew clean package -x test -x main -x spring-boot.repackage && rm -r ./build/
#COPY ./$MICRO_NAME/src ./src
#RUN ./gradlew clean package -x test
#ARG MICRO_NAME
#WORKDIR /app
#RUN mkdir ./logs
#ENV JAR_FILE_NAME=betsanddice-user-1.0-SNAPSHOT.jar
#ARG BUILD_FOLDER=/app/$MICRO_NAME/build/libs
#COPY --from=builder $BUILD_FOLDER/$JAR_FILE_NAME .
#EXPOSE 8094
#CMD ["java", "-jar", "$JAR_FILE_NAME"]


ARG MICRO_NAME=betsanddice-user

FROM gradle:jdk17-alpine AS build-image
ENV APP_HOME=/app/betsanddice-user
WORKDIR $APP_HOME

COPY build.gradle /app
COPY /gradle app/gradle

COPY /build.gradle /app
COPY /gradlew  /app

#COPY /betsanddice-user/gradle ./gradle
#COPY /betsanddice-user/gradlew .
#COPY /betsanddice-user/build.gradle .



#COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

#RUN gradle --no-daemon build

FROM openjdk:17-jdk-alpine AS builder
ENV JAR_FILE_NAME=betsanddice-user-1.0-SNAPSHOT.jar
WORKDIR $APP_HOME
#RUN mkdir ./logs

ARG BUILD_FOLDER=/$APP_HOME/build/libs
COPY --from=build-image $BUILD_FOLDER/$JAR_FILE_NAME /app
EXPOSE 8094
CMD ["java", "-jar", "$JAR_FILE_NAME"]








