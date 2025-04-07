FROM bellsoft/liberica-openjdk-alpine:21 AS build_image
WORKDIR /app/betsanddice-user
COPY src ./src
COPY gradle ./gradle
COPY build.gradle .
COPY gradlew .
RUN ./gradlew build -x test --continue
RUN rm -rf /app/betsanddice-user/src /app/betsanddice-user/gradle

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY --from=build_image /app/betsanddice-user/build/libs/betsanddice-user-1.0-SNAPSHOT.jar .
EXPOSE 8094
CMD ["java", "-jar", "betsanddice-user-1.0-SNAPSHOT.jar"]