
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Account {

    private final String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public synchronized void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public synchronized boolean withdraw(long amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }

        balance -= amount;
        return true;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public void transfer(Account to, long amount) {
        Account first = this;
        Account second = to;

        if (first.getAccountNumber().compareTo(
                second.getAccountNumber()) > 0) {
            first = to;
            second = this;
        }

        synchronized (first) {
            synchronized (second) {
                if (amount > 0 && balance >= amount) {
                    balance -= amount;
                    to.balance += amount;
                    System.out.println("Transfer completed: "
                            + accountNumber + " -> "
                            + to.accountNumber);
                }
            }
        }
    }
}

class TransactionProcessor {

    private final ExecutorService pool
            = Executors.newFixedThreadPool(4);

    public void submit(Runnable task) {
        pool.execute(task);
    }

    public void stop() throws InterruptedException {
        pool.shutdown();

        if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
            pool.shutdownNow();
        }
    }
}

class TransactionBuffer {

    private final int[] buffer = new int[5];
    private int count = 0;

    public synchronized void put(int value)
            throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }

        buffer[count++] = value;
        System.out.println("Produced transaction: " + value);

        notifyAll();
    }

    public synchronized int get()
            throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int value = buffer[0];

        for (int i = 1; i < count; i++) {
            buffer[i - 1] = buffer[i];
        }

        count--;
        notifyAll();

        return value;
    }
}

public class MiniBank {

    public static void main(String[] args)
            throws InterruptedException {

        Account a = new Account("AC0001", 10000);
        Account b = new Account("AC0002", 5000);

        System.out.println("=== THREAD POOL ===");

        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 0; i < 10; i++) {
            processor.submit(() -> a.deposit(100));
            processor.submit(() -> a.withdraw(50));
        }

        processor.stop();

        System.out.println("Account A balance: Rs. "
                + a.getBalance());

        // 2. Producer-consumer using wait() and notifyAll()
        System.out.println("\n=== PRODUCER-CONSUMER ===");

        TransactionBuffer buffer = new TransactionBuffer();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.put(i * 100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println(
                            "Processed transaction: " + buffer.get());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        // 3. Transfers with consistent lock ordering
        System.out.println("\n=== SAFE TRANSFERS ===");

        Thread t1 = new Thread(() -> a.transfer(b, 1000));
        Thread t2 = new Thread(() -> b.transfer(a, 500));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final A balance: Rs. " + a.getBalance());
        System.out.println("Final B balance: Rs. " + b.getBalance());

        System.out.println("MiniBank tasks completed.");
    }
}
