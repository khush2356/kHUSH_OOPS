import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    static Queue<Integer> buffer = new LinkedList<>();
    static int capacity = 3;

    static class Producer extends Thread {
        public void run() {

            for (int i = 1; i <= 10; i++) {

                synchronized (buffer) {

                    while (buffer.size() == capacity) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    buffer.add(i);
                    System.out.println("Produced: " + i);

                    buffer.notify();
                }
            }
        }
    }

    static class Consumer extends Thread {
        public void run() {

            for (int i = 1; i <= 10; i++) {

                synchronized (buffer) {

                    while (buffer.isEmpty()) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int item = buffer.remove();
                    System.out.println("Consumed: " + item);

                    buffer.notify();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Finished.");
    }
}