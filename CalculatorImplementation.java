import java.rmi.RemoteException;
import java.util.Collections;

public class CalculatorImplementation implements Calculator {
    private void stackMin() {
        int minimum = Collections.min(stack);
        stack.clear();
        stack.add(minimum);
    }

    private void stackMax() {
        int maximum = Collections.max(stack);
        stack.clear();
        stack.add(maximum);
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

    private void stackLCM() {
        // iteratively find LCM
        int result = stack.get(0);
        for (int i = 1; i < stack.size(); i++){
            result = lcm(result, stack.get(i));
        }

        // empty the list
        stack.clear();

        // add lcm
        stack.add(result);
    }

    private void stackGCD() {
        // iteratively find GCD
        int result = stack.get(0);
        for (int i = 1; i < stack.size(); i++){
            result = gcd(result, stack.get(i));
        }

        // empty the list
        stack.clear();

        // add gcd
        stack.add(result);
    }

    public CalculatorImplementation() throws RemoteException {
        pushOperations.put("min", this::stackMin);
        pushOperations.put("max", this::stackMax);
        pushOperations.put("lcm", this::stackLCM);
        pushOperations.put("gcd", this::stackGCD);
    }

    @Override
    public void pushValue(int val) {
        stack.add(val);
    }

    @Override
    public void pushOperation(String operation) {
        pushOperations.get(operation).run();
    }

    @Override
    public int pop() {
        return stack.remove(stack.size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public int delayPop(int millis) throws InterruptedException {
        synchronized (this) {
            wait(millis);
        }
        return pop();

    }
}
