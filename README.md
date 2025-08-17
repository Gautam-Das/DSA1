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
2. Using maven

    `mvn test-compile`

### Starting the Registry and Server
Once compiled, run the following command
`java -cp <folder> CalculatorServer`
where the folder is the target folder chosen during compilation, if the `javac` command above was used this should be `out`. If `mvn` was used this should be `target/classes`   

This should start registry and server.

### Running Clients(s)
Once the server is running, go to a different terminal and run the command
`java -cp <folder> CalculatorClient`
where the folder is the same as above. This should connect the client to the server and start an interactive session in the terminal to call relavant functions.

To run multiple clients simply run this in multiple different terminals

## Important Info

### How are multiple clients Handled
Each client has their own stack and the clients need to have their own stacks for the bonus marks.

There are a couple of ways to implement multiple clients for RMI server like this. Since there wasn't a defined method, I have chosen to implement it using UUIDs. When a client starts up, they call the `register` method which returns a randomly generated UUID. This UUID must be included in subsequent requests to interact with the stack. This was required since clients with from the same device have the same IP and ports are hidden by RMI. So to differentiate between the clients a unique identifier is needed.

### Why Invalid Client Exception
Since each clients accesses their stack using their UUID, its important to check if its valid, if its not this Exception is raised.

### Testing
In the provided test files. Comprehensive unit testing is done for each method with multiple different inputs and number of clients. However, it should be noted that the assignment clearly states that we can assume operations are sensible, therefore bad inputs (such as pop on empty stack, negative delays) have not been tested.

Along with that,Acceptance and functional testing is done via the client file.