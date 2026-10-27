
class Stack<T> {
    private Object[] elements;
    private int top = -1;

    public Stack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be positive");
        }
        elements = new Object[capacity];
    }

    public void push(T value) {
        if (top == elements.length - 1) {
            throw new IllegalStateException("Stack is full");
        }
        elements[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }

        T value = (T) elements[top];
        elements[top--] = null;
        return value;
    }

    public boolean isEmpty() {
        return top == -1;
    }
}

public class GenericStackDemo {
    public static void main(String[] args) {
        Stack<Integer> numbers = new Stack<>(5);
        numbers.push(10);
        numbers.push(20);

        System.out.println("Integer pop: " + numbers.pop());
        System.out.println("Integer pop: " + numbers.pop());

        Stack<String> names = new Stack<>(5);
        names.push("Amit");
        names.push("Priya");

        System.out.println("String pop: " + names.pop());
        System.out.println("String pop: " + names.pop());

        try {
            numbers.pop();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
