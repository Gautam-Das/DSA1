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
    // Inputs:  (none)
    // Outputs: UUID  -> unique identifier for the client.
    UUID register() throws RemoteException;

    // stores a mapping between each client UUID and its stack of integers
    HashMap<UUID, ArrayList<Integer>> clientStackMap = new HashMap<>();
    // maps operation names ("min", "max", "lcm", "gcd") to their handlers
    HashMap<String, Consumer<UUID>> pushOperations = new HashMap<>();

    /**
     * Push an integer value onto the specified client's stack.
     * Inputs:  user -> UUID of the client; val -> int value to push (any int, positive/negative/zero allowed).
     * Outputs: void
     * Errors:  InvalidClientException if UUID not registered; RemoteException
     */
    void pushValue(UUID user, int val) throws RemoteException, InvalidClientException;

    /**
    * Push an operator onto the stack of the given client.
    * The operator causes the server to pop all values from the client's stack,
    * apply the operation, and push the result back:
    *  - "min": pushes the minimum of the popped values
    *  - "max": pushes the maximum of the popped values
    *  - "lcm": pushes the least common multiple of the popped values
    *  - "gcd": pushes the greatest common divisor of the popped values
     *  Inputs:  user -> UUID of the client; operation -> one of the strings mentioned above.
     * Outputs: void
     * Errors:  InvalidClientException if UUID not registered; RemoteException
     */
    void pushOperation(UUID user, String operation) throws RemoteException, InvalidClientException;

    /**
     * Pop and return the top value from the client's stack.
     * Inputs:  user -> UUID of the client.
     * Outputs: int   -> the popped value.
     * Errors:  InvalidClientException if UUID not registered; RemoteException.
     * Notes:   Should throw (e.g., NoSuchElementException) in implementation if the stack is empty.
     */
    int pop(UUID user) throws RemoteException, InvalidClientException;

    /**
     * Check whether the client's stack is empty.
     * Inputs:  user -> UUID of the client.
     * Outputs: boolean -> true if the stack is empty; false otherwise.
     * Errors:  InvalidClientException if UUID not registered; RemoteException.
     */
    boolean isEmpty(UUID user) throws RemoteException, InvalidClientException;

    /**
     * Wait for the specified time, then pop and return the top value from the client's stack.
     * Inputs:  user   -> UUID of the client;
     *          millis -> delay in milliseconds (>= 0).
     * Outputs: int    -> the popped value after the delay.
     * Errors:  InvalidClientException if UUID not registered; RemoteException; InterruptedException if the sleep is interrupted.
     * Notes:   Stack can be modified by other calls during the delay; the value returned is whatever is on top at pop time.
     */
    int delayPop(UUID user, int millis) throws RemoteException, InvalidClientException, InterruptedException;
}
