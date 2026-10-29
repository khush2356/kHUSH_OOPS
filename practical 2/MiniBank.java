
class Customer {

    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    private static long customerCounter = 100;

    private static String generateCustomerId() {
        return "CUST" + (++customerCounter);
    }

    public Customer(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = generateCustomerId();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCustomerId() {
        return customerId;
    }
}

class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    public Account(String ownerName, long balance) {
        this.accountNumber
                = String.format("AC%04d", ++accountCounter);
        this.ownerName = ownerName;
        this.balance = Math.max(0, balance);
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposit(long amount) {
        if (amount > 0 && active) {
            balance += amount;
        }
    }

    public boolean withdraw(long amount) {
        if (amount <= 0 || !active || balance < amount) {
            return false;
        }

        balance -= amount;
        return true;
    }

    public boolean transfer(Account receiver, long amount) {
        if (receiver == null || receiver == this || !receiver.active) {
            return false;
        }

        if (withdraw(amount)) {
            receiver.deposit(amount);
            return true;
        }

        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}

public class MiniBank {

    public static void main(String[] args) {

        Customer customer = new Customer(
                "Riya", "riya@gmail.com", "9876543210");

        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Customer Name: " + customer.getName());

        Account[] accounts = {
            new Account("Riya", 5000),
            new Account("Amit", 3000),
            new Account("Neha")
        };

        Account a = accounts[0];

        a.deposit(2000);
        System.out.println("\nAfter deposit: " + a.getBalance());

        System.out.println("Withdraw 3000: " + a.withdraw(3000));
        System.out.println("After withdrawal: " + a.getBalance());

        System.out.println("Withdraw 10000: " + a.withdraw(10000));
        System.out.println("Final balance: " + a.getBalance());

        System.out.println("\nAll Account Details:");
        for (Account account : accounts) {
            System.out.println(
                    account.getAccountNumber() + " | "
                    + account.getOwnerName() + " | Rs. "
                    + account.getBalance());
        }

        System.out.println("\nTransfer Rs. 500 from Riya to Amit: "
                + accounts[0].transfer(accounts[1], 500));

        System.out.println("Riya balance: " + accounts[0].getBalance());
        System.out.println("Amit balance: " + accounts[1].getBalance());
    }
}
