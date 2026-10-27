
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

public class MiniBank {
    static class Account implements Serializable {
        private static final long serialVersionUID = 1L;
        long number;
        String owner;
        double balance;
        ArrayList<String> transactions = new ArrayList<>();

        Account(long number, String owner, double balance) {
            this.number = number;
            this.owner = owner;
            this.balance = balance;
        }

        synchronized void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }
            balance += amount;
            transactions.add("Deposit: " + amount);
        }

        synchronized void withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }
            if (amount > balance) {
                throw new IllegalArgumentException("Insufficient balance.");
            }
            balance -= amount;
            transactions.add("Withdrawal: " + amount);
        }
    }

    static HashMap<Long, Account> accounts = new HashMap<>();
    static final String DATA_FILE = "accounts.dat";
    static final String LOG_FILE = "transactions.log";
    static Scanner sc = new Scanner(System.in);
    static long nextNumber = 1001;

    public static void main(String[] args) {
        loadAccounts();

        while (true) {
            System.out.println("\n===== MINIBANK =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Account Statement");
            System.out.println("6. List Accounts");
            System.out.println("7. Search Account");
            System.out.println("8. Process Concurrent Deposits");
            System.out.println("9. Transaction Report");
            System.out.println("0. Save and Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1:
                        openAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        transfer();
                        break;
                    case 5:
                        statement();
                        break;
                    case 6:
                        listAccounts();
                        break;
                    case 7:
                        searchAccount();
                        break;
                    case 8:
                        concurrentDeposits();
                        break;
                    case 9:
                        report();
                        break;
                    case 0:
                        saveAccounts();
                        System.out.println("Data saved. Thank you!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    static long readLong() {
        while (!sc.hasNextLong()) {
            System.out.print("Enter a valid account number: ");
            sc.next();
        }
        return sc.nextLong();
    }

    static double readAmount() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid amount: ");
            sc.next();
        }
        return sc.nextDouble();
    }

    static Account getAccount(long number) {
        Account account = accounts.get(number);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        return account;
    }

    static void openAccount() throws IOException {
        sc.nextLine();
        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains(",")) {
            throw new IllegalArgumentException("Enter a valid name without commas.");
        }

        Account account = new Account(nextNumber++, name, 0);
        accounts.put(account.number, account);

        log("OPEN", account.number, 0);
        saveAccounts();

        System.out.println("Account created successfully.");
        System.out.println("Account number: " + account.number);
    }

    static void deposit() throws IOException {
        System.out.print("Enter account number: ");
        long number = readLong();
        System.out.print("Enter deposit amount: ");
        double amount = readAmount();

        Account account = getAccount(number);
        account.deposit(amount);
        log("DEPOSIT", number, amount);
        saveAccounts();

        System.out.println("Deposit successful. Balance: " + account.balance);
    }

    static void withdraw() throws IOException {
        System.out.print("Enter account number: ");
        long number = readLong();
        System.out.print("Enter withdrawal amount: ");
        double amount = readAmount();

        Account account = getAccount(number);
        account.withdraw(amount);
        log("WITHDRAW", number, amount);
        saveAccounts();

        System.out.println("Withdrawal successful. Balance: " + account.balance);
    }

    static void transfer() throws IOException {
        System.out.print("Enter sender account number: ");
        long fromNumber = readLong();
        System.out.print("Enter receiver account number: ");
        long toNumber = readLong();
        System.out.print("Enter transfer amount: ");
        double amount = readAmount();

        if (fromNumber == toNumber) {
            throw new IllegalArgumentException("Sender and receiver must be different.");
        }

        Account from = getAccount(fromNumber);
        Account to = getAccount(toNumber);

        synchronized (MiniBank.class) {
            from.withdraw(amount);
            to.deposit(amount);
        }

        log("TRANSFER_OUT", fromNumber, amount);
        log("TRANSFER_IN", toNumber, amount);
        saveAccounts();

        System.out.println("Transfer successful.");
        System.out.println("Sender balance: " + from.balance);
        System.out.println("Receiver balance: " + to.balance);
    }

    static void statement() {
        System.out.print("Enter account number: ");
        Account account = getAccount(readLong());

        System.out.println("\nAccount Number: " + account.number);
        System.out.println("Owner: " + account.owner);
        System.out.println("Balance: " + account.balance);
        System.out.println("Transactions:");

        if (account.transactions.isEmpty()) {
            System.out.println("No transactions in this session.");
        } else {
            for (String transaction : account.transactions) {
                System.out.println(transaction);
            }
        }
    }

    static void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        System.out.println("\nAccount Number | Owner | Balance");
        for (Account account : accounts.values()) {
            System.out.println(
                account.number + " | " + account.owner + " | " + account.balance
            );
        }
    }

    static void searchAccount() {
        sc.nextLine();
        System.out.print("Enter customer name to search: ");
        String name = sc.nextLine().trim();

        boolean found = false;

        for (Account account : accounts.values()) {
            if (account.owner.equalsIgnoreCase(name)) {
                System.out.println(
                    account.number + " | " + account.owner + " | " + account.balance
                );
                found = true;
            }
        }

        if (!found) {
            System.out.println("No account found for that name.");
        }
    }

    static void concurrentDeposits() throws Exception {
        System.out.print("Enter account number: ");
        Account account = getAccount(readLong());
        System.out.print("Enter amount per deposit: ");
        double amount = readAmount();
        System.out.print("Enter number of deposits: ");
        int count = readInt();

        if (amount <= 0 || count <= 0) {
            throw new IllegalArgumentException("Amount and number of deposits must be positive.");
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);
        ArrayList<Future<?>> tasks = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            tasks.add(executor.submit(() -> {
                account.deposit(amount);
                synchronized (MiniBank.class) {
                    try {
                        log("DEPOSIT", account.number, amount);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }));
        }

        executor.shutdown();

        for (Future<?> task : tasks) {
            task.get();
        }

        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            executor.shutdownNow();
            throw new IllegalStateException("Tasks did not finish in time.");
        }

        saveAccounts();
        System.out.println("Concurrent deposits completed.");
        System.out.println("Final balance: " + account.balance);
    }

    static synchronized void log(String type, long number, double amount)
            throws IOException {
        String entry = LocalDateTime.now() + " | " + type + " | "
                + number + " | " + amount + System.lineSeparator();

        Files.write(
            Paths.get(LOG_FILE),
            entry.getBytes(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }

    static void report() throws IOException {
        Path path = Paths.get(LOG_FILE);

        if (!Files.exists(path)) {
            System.out.println("No transaction report available.");
            return;
        }

        int totalTransactions = 0;
        double deposits = 0;
        double withdrawals = 0;

        for (String line : Files.readAllLines(path)) {
            String[] parts = line.split("\\s*\\|\\s*");

            if (parts.length < 4) {
                continue;
            }

            String type = parts[1];
            double amount;

            try {
                amount = Double.parseDouble(parts[3]);
            } catch (NumberFormatException e) {
                continue;
            }

            totalTransactions++;

            if (type.equals("DEPOSIT") || type.equals("TRANSFER_IN")) {
                deposits += amount;
            } else if (type.equals("WITHDRAW") || type.equals("TRANSFER_OUT")) {
                withdrawals += amount;
            }
        }

        System.out.println("\n===== TRANSACTION REPORT =====");
        System.out.println("Total logged transactions: " + totalTransactions);
        System.out.println("Total deposits and transfers in: " + deposits);
        System.out.println("Total withdrawals and transfers out: " + withdrawals);
        System.out.println("Net amount: " + (deposits - withdrawals));
    }

    static void saveAccounts() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            out.writeObject(accounts);
            out.writeLong(nextNumber);
        }
    }

    @SuppressWarnings("unchecked")
    static void loadAccounts() {
        Path path = Paths.get(DATA_FILE);

        if (!Files.exists(path)) {
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            accounts = (HashMap<Long, Account>) in.readObject();
            nextNumber = in.readLong();

            if (!accounts.isEmpty()) {
                nextNumber = Math.max(
                    nextNumber,
                    Collections.max(accounts.keySet()) + 1
                );
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            accounts = new HashMap<>();
            nextNumber = 1001;
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }
}
