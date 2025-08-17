import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public interface Calculator extends Remote {
    // interface for the Calculator service
    // the methods using user UUID use an extension of the Remote Exception
    // that handles invalid UUID

    /**
     * to have a separate stack for each client, clients needs to be uniquely identified
     * since there can be multiple clients from the same IP and their source ports are hidden,
     * and source ports aren't shown by RMI
     * thus we need to handle that ourselves
     * create unique ID and map that to the client's stack
     * @return UUID -> clients created UUID
     * @throws RemoteException by RMI
     */
    UUID register() throws RemoteException;

    // using concurrent hashmap to better support concurrency compared to hasmap
    // stores a mapping between each client UUID and its stack of integers
    ConcurrentHashMap<UUID, ArrayList<Integer>> clientStackMap = new ConcurrentHashMap<>();
    // maps operation names ("min", "max", "lcm", "gcd") to their handlers
    ConcurrentHashMap<String, Consumer<UUID>> pushOperations = new ConcurrentHashMap<>();

    /**
     * Push an integer value onto the specified client's stack.
     * @param user -> UUID of client
     * @param val -> value to push (any int)
     * @throws RemoteException
     * @throws InvalidClientException
     */
    void pushValue(UUID user, int val) throws RemoteException, InvalidClientException;

    /**
     * Push an operator onto the stack of the given client.
     * The operator causes the server to pop all values from the client's stack,
     * apply the operation, and push the result back:
     * - "min": pushes the minimum of the popped values
     * - "max": pushes the maximum of the popped values
     * - "lcm": pushes the least common multiple of the popped values
     * - "gcd": pushes the greatest common divisor of the popped values
     * @param user -> client UUID
     * @param operation -> string, one of the mentioned operations
     * @throws RemoteException -> By RMI
     * @throws InvalidClientException -> if UUID is invalid
     */
    void pushOperation(UUID user, String operation) throws RemoteException, InvalidClientException;

    /**
     * Pop and return the top value from the client's stack.
     * @param user -> UUID of the client
     * @return int, value on top of stack
     * @throws RemoteException -> By RMI
     * @throws InvalidClientException -> if UUID is invalid
     */
    int pop(UUID user) throws RemoteException, InvalidClientException;

    /**
     * Check whether the client's stack is empty.
     * @param user -> UUID of the client
     * @return boolean, whether stack is empty or not
     * @throws RemoteException -> By RMI
     * @throws InvalidClientException -> if UUID is invalid
     */
    boolean isEmpty(UUID user) throws RemoteException, InvalidClientException;

    /**
     * Wait for the specified time, then pop and return the top value from the client's stack.
     * @param user -> UUID of client
     * @param millis -> delay in milliseconds (>=0)
     * @return int -> popped value after the delay
     * @throws RemoteException by RMI
     * @throws InvalidClientException if UUID is invalid
     * @throws InterruptedException if sleep is interrupted
     */
    int delayPop(UUID user, int millis) throws RemoteException, InvalidClientException, InterruptedException;
}
