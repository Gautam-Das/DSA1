import javax.sound.midi.SysexMessage;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    static String ServerName = CalculatorServer.ServerName;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            Calculator server = (Calculator) registry.lookup(ServerName);

            // empty stack
            System.out.println(server.isEmpty()); // should be true

            // add 1,2,3
            server.pushValue(1);
            server.pushValue(2);
            server.pushValue(3);

            System.out.println(server.isEmpty()); // should be false
            System.out.println(server.pop()); // should be 3
            System.out.println(server.isEmpty()); // should be false

            server.pushValue(3);

            // calculate LCM
            server.pushOperation("lcm");
            System.out.println(server.pop()); // should be 6
            System.out.println(server.isEmpty()); // should be true

            // calculate GCD
            server.pushValue(10);
            server.pushValue(30);
            server.pushValue(20);

            server.pushOperation("gcd");
            System.out.println(server.pop()); // should be 10
            System.out.println(server.isEmpty()); // should be true

            // calculate min
            server.pushValue(100);
            server.pushValue(-100);
            server.pushValue(0);

            server.pushOperation("min");
            System.out.println(server.pop()); // should be -100
            System.out.println(server.isEmpty()); // should be true

            // calculate max
            server.pushValue(100);
            server.pushValue(-100);
            server.pushValue(0);

            server.pushOperation("max");
            System.out.println(server.pop()); // should be 100
            System.out.println(server.isEmpty()); // should be true

            // delay pop
            server.pushValue(0);
            System.out.println(server.isEmpty()); // should be false
            System.out.println(server.delayPop(2000));
            System.out.println(server.isEmpty()); // should be true

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
