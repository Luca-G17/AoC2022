import java.math.BigInteger;

public class Node {
    private final BigInteger value;
    private Node next;
    private Node prev;

    public Node(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() { return value; }

    public void setNext(Node next) { this.next = next; }
    public void setPrev(Node prev) { this.prev = prev; }
    public Node getNext() { return next; }

    public Node getPrev() { return prev; }
}
