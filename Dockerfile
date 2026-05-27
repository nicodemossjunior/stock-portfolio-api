FROM eclipse-temurin:11-jre

WORKDIR /app

COPY target/stock-portfolio-api-0.0.1-SNAPSHOT.jar app.jar

ENV SERVER_PORT=8080
ENV BRAPI_URL=https://brapi.dev
ENV BRAPI_TOKEN=
ENV JAVA_OPTS=

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
