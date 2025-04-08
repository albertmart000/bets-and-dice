FROM bellsoft/liberica-openjdk-alpine:21 AS build_image
WORKDIR /app/betsanddice-gateway
COPY src ./src
COPY gradle ./gradle
COPY build.gradle .
COPY gradlew .
RUN ./gradlew build -x test --continue
RUN rm -rf /app/betsanddice-gateway/src /app/betsanddice-gateway/gradle

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY --from=build_image /app/betsanddice-gateway/build/libs/betsanddice-gateway-1.0-SNAPSHOT.jar .
EXPOSE 8072
CMD ["java", "-jar", "betsanddice-gateway-1.0-SNAPSHOT.jar"]