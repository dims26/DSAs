import lombok.Data;

/**
 * Singly linked list
 */
@Data
class MyLinkedList<T> {
    private Node<T> head;

    MyLinkedList() {
    }

    MyLinkedList(T data) {
        this.head = new Node<>(data);
    }

    /**
     * Add a new node containing {@code data} at the head of the linked list.
     * Runs in constant time {@code O(1)}
     * @param data data to be added to the list
     * @return the node containing data
     */
    Node<T> prepend(T data) {
        Node<T> n = new Node<>(data);
        n.setNextNode(head);

        head = n;

        return n;
    }

    /**
     * Check if list is empty.
     *
     * @return true if empty, otherwise false
     */
    boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the number of items in list. Runs in linear time [{@code O(n)}].
     *
     * @return the number of items in list
     */
    int size() {
        int count = 0;
        Node<T> currentNode = head;

        while (currentNode != null) {
            count++;
            currentNode = currentNode.getNextNode();
        }

        return count;
    }

    /**
     * Inserts a new node containing {@code value} at index position.
     * Runs in linear time {@code O(n)}: Insertion takes constant time {@code O(1)}
     * but finding the position takes linear time {@code O(n)}.
     * @param value Value to be inserted
     * @param index Index at which to insert value
     * @return Node containing value
     */
    Node<T> insert(T value, int index) {
        Node<T> current = head;
        Node<T> newNode = new Node<>(value);

        if (index == 0) return prepend(value);
        else if (index < 0) throw new IndexOutOfBoundsException("Index %d cannot be less than zero".formatted(index));
        else {
            Node<T> prev = null;
            for (int i = 0; i <= index; i++) {
                if (current == null) throw new IndexOutOfBoundsException("Index: %d".formatted(index));
                if (i == index - 1) prev = current;

                current = current.getNextNode();
            }

            newNode.setNextNode(prev.getNextNode());
            prev.setNextNode(newNode);

            return newNode;
        }
    }

    /**
     * Appends a new {@link Node} containing {@code value} to the list.
     * Runs in linear time {@code O(n)}.
     * @param value Value to be appended
     * @return {@link Node} containing {@code value}
     */
    Node<T> append(T value) {
        Node<T> current = head;
        Node<T> newNode = new Node<>(value);

        while (current != null) {
            if (current.getNextNode() == null) {
                current.setNextNode(newNode);

                return newNode;
            }

            current = current.getNextNode();
        }

        head = newNode;
        return newNode;
    }

    /**
     * Removes fist Node containing {@code value}.
     * Runs in linear time {@code O(n)}: Removal takes constant time {@code O(1)}
     * but finding the value takes linear time {@code O(n)}.
     * @param value value to be removed
     * @return {@link Node} containing value, or {@link null} if not found
     */
    Node<T> remove(T value) {
        Node<T> prev = null;
        Node<T> current = head;

        while(current != null) {
            if (current.getData() == value) {
                if (current == head) {
                    head = current.getNextNode();
                } else {
                    prev.setNextNode(current.getNextNode());
                }
                current.setNextNode(null);
                return current;
            }

            prev = current;
            current = current.getNextNode();
        }

        return null;
    }

    /**
     * Removes the Node at the given index.
     * Runs in linear time {@code O(n)}: Removal takes constant time {@code O(1)}
     * but finding the value takes linear time {@code O(n)}.
     * @param index the index at which to remove a node
     * @return {@link Node} to be removed
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds
     */
    Node<T> removeAtIndex(int index) {
        Node<T> prev = null;
        Node<T> current = head;

        if (current == null) throw new IndexOutOfBoundsException("No value at index %d".formatted(index));

        if (index < 0) throw new IndexOutOfBoundsException("Index %d cannot be less than 0".formatted(index));
        else if (index == 0) return remove(head.getData());
        else {
            for (int i = 0; i < index; i++) {
                if (current == null) throw new IndexOutOfBoundsException("No value at index %d".formatted(index));
                if (i == index - 1) prev = current;

                current = current.getNextNode();
            }

            prev.setNextNode(current.getNextNode());
            current.setNextNode(null);

            return current;
        }
    }

    /**
     * Search for first node containing given value within the linked list.
     * Runs in linear time {@code O(n)}
     * @param value item searched for
     * @return {@link Node} containing {@code value}, or {@link null} if not found
     */
    Node<T> search(T value) {
        Node<T> current = head;

        while(current != null) {
            if (current.getData() == value) return current;

            current = current.getNextNode();
        }

        return null;
    }

    /**
     * Gets the {@link Node} at the given index.
     * Runs in linear time {@code O(n)}.
     * @param index The index at which to get a {@link Node}
     * @return {@link Node} at the given index
     */
    Node<T> get(int index) {
        Node<T> current = head;

        if (index < 0) throw new IndexOutOfBoundsException("Index, %d, cannot be less than 0".formatted(index));
        if (current == null) throw new IndexOutOfBoundsException("No value found at index %d".formatted(index));

        for (int i = 0; i <= index; i++) {
            if (current == null) throw new IndexOutOfBoundsException("No value found at index %d".formatted(index));
            if (i == index) break;

            current = current.getNextNode();
        }

        return current;
    }

    @Override
    public String toString() {
        Node<T> current = head;
        StringBuilder st = new StringBuilder();
        while (current != null) {
            if (current == head) st.append("[Head: %s] ".formatted(current.getData()));
            else if (current.getNextNode() == null) st.append("-> [Tail: %s]".formatted(current.getData()));
            else st.append("-> [%s] ".formatted(current.getData()));

            current = current.getNextNode();
        }

        return st.toString();
    }
}
