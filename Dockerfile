FROM openjdk:23-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install libgtk-3-0 libglu1-mesa -y && apt-get update

COPY sae-java/target/sae-java-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY javafx/lib ./javafx/

ENV DISPLAY=host.docker.internal:0.0

CMD ["java","-Dprism.order=sw", "--module-path", "javafx", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]


# docker-compose run -e DISPLAY=192.168.183.173:0.0 --rm app