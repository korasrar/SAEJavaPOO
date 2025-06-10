FROM openjdk:17-jdk-slim

WORKDIR /app

COPY sae-java/target/sae-java-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

CMD ["java", "-jar", "app.jar"]