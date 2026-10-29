
class Account {

    private long balance = 0;

    // Unsafe deposit: race condition
    public void depositUnsafe(long amount) {
        long temp = balance;
        Thread.yield();
        balance = temp + amount;
    }

    // Thread-safe deposit
    public synchronized void deposit(long amount) {
        balance += amount;
    }

    public synchronized void withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    public synchronized long getBalance() {
        return balance;
    }
}

class AccountWorker implements Runnable {

    private Account account;
    private int times;
    private long amount;
    private boolean safe;

    public AccountWorker(Account account, int times,
            long amount, boolean safe) {
        this.account = account;
        this.times = times;
        this.amount = amount;
        this.safe = safe;
    }

    @Override
    public void run() {
        System.out.println(
                Thread.currentThread().getName() + " started");

        for (int i = 0; i < times; i++) {
            if (safe) {
                account.deposit(amount);
            } else {
                account.depositUnsafe(amount);
            }
        }

        System.out.println(
                Thread.currentThread().getName() + " finished");
    }
}

public class MiniBank {

    public static void main(String[] args)
            throws InterruptedException {

        int threadCount = 10;
        int times = 1000;
        long amount = 1;

        // Test 1: Without synchronization
        Account account1 = new Account();
        Thread[] threads1 = new Thread[threadCount];

        System.out.println("WITHOUT SYNCHRONIZATION");

        for (int i = 0; i < threadCount; i++) {
            threads1[i] = new Thread(
                    new AccountWorker(account1, times, amount, false),
                    "Unsafe-Thread-" + (i + 1));
            threads1[i].start();
        }

        for (Thread t : threads1) {
            t.join();
        }

        System.out.println("Expected balance: 10000");
        System.out.println("Actual balance: "
                + account1.getBalance());

        // Test 2: With synchronization
        Account account2 = new Account();
        Thread[] threads2 = new Thread[threadCount];

        System.out.println("\nWITH SYNCHRONIZATION");

        for (int i = 0; i < threadCount; i++) {
            threads2[i] = new Thread(
                    new AccountWorker(account2, times, amount, true),
                    "Safe-Thread-" + (i + 1));
            threads2[i].start();
        }

        for (Thread t : threads2) {
            t.join();
        }

        System.out.println("Expected balance: 10000");
        System.out.println("Actual balance: "
                + account2.getBalance());
    }
}
