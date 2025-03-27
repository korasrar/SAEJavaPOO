#!/bin/bash

# Répertoire source et destination pour la compilation Java
repertoire_source="../sae-java/src"
repertoire_destination="../sae-java/bin"
repertoire_lib_javac="../sae-java/lib/junit-4.13.2.jar"
repertoire_lib_java="../sae-java/bin:../sae-java/lib/junit-4.13.2.jar:../sae-java/lib/hamcrest-2.2.jar"

# Compilation des fichiers Java
echo "Compilation des fichiers Java..."
javac -d "$repertoire_destination" -cp "$repertoire_lib" "$repertoire_source"/*.java
echo "Compilation terminée."

# Recherche de la classe exécutable
classe_executable="$1"
if [ -z "$classe_executable" ]; then
    echo "Aucune classe exécutable donnée en argument."
else
    # Lancement du programme Java
    echo "Lancement du programme Java..."
    java -cp "$repertoire_lib_java" org.junit.runner.JUnitCore "$classe_executable"
fi