
import java.util.ArrayList;
import java.util.Scanner;

public class MiniApp {

    static class Item {
        private String name;

        Item(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid item name");
            }
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    static class ItemService {
        private ArrayList<Item> items = new ArrayList<>();

        public void addItem(Item item) {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            items.add(item);
        }

        public int getCount() {
            return items.size();
        }

        public ArrayList<Item> getAll() {
            return new ArrayList<>(items);
        }
    }

    static void testAddItem() {
        ItemService service = new ItemService();
        service.addItem(new Item("Laptop"));

        if (service.getCount() != 1) {
            throw new AssertionError("Add item test failed");
        }

        try {
            service.addItem(null);
            throw new AssertionError("Null test failed");
        } catch (IllegalArgumentException expected) {
            System.out.println("Invalid item test passed");
        }

        System.out.println("Add item test passed");
    }

    public static void main(String[] args) {
        ItemService service = new ItemService();
        Scanner sc = new Scanner(System.in);

        testAddItem();

        while (true) {
            System.out.println("\n1. Add Item");
            System.out.println("2. Display Items");
            System.out.println("3. Count Items");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    try {
                        service.addItem(new Item(sc.nextLine()));
                        System.out.println("Item added");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Items: " + service.getAll());
                    break;

                case 3:
                    System.out.println("Count: " + service.getCount());
                    break;

                case 4:
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
