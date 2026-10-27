
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class MiniBank {

    // Annotation used to identify an object's ID field
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Id {
    }

    // Customer class
    static class Customer {
        @Id
        private String customerId;
        private String name;

        Customer(String customerId, String name) {
            this.customerId = customerId;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Customer ID: " + customerId
                    + " | Name: " + name;
        }
    }

    // Account class
    static class Account {
        @Id
        private String accountNumber;
        private String holderName;
        private long balance;

        Account(String accountNumber, String holderName,
                long balance) {
            this.accountNumber = accountNumber;
            this.holderName = holderName;
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "Account: " + accountNumber
                    + " | Holder: " + holderName
                    + " | Balance: " + balance;
        }
    }

    // Generic Repository class
    static class Repository<T> {
        private final HashMap<String, T> data = new HashMap<>();

        // Read the field marked with @Id using reflection
        private String getId(T entity) {
            if (entity == null) {
                throw new IllegalArgumentException(
                    "Entity cannot be null");
            }

            Class<?> type = entity.getClass();

            while (type != null) {
                for (Field field : type.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(entity);

                            if (value == null) {
                                throw new IllegalArgumentException(
                                    "@Id field cannot be null");
                            }

                            return value.toString();

                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException(
                                "Cannot read ID field", e);
                        }
                    }
                }

                type = type.getSuperclass();
            }

            throw new IllegalArgumentException(
                "No field marked with @Id found");
        }

        // Save one object
        public T save(T entity) {
            String id = getId(entity);
            data.put(id, entity);
            return entity;
        }

        // Find an object by ID
        public T findById(String id) {
            return data.get(id);
        }

        // Return all stored objects
        public ArrayList<T> findAll() {
            return new ArrayList<>(data.values());
        }

        // Delete an object by ID
        public boolean delete(String id) {
            return data.remove(id) != null;
        }

        // Save several objects
        @SafeVarargs
        public final void saveAll(T... items) {
            for (T item : items) {
                save(item);
            }
        }

        // Return number of stored objects
        public int count() {
            return data.size();
        }

        // Generic method with bounded type parameter
        public static <N extends Number> double average(N[] values) {
            if (values == null || values.length == 0) {
                throw new IllegalArgumentException(
                    "Array cannot be empty");
            }

            double sum = 0;

            for (N value : values) {
                if (value == null) {
                    throw new IllegalArgumentException(
                        "Values cannot contain null");
                }
                sum += value.doubleValue();
            }

            return sum / values.length;
        }
    }

    public static void main(String[] args) {

        // Repository for Account objects
        Repository<Account> accountRepo = new Repository<>();

        Account a1 = new Account("AC0001", "Rahul", 5000);
        Account a2 = new Account("AC0002", "Amit", 2000);
        Account a3 = new Account("AC0003", "Priya", 9000);

        accountRepo.saveAll(a1, a2, a3);

        System.out.println("=== Find Account by ID ===");
        Account found = accountRepo.findById("AC0002");
        System.out.println(found);

        System.out.println("\n=== All Accounts ===");
        for (Account account : accountRepo.findAll()) {
            System.out.println(account);
        }

        System.out.println("\nTotal accounts: " + accountRepo.count());

        System.out.println("\n=== Delete Account ===");
        System.out.println(
            "Deleted AC0001: " + accountRepo.delete("AC0001"));
        System.out.println(
            "Remaining accounts: " + accountRepo.count());

        // Reuse the same repository class for Customer
        Repository<Customer> customerRepo = new Repository<>();

        customerRepo.saveAll(
            new Customer("C001", "Rahul"),
            new Customer("C002", "Amit")
        );

        System.out.println("\n=== All Customers ===");
        for (Customer customer : customerRepo.findAll()) {
            System.out.println(customer);
        }

        // Test bounded generic method
        Integer[] numbers = {10, 20, 30};
        System.out.println("\nAverage: "
                + Repository.average(numbers));
    }
}
