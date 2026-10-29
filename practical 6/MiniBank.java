
import java.util.Scanner;
import java.util.regex.Pattern;

class Validator {

    private static final Pattern MOBILE
            = Pattern.compile("[6-9][0-9]{9}");

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE.matcher(mobile).matches();
    }
}

interface Transactable {

    void deposit(long amount);

    boolean withdraw(long amount);
}

interface InterestBearing {

    double interestRate();

    long getBalance();

    default double yearlyInterest() {
        return getBalance() * interestRate() / 100;
    }
}

@FunctionalInterface
interface WithdrawRule {

    boolean allow(Account account, long amount);
}

interface Premium {
}

class Account implements Transactable, InterestBearing {

    private final String accountNumber;
    private String ownerName;
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = Math.max(0, balance);
    }

    @Override
    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public boolean withdraw(long amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }

        balance -= amount;
        return true;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName
                + " | Rs. " + balance;
    }
}

class SavingsAccount extends Account implements Premium {

    private long minBalance;

    public SavingsAccount(String number, String name,
            long balance, long minBalance) {
        super(number, name, balance);
        this.minBalance = minBalance;
    }

    @Override
    public boolean withdraw(long amount) {
        if (amount <= 0 || getBalance() - amount < minBalance) {
            return false;
        }

        return super.withdraw(amount);
    }
}

public class MiniBank {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Account account = new Account("AC0001", "Riya", 5000);

        // Anonymous class
        WithdrawRule rule1 = new WithdrawRule() {
            @Override
            public boolean allow(Account a, long amount) {
                return amount > 0 && a.getBalance() >= amount;
            }
        };

        // Lambda expression
        WithdrawRule rule2
                = (a, amount) -> amount > 0 && a.getBalance() >= amount;

        System.out.println("Anonymous class: "
                + rule1.allow(account, 1000));
        System.out.println("Lambda expression: "
                + rule2.allow(account, 1000));
        System.out.println("Valid mobile: "
                + Validator.isValidMobile("9876543210"));
        System.out.println("Yearly interest: Rs. "
                + account.yearlyInterest());

        SavingsAccount premiumAccount
                = new SavingsAccount("AC0002", "Amit", 10000, 2000);

        System.out.println("Premium account: "
                + (premiumAccount instanceof Premium));

        int choice = 0;

        do {
            System.out.println("\n===== MINIBANK MENU =====");
            System.out.println("1. Show Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Enter a valid number.");
                sc.next();
                continue;
            }

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println(account);
                    break;

                case 2:
                    System.out.print("Enter deposit amount: ");
                    if (sc.hasNextLong()) {
                        long amount = sc.nextLong();
                        account.deposit(amount);
                        System.out.println("Balance: Rs. "
                                + account.getBalance());
                    } else {
                        System.out.println("Invalid amount.");
                        sc.next();
                    }
                    break;

                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    if (sc.hasNextLong()) {
                        long amount = sc.nextLong();

                        if (rule2.allow(account, amount)
                                && account.withdraw(amount)) {
                            System.out.println("Withdrawal successful.");
                        } else {
                            System.out.println("Withdrawal failed.");
                        }

                        System.out.println("Balance: Rs. "
                                + account.getBalance());
                    } else {
                        System.out.println("Invalid amount.");
                        sc.next();
                    }
                    break;

                case 4:
                    System.out.println("Thank you for using MiniBank!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }
}
