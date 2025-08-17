import java.rmi.RemoteException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.UUID;

public class CalculatorImplementation implements Calculator {
    // verify that the UUID is valid
    private void validateUser(UUID user) throws InvalidClientException {
        if (!clientStackMap.containsKey(user)) {
            throw new InvalidClientException(user);
        }
    }
    // ---------- Helper Methods for Stack Operations ----------

    // Replace the user's stack with the minimum of all current values
    private void stackMin(UUID user) {
        ArrayList<Integer> userStack = clientStackMap.get(user);
        int minimum = Collections.min(userStack);
        userStack.clear();
        userStack.add(minimum);
    }

    // Replace the user's stack with the maximum of all current values
    private void stackMax(UUID user) {
        ArrayList<Integer> userStack = clientStackMap.get(user);

        int maximum = Collections.max(userStack);
        userStack.clear();
        userStack.add(maximum);
    }

    // Compute the greatest common divisor using Euclidean algorithm
    // return positive mod by convention
    private int gcd(int a, int b){
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    // Compute the least common multiple
    private int lcm(int a, int b){
        if (a == 0 || b == 0) {
            return 0; // LCM involving zero is typically defined as zero
        }
        System.out.println(a);
        System.out.println(b);
        System.out.println(gcd(a,b));
        return Math.abs(a * b) / gcd(a, b);
    }

    // Replace the user's stack with the LCM of all current values
    private void stackLCM(UUID user) {
        ArrayList<Integer> userStack = clientStackMap.get(user);

        // iteratively find LCM
        int result = userStack.get(0);
        for (int i = 1; i < userStack.size(); i++){
            result = lcm(result, userStack.get(i));
        }

        // empty the list
        userStack.clear();

        // add lcm
        userStack.add(result);
    }

    // Replace the user's stack with the GCD of all current values
    private void stackGCD(UUID user) {
        ArrayList<Integer> userStack = clientStackMap.get(user);

        // iteratively find GCD
        int result = userStack.get(0);
        for (int i = 1; i < userStack.size(); i++){
            result = gcd(result, userStack.get(i));
        }

        // empty the list
        userStack.clear();

        // add gcd
        userStack.add(result);
    }

    // ---------- Constructor ----------
    public CalculatorImplementation() throws RemoteException {
        pushOperations.put("min", this::stackMin);
        pushOperations.put("max", this::stackMax);
        pushOperations.put("lcm", this::stackLCM);
        pushOperations.put("gcd", this::stackGCD);
    }

    // ---------- Public Methods -------
    @Override
    public UUID register(){
        // Generate a unique client ID and create a new stack for this client
        UUID id = UUID.randomUUID();
        clientStackMap.put(id, new ArrayList<>());
        return id;
    }

    @Override
    public void pushValue(UUID user, int val) throws InvalidClientException {
        validateUser(user);
        clientStackMap.get(user).add(val);
    }

    @Override
    public void pushOperation(UUID user, String operation) throws InvalidClientException {
        validateUser(user);
        pushOperations.get(operation).accept(user);
    }

    @Override
    public int pop(UUID user) throws InvalidClientException {
        validateUser(user);
        return clientStackMap.get(user).remove(clientStackMap.get(user).size() - 1);
    }

    @Override
    public boolean isEmpty(UUID user) throws InvalidClientException {
        validateUser(user);
        return clientStackMap.get(user).isEmpty();
    }

    @Override
    public int delayPop(UUID user, int millis) throws InvalidClientException, InterruptedException {
        validateUser(user);
        // using thread.sleep to wait the required time
        // not using Synchronized, so that other operations can still be carried out
        Thread.sleep(millis);
        return pop(user);
    }
}
