FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY pom.xml .
COPY src src
COPY .mvn .mvn

RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/noticeboardapp-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
EXPOSE ${PORT}

ENV SERVER_NAME=http://localhost:8080
ENV NOTICEBOARD_DB_HOST=localhost NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_USERNAME="" NOTICEBOARD_DB_PASSWORD=""

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar

