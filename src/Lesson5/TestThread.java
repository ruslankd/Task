package Lesson5;

public class TestThread {
    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;

    public static void main(String[] args) {
        System.out.println("Runtime: " + runtimeFunction() + System.lineSeparator());
        System.out.println("Runtime: " + multithreadingRuntimeFunction());
    }

    public static long runtimeFunction() {
        float[] arr = new float[SIZE];
        for (int i = 0; i < SIZE; i++) arr[i] = 1;

        long t1 = System.currentTimeMillis();

        float j;
        for (int i = 0; i < SIZE; i++) {
            j = (float) i;
            arr[i] = (float) (arr[i] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
        }

        long t2 = System.currentTimeMillis();

        System.out.printf("Test:%n" +
                        "\t0:\t\t\t%f%n" +
                        "\t1:\t\t\t%f%n" +
                        "\t...%n" +
                        "\t500000:\t\t%f%n" +
                        "\t500001:\t\t%f%n" +
                        "\t...%n" +
                        "\t999999:\t\t%f%n" +
                        "\t1000000:\t%f%n",
                        arr[0], arr[1], arr[500000],
                        arr[500001], arr[999999], arr[1000000]);

        return (t2 - t1);
    }

    public static long multithreadingRuntimeFunction() {
        float[] arr = new float[SIZE];
        float[] arr1 = new float[HALF];
        float[] arr2 = new float[HALF];

        for (int i = 0; i < SIZE; i++) arr[i] = 1;

        long t1 = System.currentTimeMillis();

        System.arraycopy(arr, 0, arr1, 0, HALF);
        System.arraycopy(arr, HALF, arr2, 0, HALF);

        Thread thread1 = new Thread(() -> arithmeticMethod(arr1, 0));
        Thread thread2 = new Thread(() -> arithmeticMethod(arr2, HALF));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.arraycopy(arr1, 0, arr, 0, HALF);
        System.arraycopy(arr2, 0, arr, HALF, HALF);

        long t2 = System.currentTimeMillis();

        System.out.printf("Test:%n" +
                        "\t0:\t\t\t%f%n" +
                        "\t1:\t\t\t%f%n" +
                        "\t...%n" +
                        "\t500000:\t\t%f%n" +
                        "\t500001:\t\t%f%n" +
                        "\t...%n" +
                        "\t999999:\t\t%f%n" +
                        "\t1000000:\t%f%n",
                        arr[0], arr[1], arr[500000],
                        arr[500001], arr[999999], arr[1000000]);

        return (t2 - t1);
    }

    public static void arithmeticMethod(float[] arr, int startIndex) {
        float j;
        for (int i = 0; i < HALF; i++) {
            j = (float) (i + startIndex);
            arr[i] = (float) (arr[i] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
        }
    }
}
