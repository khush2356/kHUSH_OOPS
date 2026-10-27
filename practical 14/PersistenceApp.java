
import java.io.*;
import java.nio.file.*;

public class PersistenceApp {

    static class Item implements Serializable {
        private static final long serialVersionUID = 1L;
        String name;

        Item(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        Path file = Paths.get("items.dat");

        Item original = new Item("Laptop");

        try {
            // Save object
            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(file))) {
                out.writeObject(original);
            }

            System.out.println("Item saved successfully.");

            // Load object
            Item loaded;

            try (ObjectInputStream in = new ObjectInputStream(
                    Files.newInputStream(file))) {
                loaded = (Item) in.readObject();
            }

            System.out.println("Original item: " + original);
            System.out.println("Loaded item: " + loaded);

            if (original.name.equals(loaded.name)) {
                System.out.println("Persistence test passed.");
            } else {
                System.out.println("Persistence test failed.");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
