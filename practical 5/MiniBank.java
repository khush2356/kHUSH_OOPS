
abstract class Account {

    protected String accountNumber;
    protected String ownerName;
    protected long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public long getBalance() {
        return balance;
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && canWithdraw(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double monthlyInterest() {
        return balance * interestRate() / 100 / 12;
    }
}

class SavingsAccount extends Account {

    private long minBalance;

    public SavingsAccount(String number, String name,
            long balance, long minBalance) {
        super(number, name, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && balance - amount >= minBalance;
    }
}

class CurrentAccount extends Account {

    private long overdraftLimit;

    public CurrentAccount(String number, String name,
            long balance, long overdraftLimit) {
        super(number, name, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && balance - amount >= -overdraftLimit;
    }
}

class FixedDepositAccount extends Account {

    public FixedDepositAccount(String number, String name, long balance) {
        super(number, name, balance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }
}

public class MiniBank {

    public static void main(String[] args) {

        Account[] accounts = {
            new SavingsAccount("AC0001", "Riya", 10000, 2000),
            new CurrentAccount("AC0002", "Amit", 5000, 3000),
            new FixedDepositAccount("AC0003", "Neha", 20000)
        };

        for (Account account : accounts) {
            System.out.println("Account: " + account.accountNumber);
            System.out.println("Owner: " + account.ownerName);
            System.out.println("Balance: Rs. " + account.getBalance());
            System.out.println("Interest Rate: "
                    + account.interestRate() + "%");
            System.out.println("Monthly Interest: Rs. "
                    + account.monthlyInterest());

            if (account instanceof SavingsAccount savings) {
                System.out.println("Minimum Balance: Rs. "
                        + savings.getBalance());
            }

            System.out.println("-----------------------");
        }

        System.out.println("Savings withdrawal of Rs. 3000: "
                + accounts[0].withdraw(3000));

        System.out.println("Fixed deposit withdrawal of Rs. 1000: "
                + accounts[2].withdraw(1000));
    }
}
