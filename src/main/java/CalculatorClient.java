import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class CalculatorClient {
    static String ServerName = CalculatorServer.ServerName;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            Calculator server = (Calculator) registry.lookup(ServerName);

            // register and get UUID
            UUID id = server.register();
            // empty stack
            System.out.println(server.isEmpty(id)); // should be true

            // add 1,2,3
            server.pushValue(id, 1);
            server.pushValue(id, 2);
            server.pushValue(id, 3);

            System.out.println(server.isEmpty(id)); // should be false
            System.out.println(server.pop(id)); // should be 3
            System.out.println(server.isEmpty(id)); // should be false

            server.pushValue(id, 3);

            // calculate LCM
            server.pushOperation(id,"lcm");
            System.out.println(server.pop(id)); // should be 6
            System.out.println(server.isEmpty(id)); // should be true

            // calculate GCD
            server.pushValue(id, 10);
            server.pushValue(id, 30);
            server.pushValue(id, 20);

            server.pushOperation(id,"gcd");
            System.out.println(server.pop(id)); // should be 10
            System.out.println(server.isEmpty(id)); // should be true

            // calculate min
            server.pushValue(id, 100);
            server.pushValue(id, -100);
            server.pushValue(id, 0);

            server.pushOperation(id,"min");
            System.out.println(server.pop(id)); // should be -100
            System.out.println(server.isEmpty(id)); // should be true

            // calculate max
            server.pushValue(id, 100);
            server.pushValue(id, -100);
            server.pushValue(id, 0);

            server.pushOperation(id,"max");
            System.out.println(server.pop(id)); // should be 100
            System.out.println(server.isEmpty(id)); // should be true

            // delay pop
            server.pushValue(id, 0);
            System.out.println(server.isEmpty(id)); // should be false
            System.out.println(server.delayPop(id,2000)); // should be 0
            System.out.println(server.isEmpty(id)); // should be true

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
