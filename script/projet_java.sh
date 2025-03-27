#!/bin/bash

# Répertoire source et destination pour la compilation Java
repertoire_source="../sae-java/src"
repertoire_destination="../sae-java/bin"
repertoire_lib="../sae-java/lib/junit-4.13.2.jar"

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
    java -cp "$repertoire_destination" "$classe_executable"
fi