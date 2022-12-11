import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class Monkey {
    private class IntWrapper {
        public IntWrapper(BigInteger value) { this.value = value; }
        public BigInteger value;
    }
    private final String operation;
    private BigInteger newVal;
    private IntWrapper old = new IntWrapper(new BigInteger("0"));
    private final IntWrapper operand1;
    private final IntWrapper operand2;
    private final int trueTest;
    private final int falseTest;
    private final int testDivisor;
    private final Queue<BigInteger> items = new ArrayDeque<>();
    private long inspected = 0;
    private int modNum = 0;

    public Monkey(String operation, String test, String testTrue, String testFalse, String items) {
        String[] ops = operation.split(" ");
        if (Objects.equals(ops[5], "old"))
            this.operand1 = old;
        else
            this.operand1 = new IntWrapper(new BigInteger(ops[5]));
        if (Objects.equals(ops[7], "old"))
            this.operand2 = old;
        else
            this.operand2 = new IntWrapper(new BigInteger(ops[7]));
        this.operation = ops[6];
        this.testDivisor = Integer.parseInt(test.split(" ")[5]);
        this.trueTest = Integer.parseInt(testTrue.split(" ")[9]);
        this.falseTest = Integer.parseInt(testFalse.split(" ")[9]);
        String[] itemsArr = items.split(" ");
        for (int i = 4; i < itemsArr.length; i++) {
            String item = itemsArr[i].replace(",", "");
            this.items.add(new BigInteger(item));
        }
    }
    public BigInteger floorDiv(BigInteger a, BigInteger b) {
        // divideAndRemainder returns quotient and remainder in array
        BigInteger[] qr = a.divideAndRemainder(b);
        return qr[0].signum() >= 0 || qr[1].signum() == 0 ?
                qr[0] : qr[0].subtract(BigInteger.ONE);
    }
    private void operation(boolean div3) {
        old.value = items.remove();
        if (operation.equals("+"))
            newVal = operand1.value.add(operand2.value);
        else
            newVal = operand1.value.multiply(operand2.value);
        if (div3)
            newVal = floorDiv(newVal, new BigInteger("3"));
        else
            newVal = newVal.mod(BigInteger.valueOf(modNum));

        inspected++;
    }
    private BigInteger[] testItem() {
        if (newVal.mod(BigInteger.valueOf(testDivisor)).intValue() == 0)
            return new BigInteger[] {BigInteger.valueOf(trueTest), newVal};
        else
            return new BigInteger[] {BigInteger.valueOf(falseTest), newVal};
    }
    public BigInteger[] processNextItem(boolean div3) {
        operation(div3);
        return testItem();
    }
    public void catchItem(BigInteger item) {
        items.add(item);
    }
    public int getItemCount() { return items.size(); }
    public long getInspected() { return inspected; }
    public int getTestDivisor() { return testDivisor; }
    public void setModNum(int modNum) {
        this.modNum = modNum;
    }
}
