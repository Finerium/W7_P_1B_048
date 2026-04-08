// Solusi race condition dengan synchronized pada method serveCustomer
class Resto {
    private int chickenStock;

    public Resto(int stock) {
        this.chickenStock = stock;
    }

    // synchronized memastikan hanya satu kasir yang bisa eksekusi method ini sekaligus
    public synchronized void serveCustomer(String kasir) throws InterruptedException {
        if (chickenStock > 0) {
            System.out.println(kasir + " melayani pelanggan, stok sebelum: " + chickenStock);
            Thread.sleep(10);
            chickenStock--;
            System.out.println(kasir + " berhasil melayani, stok sekarang: " + chickenStock);
        } else {
            System.out.println(kasir + ": stok habis, tidak bisa melayani");
        }
    }

    public int getStock() { return chickenStock; }
}

public class RestoSimulasi {

    public static void main(String[] args) throws InterruptedException {
        Resto ayamJuicyLuicyGallagher = new Resto(3);

        // Lima kasir mencoba melayani secara bersamaan
        Thread[] kasirList = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final String namaKasir = "Kasir-" + (i + 1);
            kasirList[i] = new Thread(() -> {
                try {
                    ayamJuicyLuicyGallagher.serveCustomer(namaKasir);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (Thread t : kasirList) t.start();
        for (Thread t : kasirList) t.join();

        System.out.println("HASIL AKHIR - Stok tersisa: " + ayamJuicyLuicyGallagher.getStock());
    }
}
