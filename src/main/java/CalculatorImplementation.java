import java.rmi.RemoteException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.UUID;

public class CalculatorImplementation implements Calculator {
    private void stackMin(UUID user) {
        ArrayList<Integer> userStack = clientStackMap.get(user);
        int minimum = Collections.min(userStack);
        userStack.clear();
        userStack.add(minimum);
    }

    private void stackMax(UUID user) {
        ArrayList<Integer> userStack = clientStackMap.get(user);

        int maximum = Collections.max(userStack);
        userStack.clear();
        userStack.add(maximum);
    }

    private int gcd(int a, int b){
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private int lcm(int a, int b){
        if (a == 0 || b == 0) {
            return 0; // LCM involving zero is typically defined as zero
        }
        return Math.abs(a * b) / gcd(a, b);
    }

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

    public CalculatorImplementation() throws RemoteException {
        pushOperations.put("min", this::stackMin);
        pushOperations.put("max", this::stackMax);
        pushOperations.put("lcm", this::stackLCM);
        pushOperations.put("gcd", this::stackGCD);
    }

    @Override
    public UUID register(){
        return UUID.randomUUID();
    }

    @Override
    public void pushValue(UUID user, int val) {
        clientStackMap.get(user).add(val);
    }

    @Override
    public void pushOperation(UUID user, String operation) {
        pushOperations.get(operation).accept(user);
    }

    @Override
    public int pop(UUID user) {
        return clientStackMap.get(user).remove(clientStackMap.get(user).size() - 1);
    }

    @Override
    public boolean isEmpty(UUID user) {
        return clientStackMap.get(user).isEmpty();
    }

    @Override
    public int delayPop(UUID user, int millis) throws InterruptedException {
        synchronized (this) {
            wait(millis);
        }
        return pop(user);

    }
}
