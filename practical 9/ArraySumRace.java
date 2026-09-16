public class ArraySumRace {

    static int[] numbers = new int[100000];
    static long total = 0;

    static class SumThread extends Thread {

        int start, end;

        SumThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {

            for (int i = start; i < end; i++) {
                total += numbers[i];
            }
        }
    }

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = 1;
        }

        Thread t1 = new SumThread(0, 25000);
        Thread t2 = new SumThread(25000, 50000);
        Thread t3 = new SumThread(50000, 75000);
        Thread t4 = new SumThread(75000, 100000);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("Total = " + total);
        System.out.println("Expected = 100000");
    }
}