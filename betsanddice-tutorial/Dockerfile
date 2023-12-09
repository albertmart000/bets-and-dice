FROM openjdk:17-jdk-alpine AS build_image
WORKDIR /app/betsanddice-tutorial
COPY build.gradle /app
COPY /gradle ./gradle
COPY /gradlew .
COPY /build.gradle .
COPY /src ./src
RUN ./gradlew build -x test --continue
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build_image /app/betsanddice-tutorial/build/libs/betsanddice-tutorial-1.0-SNAPSHOT.jar .
EXPOSE 8098
CMD ["java", "-jar", "betsanddice-tutorial-1.0-SNAPSHOT.jar"]

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-tutorial
#COPY ./build.gradle /app
#COPY ./betsanddice-tutorial/gradle ./gradle
#COPY ./betsanddice-tutorial/gradlew .
#COPY ./betsanddice-tutorial/build.gradle .
#COPY ./betsanddice-tutorial/src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-tutorial/build/libs/b
#EXPOSE 8098
#CMD ["java", "-jar", "betsanddice-tutorial-1.0-SNAPSHOT.jar"]