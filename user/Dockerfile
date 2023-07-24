FROM openjdk:17-slim AS build

WORKDIR /workspace/app

COPY gradle.properties .
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

#RUN ./gradlew clean build -x test

COPY src src

RUN ./gradlew build -x test

FROM openjdk:17-slim

COPY --from=build /workspace/app/build/libs/*.jar app.jar

ENV PORT=80
EXPOSE 80

ENTRYPOINT ["java","-jar","app.jar"]
