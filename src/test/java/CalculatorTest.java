import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private CalculatorImplementation calculator;

    @BeforeEach
    void setUp() throws RemoteException {
        calculator = new CalculatorImplementation();
    }

    // -------- register --------
    @Test
    void register_singleClient_createsUniqueStack() throws Exception {
        UUID client = calculator.register();
        assertNotNull(client);
        assertTrue(calculator.isEmpty(client));
    }

    @Test
    void register_multipleClients_createsSeparateUUID() throws Exception {
        UUID client1 = calculator.register();
        UUID client2 = calculator.register();
        UUID client3 = calculator.register();
        UUID client4 = calculator.register();

        assertNotEquals(client1, client2);
        assertNotEquals(client2, client3);
        assertNotEquals(client3, client4);
        assertNotEquals(client1, client4);

    }

    @Test
    void register_multipleClients_createsMultipleNewStacks() throws Exception {
        UUID client1 = calculator.register();
        UUID client2 = calculator.register();
        UUID client3 = calculator.register();
        UUID client4 = calculator.register();

        assertTrue(calculator.isEmpty(client1));
        assertTrue(calculator.isEmpty(client2));
        assertTrue(calculator.isEmpty(client3));
        assertTrue(calculator.isEmpty(client4));
    }

    // -------- pushValue --------
    @Test
    void pushValue_singleClient_pushesValues() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 5);
        calculator.pushValue(client, 10);
        assertEquals(
                Arrays.asList(5, 10),
                Calculator.clientStackMap.get(client)
        );
    }

    @Test
    void pushValue_multipleClients_independentStacks() throws Exception {
        UUID c1 = calculator.register();
        UUID c2 = calculator.register();
        UUID c3 = calculator.register();
        UUID c4 = calculator.register();

        calculator.pushValue(c1, 1);
        calculator.pushValue(c2, 2);
        calculator.pushValue(c3, 3);
        calculator.pushValue(c4, 10);
        calculator.pushValue(c1, 4);
        calculator.pushValue(c2, 5);
        calculator.pushValue(c3, 6);
        calculator.pushValue(c4, 11);

        assertEquals(Arrays.asList(1,4), Calculator.clientStackMap.get(c1));
        assertEquals(Arrays.asList(2,5), Calculator.clientStackMap.get(c2));
        assertEquals(Arrays.asList(3,6), Calculator.clientStackMap.get(c3));
        assertEquals(Arrays.asList(10,11), Calculator.clientStackMap.get(c4));
    }

    @Test
    void pushValue_invalidClient_throwsException() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(InvalidClientException.class,
                () -> calculator.pushValue(fakeId, 10));
    }

    // -------- pushOperation --------
    @Test
    void pushOperation_singleClient_minOperation() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 5);
        calculator.pushValue(client, 1);
        calculator.pushValue(client, 9);

        calculator.pushOperation(client, "min");
        assertEquals(Arrays.asList(1), Calculator.clientStackMap.get(client));
    }

    @Test
    void pushOperation_singleClient_maxOperation() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 5);
        calculator.pushValue(client, 1);
        calculator.pushValue(client, 9);

        calculator.pushOperation(client, "max");
        assertEquals(Arrays.asList(9), Calculator.clientStackMap.get(client));
    }

    @Test
    void pushOperation_singleClient_gcdOperation() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 5);
        calculator.pushValue(client, 15);
        calculator.pushValue(client, 25);
        calculator.pushValue(client, -10);

        calculator.pushOperation(client, "gcd");
        assertEquals(Arrays.asList(5), Calculator.clientStackMap.get(client));
    }

    @Test
    void pushOperation_singleClient_lcmOperation() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 5);
        calculator.pushValue(client, 1);
        calculator.pushValue(client, 9);
        calculator.pushValue(client, -15);

        calculator.pushOperation(client, "lcm");
        assertEquals(Arrays.asList(45), Calculator.clientStackMap.get(client));
    }

    @Test
    void pushOperation_multipleClients_independentStacks() throws Exception {
        UUID c1 = calculator.register();
        UUID c2 = calculator.register();
        UUID c3 = calculator.register();
        UUID c4 = calculator.register();

        calculator.pushValue(c1, 5);
        calculator.pushValue(c1, 10);
        calculator.pushValue(c2, 3);
        calculator.pushValue(c2, 6);
        calculator.pushValue(c3, -10);
        calculator.pushValue(c3, 12);
        calculator.pushValue(c4, 8);
        calculator.pushValue(c4, 12);

        calculator.pushOperation(c1, "max");
        calculator.pushOperation(c2, "min");
        calculator.pushOperation(c3, "gcd");
        calculator.pushOperation(c4, "lcm");

        assertEquals(10, calculator.pop(c1));
        assertEquals(3, calculator.pop(c2));
        assertEquals(2, calculator.pop(c3));
        assertEquals(24, calculator.pop(c4));
    }

    @Test
    void pushOperation_invalidClient_throwsException() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(InvalidClientException.class,
                () -> calculator.pushOperation(fakeId, "min"));
    }

    // -------- pop --------
    // No test for Empty Stack since task outline states to assume
    // functions are called when there are values in the stack

    @Test
    void pop_singleClient_returnsLastValue() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 5);
        calculator.pushValue(client, 10);
        assertEquals(10, calculator.pop(client));
    }

    @Test
    void pop_multipleClients_independentStacks() throws Exception {
        UUID c1 = calculator.register();
        UUID c2 = calculator.register();
        UUID c3 = calculator.register();
        UUID c4 = calculator.register();

        calculator.pushValue(c1, 1);
        calculator.pushValue(c2, 2);
        calculator.pushValue(c3, 3);
        calculator.pushValue(c4, 4);

        assertEquals(1, calculator.pop(c1));
        assertEquals(2, calculator.pop(c2));
        assertEquals(3, calculator.pop(c3));
        assertEquals(4, calculator.pop(c4));
    }

    @Test
    void pop_invalidClient_throwsException() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(InvalidClientException.class,
                () -> calculator.pop(fakeId));
    }

    // -------- isEmpty --------
    @Test
    void isEmpty_singleClient_stackEmptyAndNotEmpty() throws Exception {
        UUID client = calculator.register();
        assertTrue(calculator.isEmpty(client));
        calculator.pushValue(client, 1);
        assertFalse(calculator.isEmpty(client));
    }

    @Test
    void isEmpty_multipleClients_independent() throws Exception {
        UUID c1 = calculator.register();
        UUID c2 = calculator.register();
        UUID c3 = calculator.register();
        UUID c4 = calculator.register();

        calculator.pushValue(c1, 1);
        calculator.pushValue(c4, 4);
        assertFalse(calculator.isEmpty(c1));
        assertTrue(calculator.isEmpty(c2));
        assertTrue(calculator.isEmpty(c3));
        assertFalse(calculator.isEmpty(c4));
    }

    @Test
    void isEmpty_invalidClient_throwsException() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(InvalidClientException.class,
                () -> calculator.isEmpty(fakeId));
    }

    // -------- delayPop --------
    @Test
    void delayPop_singleClient_delaysAndPops() throws Exception {
        UUID client = calculator.register();
        calculator.pushValue(client, 42);
        long start = System.currentTimeMillis();
        int result = calculator.delayPop(client, 200);
        long end = System.currentTimeMillis();

        assertEquals(42, result);
        assertTrue((end - start) >= 200);
    }

    @Test
    void delayPop_multipleClients_independentDelays() throws Exception {
        UUID c1 = calculator.register();
        UUID c2 = calculator.register();
        UUID c3 = calculator.register();
        UUID c4 = calculator.register();

        calculator.pushValue(c1, 1);
        calculator.pushValue(c2, 2);
        calculator.pushValue(c3, 3);
        calculator.pushValue(c4, 4);

        long start = System.currentTimeMillis();

        int r1 = calculator.delayPop(c1, 50);
        int r2 = calculator.delayPop(c2, 50);
        int r3 = calculator.delayPop(c3, 50);
        int r4 = calculator.delayPop(c4, 50);

        long end = System.currentTimeMillis();


        assertEquals(1, r1);
        assertEquals(2, r2);
        assertEquals(3, r3);
        assertEquals(4, r4);
        assertTrue((end - start) >= 200);
    }

    @Test
    void delayPop_invalidClient_throwsException() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(InvalidClientException.class,
                () -> calculator.delayPop(fakeId, 100));
    }
}
