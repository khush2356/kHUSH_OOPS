
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiniBank {

    static class Customer {
        String customerId;
        String name;

        Customer(String customerId, String name) {
            this.customerId = customerId;
            this.name = name;
        }
    }

    static class Account implements Comparable<Account> {
        private String accountNumber;
        private Customer customer;
        private long balance;

        Account(String accountNumber, Customer customer, long balance) {
            this.accountNumber = accountNumber;
            this.customer = customer;
            this.balance = balance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public long getBalance() {
            return balance;
        }

        @Override
        public int compareTo(Account other) {
            return this.accountNumber.compareTo(other.accountNumber);
        }

        @Override
        public String toString() {
            return "Account: " + accountNumber
                    + " | Customer: " + customer.name
                    + " | Balance: " + balance;
        }
    }

    static class AccountStore {
        private HashMap<String, Account> accounts = new HashMap<>();
        private ArrayList<String> transactionHistory = new ArrayList<>();

        public void add(Account account) {
            accounts.put(account.getAccountNumber(), account);
            transactionHistory.add(
                "Added account " + account.getAccountNumber()
            );
        }

        public Account get(String accountNumber) {
            return accounts.get(accountNumber);
        }

        public ArrayList<Account> getAll() {
            return new ArrayList<>(accounts.values());
        }

        public ArrayList<String> getTransactionHistory() {
            return new ArrayList<>(transactionHistory);
        }

        public ArrayList<Account> sortedByBalance() {
            ArrayList<Account> result = getAll();

            result.sort(
                Comparator.comparingLong(Account::getBalance)
                    .thenComparing(Account::getAccountNumber)
            );

            return result;
        }

        public ArrayList<Account> getAccountsAbove(long amount) {
            ArrayList<Account> result = new ArrayList<>();

            for (Account account : accounts.values()) {
                if (account.getBalance() > amount) {
                    result.add(account);
                }
            }

            result.sort(
                Comparator.comparingLong(Account::getBalance)
                    .thenComparing(Account::getAccountNumber)
            );

            return result;
        }

        public TreeMap<String, Account> getSortedByAccountNumber() {
            return new TreeMap<>(accounts);
        }
    }

    static class TransactionProcessor {
        private ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();

        public void add(Account account) {
            sharedAccounts.put(account.getAccountNumber(), account);
        }

        public Account get(String accountNumber) {
            return sharedAccounts.get(accountNumber);
        }

        public int size() {
            return sharedAccounts.size();
        }
    }

    public static void main(String[] args) {

        AccountStore store = new AccountStore();

        Account a1 = new Account(
            "AC0001", new Customer("C001", "Rahul"), 5000);

        Account a2 = new Account(
            "AC0002", new Customer("C002", "Amit"), 2000);

        Account a3 = new Account(
            "AC0003", new Customer("C003", "Priya"), 9000);

        store.add(a1);
        store.add(a2);
        store.add(a3);

        System.out.println("=== Account Lookup ===");
        Account found = store.get("AC0002");

        if (found != null) {
            System.out.println(found);
        } else {
            System.out.println("Account not found.");
        }

        System.out.println("\n=== Accounts Sorted by Balance ===");

        for (Account account : store.sortedByBalance()) {
            System.out.println(account);
        }

        System.out.println("\n=== Accounts Above 4000 ===");

        for (Account account : store.getAccountsAbove(4000)) {
            System.out.println(account);
        }

        System.out.println("\n=== Accounts Sorted by Account Number ===");

        for (Account account :
                store.getSortedByAccountNumber().values()) {
            System.out.println(account);
        }

        System.out.println("\n=== Transaction History ===");

        for (String entry : store.getTransactionHistory()) {
            System.out.println(entry);
        }

        System.out.println("\n=== ConcurrentHashMap Test ===");

        TransactionProcessor processor = new TransactionProcessor();

        processor.add(a1);
        processor.add(a2);
        processor.add(a3);

        System.out.println("Shared accounts: " + processor.size());
        System.out.println("Lookup AC0002: " + processor.get("AC0002"));
    }
}
