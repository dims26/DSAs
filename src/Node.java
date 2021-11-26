import lombok.Data;

/**
 * An object for storing a single node of a {@link MyLinkedList}
 * Models two attributes; data and the link to the next node in the linked list
 */
@Data
class Node<T> {
    Node(T data) {
        this.data = data;
    }

    private T data;
    private Node<T> nextNode;

    @Override
    public String toString() {
        return "Node(data: %s)".formatted(data);
    }
}
