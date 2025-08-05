import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Calculator extends Remote {

    ArrayList<Integer> stack = new ArrayList<Integer>();
    HashMap<String, Runnable> pushOperations = new HashMap<String, Runnable>();

    void pushValue(int val) throws RemoteException;
    void pushOperation(String operation) throws RemoteException;
    int pop() throws RemoteException;
    boolean isEmpty() throws RemoteException;
    int delayPop(int millis) throws RemoteException, InterruptedException;
}
