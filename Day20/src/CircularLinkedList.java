import java.math.BigInteger;
import java.util.HashMap;

public class CircularLinkedList {
    private Node head = null;
    private Node tail = null;

    public Node add(BigInteger value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        }
        else
            tail.setNext(newNode);
        newNode.setPrev(tail);
        tail = newNode;
        head.setPrev(tail);
        tail.setNext(head);
        return newNode;
    }
    public Node get(long i) {
        if (i == 0) return head;
        int x = 0;
        Node curr = head.getNext();
        while(x < i - 1 && !curr.equals(head)) {
            curr = curr.getNext();
            x++;
        }
        if (!curr.equals(head)) return curr;
        return null;
    }
    public BigInteger indexOf(Node node) {
        Node currentNode = head;
        int i = 0;
        if (currentNode.equals(node)) return new BigInteger("0");
        currentNode = currentNode.getNext();
        i++;
        while (!currentNode.getNext().equals(head)) {
            if (currentNode.equals(node))
                return BigInteger.valueOf(i);
            i++;
            currentNode = currentNode.getNext();
        }
        return BigInteger.valueOf(i);
    }

    public void delete(Node n) {
        if (head.equals(n))
            head = n.getNext();
        if (tail.equals(n))
            tail = n.getPrev();
        Node prev = n.getPrev();
        Node next = n.getNext();
        prev.setNext(next);
        next.setPrev(prev);
    }
    public void move(Node n, BigInteger to) {
        delete(n);
        insertAt(n, to);
    }
    public void insertAt(Node n, BigInteger index) {
        Node currentNode = head;
        for (int i = 0; i < index.longValue(); i++) {
            currentNode = currentNode.getNext();
        }
        Node oldPrev = currentNode.getPrev();
        currentNode.setPrev(n);
        oldPrev.setNext(n);
        n.setPrev(oldPrev);
        n.setNext(currentNode);
        if (index.longValue() == 0)
            head = n;
    }
}
