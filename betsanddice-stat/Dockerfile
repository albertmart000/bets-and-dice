FROM openjdk:17-jdk-alpine AS build_image
WORKDIR /app/betsanddice-stat
COPY build.gradle /app
COPY /gradle ./gradle
COPY /gradlew .
COPY /build.gradle .
COPY /src ./src
RUN ./gradlew build -x test --continue
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build_image /app/betsanddice-stat/build/libs/betsanddice-stat-1.0-SNAPSHOT.jar .
EXPOSE 8096
CMD ["java", "-jar", "betsanddice-stat-1.0-SNAPSHOT.jar"]

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-stat
#COPY ./build.gradle /app
#COPY ./betsanddice-stat/gradle ./gradle
#COPY ./betsanddice-stat/gradlew .
#COPY ./betsanddice-stat/build.gradle .
#COPY ./betsanddice-stat/src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-stat/build/libs/betsanddice-stat-1.0-SNAPSHOT.jar .
#EXPOSE 8096
#CMD ["java", "-jar", "betsanddice-stat-1.0-SNAPSHOT.jar"]
