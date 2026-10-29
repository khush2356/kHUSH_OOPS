
class BankException extends Exception {

    public BankException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends BankException {

    private long shortfall;

    public InsufficientFundsException(long shortfall) {
        super("Insufficient balance. Shortfall: Rs. " + shortfall);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}

class AccountNotFoundException extends BankException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}

class InvalidAmountException extends BankException {

    public InvalidAmountException(String message) {
        super(message);
    }
}

class Account {

    private String accountNumber;
    private String ownerName;
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be positive.");
        }

        balance += amount;
    }

    public void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive.");
        }

        if (amount > balance) {
            throw new InsufficientFundsException(amount - balance);
        }

        balance -= amount;
    }

    public void transfer(Account to, long amount) throws BankException {
        try {
            if (to == null) {
                throw new AccountNotFoundException(
                        "Destination account not found.");
            }

            withdraw(amount);
            to.deposit(amount);

            System.out.println("Transfer successful.");

        } catch (BankException e) {
            throw e;
        } finally {
            System.out.println("Transfer operation completed.");
        }
    }
}

class BankResource implements AutoCloseable {

    public BankResource() {
        System.out.println("Resource opened.");
    }

    @Override
    public void close() {
        System.out.println("Resource closed.");
    }
}

public class MiniBank {

    public static void main(String[] args) {

        Account a1 = new Account("AC0001", "Riya", 1000);
        Account a2 = new Account("AC0002", "Amit", 500);

        try {
            a1.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
            System.out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Withdrawal attempt completed.");
        }

        try {
            a1.deposit(500);
            System.out.println("Balance after deposit: Rs. "
                    + a1.getBalance());

            a1.transfer(a2, 300);

            System.out.println("Riya balance: Rs. "
                    + a1.getBalance());
            System.out.println("Amit balance: Rs. "
                    + a2.getBalance());

        } catch (BankException e) {
            System.out.println("Bank error: " + e.getMessage());
        }

        try (BankResource resource = new BankResource()) {
            System.out.println("Performing banking operation.");
        } catch (Exception e) {
            System.out.println("Resource error: " + e.getMessage());
        }
    }
}
