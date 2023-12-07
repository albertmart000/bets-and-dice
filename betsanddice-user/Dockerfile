FROM openjdk:17-jdk-alpine AS build_image
WORKDIR /app/betsanddice-user
COPY build.gradle /app
COPY /gradle ./gradle
COPY /gradlew .
COPY /build.gradle .
COPY /src ./src
RUN ./gradlew build -x test --continue
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build_image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar .
EXPOSE 8094
CMD ["java", "-jar", "betsanddice-user-1.0-SNAPSHOT.jar"]



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






























#
#FROM gradle:jdk17-alpine AS build_image
#
#
#WORKDIR /app/betsanddice-user
#COPY build.gradle /app
#COPY /gradle app/gradle
#COPY /build.gradle /app
#COPY /gradlew /app
#COPY /src app/src
##RUN ./gradlew build
##RUN ./gradlew build -x test --continue
#
#RUN ./gradlew build || return 0
##COPY . .
##RUN ./gradlew build
#
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#RUN mkdir ./logs
#
##COPY --from=build-image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar .
##COPY --from=build-image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar /app
#
#
#COPY --from=build_image app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar .
#EXPOSE 8094
#CMD ["java", "-jar", "betsanddice-user-1.0-SNAPSHOT.jar"]