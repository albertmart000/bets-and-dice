FROM bellsoft/liberica-openjdk-alpine:21 AS build_image
WORKDIR /app/betsanddice-eureka
COPY src ./src
COPY gradle ./gradle
COPY build.gradle .
COPY gradlew .
RUN ./gradlew build -x test --continue
RUN rm -rf /app/betsanddice-eureka/src /app/betsanddice-eureka/gradle

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY --from=build_image /app/betsanddice-eureka/build/libs/betsanddice-eureka-1.0-SNAPSHOT.jar .
EXPOSE 8070
CMD ["java", "-jar", "betsanddice-eureka-1.0-SNAPSHOT.jar"]