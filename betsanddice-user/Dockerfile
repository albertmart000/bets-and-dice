FROM bellsoft/liberica-openjdk-alpine:17.0.1
RUN apk add -U consul
ADD ["build/libs/betsanddice-user-1.0-SNAPSHOT.jar", "/opt/betsanddice-user/"]
ENTRYPOINT ["java", "-jar", "/opt/betsanddice-user/betsanddice-user-1.0-SNAPSHOT.jar"]
EXPOSE 7777

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-user
#COPY build.gradle /app
#COPY /gradle ./gradle
#COPY /gradlew .
#COPY /build.gradle .
#COPY /src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar .
#EXPOSE 8094
#CMD ["java", "-jar", "betsanddice-user-1.0-SNAPSHOT.jar"]

#FROM openjdk:17-jdk-alpine AS build_image
#WORKDIR /app/betsanddice-user
#COPY ./build.gradle /app
#COPY ./betsanddice-user/gradle ./gradle
#COPY ./betsanddice-user/gradlew .
#COPY ./betsanddice-user/build.gradle .
#COPY ./betsanddice-user/src ./src
#RUN ./gradlew build -x test --continue
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build_image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar .
#EXPOSE 8094
#CMD ["java", "-jar", "betsanddice-user-1.0-SNAPSHOT.jar"]