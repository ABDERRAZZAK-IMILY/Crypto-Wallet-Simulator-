FROM openjdk:8-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /app/bin /app/lib /app/logs

COPY bin/ /app/bin/

COPY postgresql-42.7.8.jar /app/lib/

COPY src/ /app/src/

ENV CLASSPATH=/app/bin:/app/lib/*

ENV DB_URL=jdbc:postgresql://db:5432/crepto
ENV DB_USER=postgres
ENV DB_PASSWORD=202580

VOLUME ["/app/logs", "/app/src"]

CMD ["java", "com.ui.Main"]
