import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.UUID;

public class CalculatorClient {
    static String ServerName = CalculatorServer.ServerName;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            Calculator server = (Calculator) registry.lookup(ServerName);

            // register and get UUID
            UUID id = server.register();

            // Start scanning user input
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter commands (number, pop, lcm, min, max, gcd, isEmpty, delayPop <ms>, exit):");

            while (true) {
                String line = scanner.nextLine().trim();

                if (line.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                try {
                    // If input is a number
                    // -? : optional leading -
                    // \\d+ : one of more digits
                    if (line.matches("-?\\d+")) {
                        int value = Integer.parseInt(line);
                        server.pushValue(id, value);
                        System.out.println("Pushed: " + value);
                    }
                    // If input is pop
                    else if (line.equalsIgnoreCase("pop")) {
                        int val = server.pop(id);
                        System.out.println("Popped: " + val);
                    }
                    // If input is one of the operations
                    else if (line.equalsIgnoreCase("lcm") ||
                            line.equalsIgnoreCase("min") ||
                            line.equalsIgnoreCase("max") ||
                            line.equalsIgnoreCase("gcd")) {
                        server.pushOperation(id, line.toLowerCase());
                        System.out.println("Applied operation: " + line);
                    }
                    // If input is isEmpty
                    else if (line.equalsIgnoreCase("isEmpty")) {
                        boolean empty = server.isEmpty(id);
                        System.out.println("Stack empty? " + empty);
                    }
                    // If input is delayPop <number>
                    else if (line.toLowerCase().startsWith("delaypop")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length == 2 && parts[1].matches("-?\\d+")) {
                            int millis = Integer.parseInt(parts[1]);
                            int val = server.delayPop(id, millis);
                            System.out.println("DelayPop result: " + val);
                        } else {
                            System.out.println("Usage: delayPop <millis>");
                        }
                    }
                    // Unknown command
                    else {
                        System.out.println("Unknown command: " + line);
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
