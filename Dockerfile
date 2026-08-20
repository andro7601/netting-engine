FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw  ./
COPY pom.xml pom.xml
RUN chmod +x mvnw
RUN --mount=type=cache,target=/app/.m2 \
    ./mvnw dependency:go-offline -Dmaven.repo.local=/app/.m2
COPY src/ src/
RUN --mount=type=cache,target=/app/.m2 \
    ./mvnw package -DskipTests -Dmaven.repo.local=/app/.m2

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /app/target/netting-engine-0.0.1-SNAPSHOT.jar app.jar
RUN cat > entrypoint.sh <<'EOF'
#!/bin/sh
set -e
exec java \
  -Dspring.datasource.password="$(cat /run/secrets/POSTGRES_PASSWORD)" \
  -jar app.jar
EOF
RUN chmod +x entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["./entrypoint.sh"]
