FROM bellsoft/liberica-openjdk-alpine:17.0.1
RUN apk add -U consul
ADD ["build/libs/betsanddice-craps-1.0-SNAPSHOT.jar", "/opt/betsanddice-craps/"]
ENTRYPOINT ["java", "-jar", "/opt/betsanddice-craps/betsanddice-craps-1.0-SNAPSHOT.jar"]
EXPOSE 7777

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-craps
#COPY build.gradle /app
#COPY /gradle ./gradle
#COPY /gradlew .
#COPY /build.gradle .
#COPY /src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-craps/build/libs/betsanddice-craps-1.0-SNAPSHOT.jar .
#EXPOSE 8095
#CMD ["java", "-jar", "betsanddice-craps-1.0-SNAPSHOT.jar"]

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-craps
#COPY ./build.gradle /app
#COPY ./betsanddice-craps/gradle ./gradle
#COPY ./betsanddice-craps/gradlew .
#COPY ./betsanddice-craps/build.gradle .
#COPY ./betsanddice-craps/src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-craps/build/libs/betsanddice-craps-1.0-SNAPSHOT.jar .
#EXPOSE 8095
#CMD ["java", "-jar", "betsanddice-craps-1.0-SNAPSHOT.jar"]