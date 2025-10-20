FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/Worldwide-Windsurfer-s-Weather-Service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]