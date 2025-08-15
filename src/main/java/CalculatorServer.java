import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class CalculatorServer {
    static String ServerName = "main.java.Calculator Service";

    public static void main(String[] args){
        try {
            Calculator server = new CalculatorImplementation();
            Calculator stub = (Calculator) UnicastRemoteObject
                    .exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(ServerName, stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
