import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class PenjumlahanParalel {

    public static void main(String[] args) throws InterruptedException {
        int jumlahThread;
        long angkaAkhir;

        // Baca input dari Scanner
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Masukkan jumlah thread: ");
            jumlahThread = sc.nextInt();
            System.out.print("Masukkan angka akhir: ");
            angkaAkhir = sc.nextLong();
        }

        AtomicLong total = new AtomicLong(0);
        long rangePerThread = angkaAkhir / jumlahThread;

        Thread[] threads = new Thread[jumlahThread];

        for (int i = 0; i < jumlahThread; i++) {
            final long start = (long) i * rangePerThread + 1;
            // Thread terakhir mendapat sisa range
            final long end = (i == jumlahThread - 1) ? angkaAkhir : start + rangePerThread - 1;
            final int threadIndex = i + 1;

            threads[i] = new Thread(() -> {
                System.out.println("Thread-" + threadIndex + " mengerjakan range: " + start + " - " + end);
                long partial = 0;
                for (long j = start; j <= end; j++) {
                    partial += j;
                }
                System.out.println("Thread-" + threadIndex + " hasil parsial: " + partial);
                // Akumulasi thread-safe menggunakan AtomicLong
                total.addAndGet(partial);
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("HASIL AKHIR - total penjumlahan 1 s/d " + angkaAkhir + ": " + total.get());
    }
}
