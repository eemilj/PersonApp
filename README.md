# PersonApp (Buutti)

Program that is able to filter person objects from a JSON file based on their fields and present the result to the user. The
program is also able to sort the result in ascending or descending order using one of the
fields as a key. It also declares the number of filtered objects.

## Prerequisites

Before you begin, ensure you have met the following requirements:
* You have installed the latest version of [Java JDK](https://www.oracle.com/se/java/technologies/downloads/) (Only tested on 21).
* You have installed [Maven](https://maven.apache.org/). You can check by running `mvn -v` in your terminal to see if Maven is installed and the version.

## Installing

To install, follow these steps:

```
git clone https://github.com/eemilj/PersonApp.git
cd PersonApp
```


## Using PersonApp

To use PersonApp, follow these steps:

1. Open a terminal in the project's root directory.
2. Run the following command to clean the project, compile the source code, and package it into a JAR file:
```
mvn clean package
```
3. After the build completes, you can find the JAR file in the `target` directory. To run your application, use:
```
java -jar target/PersonApp-1.0.jar src/main/resources/examplefiles/input.json
```
All example files are under src/main/resources/examplefiles/ which can be used to test the app

## License

This project uses the following license: [MIT](https://opensource.org/license/mit).
