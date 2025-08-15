import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public interface Calculator extends Remote {

    // to have a separate stack for each client, clients needs to be uniquely identified
    // since there can be multiple clients from the same IP and their source ports are hidden,
    // and source ports aren't shown by RMI
    // thus we need to handle that ourselves
    // create unique ID and map that to the client's stack
    UUID register() throws RemoteException;

    HashMap<UUID, ArrayList<Integer>> clientStackMap = new HashMap<>();
    HashMap<String, Consumer<UUID>> pushOperations = new HashMap<>();

    void pushValue(UUID user, int val) throws RemoteException;
    void pushOperation(UUID user, String operation) throws RemoteException;
    int pop(UUID user) throws RemoteException;
    boolean isEmpty(UUID user) throws RemoteException;
    int delayPop(UUID user, int millis) throws RemoteException, InterruptedException;
}
