FROM bellsoft/liberica-openjdk-alpine:21 AS build_image
WORKDIR /app/betsanddice-config
COPY src ./src
COPY gradle ./gradle
COPY build.gradle .
COPY gradlew .
RUN ./gradlew build -x test --continue
RUN rm -rf /app/betsanddice-config/src /app/betsanddice-config/gradle

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY --from=build_image /app/betsanddice-config/build/libs/betsanddice-config-1.0-SNAPSHOT.jar .
EXPOSE 8094
CMD ["java", "-jar", "betsanddice-config-1.0-SNAPSHOT.jar"]