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
    
    Windows: `javac -d out -cp "out;src/test/java" src/test/java/*.java`

    Linux: `javac -d out -cp "out:src/test/java" src/test/java/*.java`
2. `mvn` 

    `mvn compile`

### Starting the Registry


#### TODO

- Why Invalid Client Exception
- Types of tests handled
- Implementation is based on different stack for each client