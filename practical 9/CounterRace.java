public class CounterRace{

    static int counter = 0;

    static class MyThread extends Thread {
        public void run() {
            for (int i = 0; i < 100000; i++) {
                counter++;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Thread[] threads = new Thread[5];

        for (int i = 0; i < 5; i++) {
            threads[i] = new MyThread();
            threads[i].start();
        }

        for (int i = 0; i < 5; i++) {
            threads[i].join();
        }

        System.out.println("Counter = " + counter);
        System.out.println("Expected = " + (5 * 100000));
    }
}
