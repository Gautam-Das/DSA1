# DSA - Assignment 1
### Project Structure
```
│   pom.xml (dependencies)
│   README.md 
├───src
│   ├───main
│   │   └───java
│   │           Calculator.java (Calculator Service Interface)
│   │           CalculatorClient.java (Client that calls the Calculator server)
│   │           CalculatorImplementation.java (Calculator Service Implementation)
│   │           CalculatorServer.java (Server hosting the Calculator service)
│   │           InvalidClientException.java (Error when invalid Client UUID is provided)
│   └───test
│       └───java
│               CalculatorTest.java (Test cases for the Calculator Service)
│
```
## Running the Project
### Compiling
To compile the Java files there are two options:
1. `javac` (doesn't include the dependencies required for Testing)
    
    Windows: `javac -d out -cp "out;src/main/java" src/main/java/*.java`

    Linux: `javac -d out -cp "out:src/main/java" src/main/java/*.java`
2. `mvn`

    `mvn test-compile`

### Starting the Registry and Server
Once compiled, run the following command
`java -cp <folder> CalculatorServer`
where the folder is the target folder chosen during compilation, if the `javac` command above was used this should be `out`. If `mvn` was used this should be `target/classes`

### Running Clients(s)
Once the server is running, go to a different terminal and run the command
`java -cp <folder> CalculatorClient`
where the folder is the same as above. In this case the Client has some test code.

#### TODO

- Why Invalid Client Exception
- Types of tests handled
- Implementation is based on different stack for each client