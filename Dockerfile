FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY pom.xml .
COPY src src
COPY .mvn .mvn

RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

RUN apt update && apt install -y curl

ENV PORT=8080
EXPOSE ${PORT}

ENV PUBLISH_URL=https://publishing-production-d35a.up.railway.app/notice
ENV NOTICEBOARD_DB_HOST=localhost NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_USERNAME="" NOTICEBOARD_DB_PASSWORD=""

HEALTHCHECK --interval=60s --timeout=30s --start-period=120s --retries=3 \
    CMD curl -f -s http://localhost:${PORT}/status || exit 1

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar

