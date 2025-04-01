FROM bellsoft/liberica-openjdk-alpine:21 AS build_image
WORKDIR /app/betsanddice-craps
COPY src ./src
COPY gradle ./gradle
COPY build.gradle .
COPY gradlew .
RUN ./gradlew build -x test --continue
RUN rm -rf /app/betsanddice-craps/src /app/betsanddice-craps/gradle

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY --from=build_image /app/betsanddice-craps/build/libs/betsanddice-craps-1.0-SNAPSHOT.jar .
EXPOSE 8095
CMD ["java", "-jar", "betsanddice-craps-1.0-SNAPSHOT.jar"]