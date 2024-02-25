FROM openjdk:17-jdk-alpine AS build_image
WORKDIR /app/betsanddice-game
COPY build.gradle /app
COPY /gradle ./gradle
COPY /gradlew .
COPY /build.gradle .
COPY /src ./src
RUN ./gradlew build -x test --continue
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build_image /app/betsanddice-game/build/libs/betsanddice-game-1.0-SNAPSHOT.jar .
EXPOSE 8095
CMD ["java", "-jar", "betsanddice-game-1.0-SNAPSHOT.jar"]

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-game
#COPY ./build.gradle /app
#COPY ./betsanddice-game/gradle ./gradle
#COPY ./betsanddice-game/gradlew .
#COPY ./betsanddice-game/build.gradle .
#COPY ./betsanddice-game/src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-game/build/libs/betsanddice-game-1.0-SNAPSHOT.jar .
#EXPOSE 8095
#CMD ["java", "-jar", "betsanddice-game-1.0-SNAPSHOT.jar"]