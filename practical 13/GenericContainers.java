
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericContainers {

    public static <T> T firstElement(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        return array[0];
    }

    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>();
        intBox.set(100);

        Box<String> strBox = new Box<>();
        strBox.set("Hello Java");

        System.out.println("Integer Box: " + intBox.get());
        System.out.println("String Box: " + strBox.get());

        Pair<String, Integer> pair =
            new Pair<>("Rahul", 21);

        System.out.println("Pair: " +
            pair.getFirst() + ", " + pair.getSecond());

        Integer[] numbers = {10, 20, 30};
        String[] names = {"Amit", "Priya", "Rahul"};

        System.out.println("First number: " +
            firstElement(numbers));
        System.out.println("First name: " +
            firstElement(names));
    }
}
