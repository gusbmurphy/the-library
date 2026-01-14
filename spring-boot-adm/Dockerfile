FROM eclipse-temurin:21 AS build

COPY ./gradle ./gradle
COPY ./gradlew* .
COPY ./settings.gradle.kts .
RUN ./gradlew

RUN mkdir /project
WORKDIR /project

COPY . .

RUN ./gradlew clean installDist

FROM eclipse-temurin:21 AS jre

RUN $JAVA_HOME/bin/jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM debian:bookworm-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre /javaruntime $JAVA_HOME

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir /opt/app

COPY --from=build \
      /project/build/install/spring-boot-adm \
      /opt/app/

WORKDIR /opt/app/bin
CMD ["./spring-boot-adm"]
