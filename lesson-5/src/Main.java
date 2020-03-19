import java.util.Arrays;

public class Main {
    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {
        slowMethod();
        try {
            fastMethod();
        } catch (InterruptedException e) {
            System.out.println("Прерывание дочернего потока");
        }

    }

    /**
     * Метод выполняет расчеты в одном потоке
     */
    private static void slowMethod () {
        float[] arr = createFillFloatArray(size, 1.0f);

        long a = System.currentTimeMillis();

        calculationMethod(arr, 0);

        System.out.printf("Время выполнения медленного метода составляет: %,d мс%n", System.currentTimeMillis() - a);
    }

    /**
     * Метод выполняет расчеты в двух потоках
     */
    private static void fastMethod () throws InterruptedException {
        float[] arr = createFillFloatArray(size, 1.0f);

        long a = System.currentTimeMillis();

        float[] tmpArr1 = new float[h];
        float[] tmpArr2 = new float[h];

        System.arraycopy(arr, 0, tmpArr1, 0, h);
        System.arraycopy(arr, h, tmpArr2, 0, h);

        Runnable r1 = () -> calculationMethod(tmpArr1, 0);
        Runnable r2 = () -> calculationMethod(tmpArr2, h);

        Thread t1 = new Thread(r1, "first half");
        Thread t2 = new Thread(r2, "second half");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.arraycopy(tmpArr1, 0, arr, 0, h);
        System.arraycopy(tmpArr2, 0, arr, h, h);

        System.out.printf("Время выполнения быстрого метода составляет: %,d мс%n", System.currentTimeMillis() - a);
    }

    /**
     * Метод создания заполненного массива
     * @param size - размер массива
     * @param value - значение для заполенния массива
     * @return float[] - заполенный массив
     */
    private static float[] createFillFloatArray (int size, final float value) {
        float[] arr = new float[size];
        Arrays.fill(arr, value);
        return arr;
    }

    /**
     * Метод выполняет полезные вычисления в массиве чисел с плавающей точкой
     * @param arr - ссылка на массив
     * @param offset - смещение, определяет исходное значение индекса для расчета
     */
    private static void calculationMethod (float[] arr, int offset) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + (float) offset / 5) * Math.cos(0.2f + (float) offset / 5) * Math.cos(0.4f + (float) offset / 2));
            offset++;
        }
    }
}
