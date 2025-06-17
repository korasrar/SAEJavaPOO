FROM openjdk:23

WORKDIR /app

COPY sae-java/target/sae-java-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY javafx/ ./javafx/

CMD ["java", "--module-path", "javafx", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]