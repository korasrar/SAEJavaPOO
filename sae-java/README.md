## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Important command !

javac -cp sae-java/lib:junit-4.13.2.jar -d sae-java/bin sae-java/src/*

java -cp sae-java/lib:hamcrest-2.2.jar:junit-4.13.2.jar org.junit.runner.JUnitCore
*NomDuFichier*Test  
