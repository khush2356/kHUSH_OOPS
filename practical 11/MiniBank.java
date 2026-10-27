
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MiniBank {

    static class Customer implements Serializable {
        private static final long serialVersionUID = 1L;

        String customerId;
        String name;
        transient String temporaryNote = "Temporary note";

        Customer(String customerId, String name) {
            this.customerId = customerId;
            this.name = name;
        }
    }

    static abstract class Account implements Serializable {
        private static final long serialVersionUID = 1L;

        String accountNumber;
        Customer customer;
        long balance;

        Account(String accountNumber, Customer customer, long balance) {
            this.accountNumber = accountNumber;
            this.customer = customer;
            this.balance = balance;
        }

        void deposit(long amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid deposit");
            }
            balance = Math.addExact(balance, amount);
        }

        void withdraw(long amount) {
            if (amount <= 0 || amount > balance) {
                throw new IllegalArgumentException("Invalid withdrawal");
            }
            balance -= amount;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()
                    + " | Account: " + accountNumber
                    + " | Customer: " + customer.name
                    + " | Balance: " + balance;
        }
    }

    static class SavingsAccount extends Account {
        private static final long serialVersionUID = 1L;

        SavingsAccount(String number, Customer customer, long balance) {
            super(number, customer, balance);
        }
    }

    static class CurrentAccount extends Account {
        private static final long serialVersionUID = 1L;

        CurrentAccount(String number, Customer customer, long balance) {
            super(number, customer, balance);
        }
    }

    static class StatePersister {

        public static void save(Account[] accounts, Path file)
                throws IOException {
            Files.createDirectories(file.toAbsolutePath().getParent());

            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(file))) {
                out.writeObject(accounts);
            }
        }

        public static Account[] load(Path file)
                throws IOException, ClassNotFoundException {
            try (ObjectInputStream in = new ObjectInputStream(
                    Files.newInputStream(file))) {
                return (Account[]) in.readObject();
            }
        }
    }

    static class TransactionLog {

        public static void append(Path file, String line)
                throws IOException {
            Files.createDirectories(file.toAbsolutePath().getParent());

            Files.write(
                file,
                java.util.Collections.singletonList(line),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        }
    }

    static class ReportGenerator {

        public static void generate(Path logsFolder, Path reportFile)
                throws IOException {

            long totalDeposits = 0;
            long totalWithdrawals = 0;

            StringBuilder report = new StringBuilder();

            try (DirectoryStream<Path> stream =
                    Files.newDirectoryStream(logsFolder)) {

                for (Path file : stream) {
                    if (!Files.isRegularFile(file)
                            || !file.getFileName().toString().endsWith(".log")
                            || Files.size(file) == 0) {
                        continue;
                    }

                    BasicFileAttributes attrs =
                        Files.readAttributes(
                            file, BasicFileAttributes.class);

                    report.append("File: ")
                          .append(file.getFileName()).append("\n");
                    report.append("Size: ")
                          .append(attrs.size()).append(" bytes\n");
                    report.append("Last modified: ")
                          .append(attrs.lastModifiedTime()).append("\n");

                    try (BufferedReader reader = Files.newBufferedReader(
                            file, StandardCharsets.UTF_8)) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");

                            if (parts.length != 3) {
                                continue;
                            }

                            try {
                                long amount = Long.parseLong(parts[2]);

                                if (amount <= 0) {
                                    continue;
                                }

                                if (parts[0].equals("DEPOSIT")) {
                                    totalDeposits =
                                        Math.addExact(totalDeposits, amount);
                                } else if (parts[0].equals("WITHDRAW")) {
                                    totalWithdrawals =
                                        Math.addExact(totalWithdrawals, amount);
                                }
                            } catch (NumberFormatException e) {
                                // Ignore invalid transaction amounts.
                            }
                        }
                    }

                    report.append("\n");
                }
            }

            long netChange = Math.subtractExact(
                totalDeposits, totalWithdrawals);

            report.append("===== END OF DAY SUMMARY =====\n");
            report.append("Total deposits: ")
                  .append(totalDeposits).append("\n");
            report.append("Total withdrawals: ")
                  .append(totalWithdrawals).append("\n");
            report.append("Net change: ")
                  .append(netChange).append("\n");

            Files.createDirectories(
                reportFile.toAbsolutePath().getParent());

            Files.writeString(
                reportFile,
                report.toString(),
                StandardCharsets.UTF_8
            );

            System.out.println(report);
            System.out.println("Report saved to: " + reportFile);
        }
    }

    public static void main(String[] args) {
        Path dataFolder = Paths.get("data");
        Path accountFile = dataFolder.resolve("accounts.dat");
        Path logsFolder = dataFolder.resolve("logs");
        Path logFile = logsFolder.resolve("transactions.log");
        Path reportFile = dataFolder.resolve("end-of-day-report.txt");

        try {
            Files.createDirectories(logsFolder);

            Account[] accounts = {
                new SavingsAccount("AC0001",
                    new Customer("C001", "Rahul"), 1000),
                new CurrentAccount("AC0002",
                    new Customer("C002", "Amit"), 2000),
                new SavingsAccount("AC0003",
                    new Customer("C003", "Priya"), 3000)
            };

            TransactionLog.append(logFile, "DEPOSIT AC0001 500");
            accounts[0].deposit(500);

            TransactionLog.append(logFile, "WITHDRAW AC0002 200");
            accounts[1].withdraw(200);

            TransactionLog.append(logFile, "DEPOSIT AC0003 1000");
            accounts[2].deposit(1000);

            StatePersister.save(accounts, accountFile);
            System.out.println("Accounts saved successfully.");

            Account[] loadedAccounts = StatePersister.load(accountFile);

            System.out.println("\nReloaded accounts:");

            for (Account account : loadedAccounts) {
                System.out.println(account);
            }

            System.out.println("\nGenerating end-of-day report:");
            ReportGenerator.generate(logsFolder, reportFile);

            System.out.println("\nTransient field test:");
            System.out.println(
                "Temporary note: "
                + loadedAccounts[0].customer.temporaryNote);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Error: Amount overflow.");
        }
    }
}
