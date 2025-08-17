import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public interface Calculator extends Remote {
    // interface for the Calculator service
    // the methods using user UUID use an extension of the Remote Exception
    // that handles invalid UUID

    // to have a separate stack for each client, clients needs to be uniquely identified
    // since there can be multiple clients from the same IP and their source ports are hidden,
    // and source ports aren't shown by RMI
    // thus we need to handle that ourselves
    // create unique ID and map that to the client's stack
    UUID register() throws RemoteException;

    // stores a mapping between each client UUID and its stack of integers
    HashMap<UUID, ArrayList<Integer>> clientStackMap = new HashMap<>();
    // maps operation names ("min", "max", "lcm", "gcd") to their handlers
    HashMap<String, Consumer<UUID>> pushOperations = new HashMap<>();

    // Push an integer value onto the stack of the given client.
    void pushValue(UUID user, int val) throws RemoteException, InvalidClientException;

    // Push an operator onto the stack of the given client.
    // The operator causes the server to pop all values from the client's stack,
    // apply the operation, and push the result back:
    //  - "min": pushes the minimum of the popped values
    //  - "max": pushes the maximum of the popped values
    //  - "lcm": pushes the least common multiple of the popped values
    //  - "gcd": pushes the greatest common divisor of the popped values
    void pushOperation(UUID user, String operation) throws RemoteException, InvalidClientException;

    // Pop and return the top value from the client's stack.
    int pop(UUID user) throws RemoteException, InvalidClientException;

    // Check whether the client's stack is empty.
    // Returns true if empty, false otherwise.
    boolean isEmpty(UUID user) throws RemoteException, InvalidClientException;

    // Wait for the given number of milliseconds, then pop and return
    // the top value from the client's stack.
    // Stack can be modified during this time
    int delayPop(UUID user, int millis) throws RemoteException, InvalidClientException, InterruptedException;
}
