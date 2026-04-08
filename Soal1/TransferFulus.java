// Solusi deadlock menggunakan lock ordering berdasarkan identity hash code
class Account {
    private String name;
    private int balance;

    public Account(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() { return name; }
    public int getBalance() { return balance; }

    public void debit(int amount) { balance -= amount; }
    public void credit(int amount) { balance += amount; }
}

public class TransferFulus {

    // Transfer dengan lock ordering: kunci hash lebih kecil duluan
    public static void transfer(Account from, Account to, int amount) throws InterruptedException {
        Account first, second;

        // Tentukan urutan penguncian berdasarkan identity hash code
        if (System.identityHashCode(from) < System.identityHashCode(to)) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        synchronized (first) {
            System.out.println(Thread.currentThread().getName()
                + " mengunci " + first.getName() + ", menunggu " + second.getName());
            Thread.sleep(100);
            synchronized (second) {
                from.debit(amount);
                to.credit(amount);
                System.out.println(Thread.currentThread().getName()
                    + " transfer Rp" + amount
                    + " dari " + from.getName() + " ke " + to.getName() + " berhasil");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account("Rekening-A", 1000);
        Account acc2 = new Account("Rekening-B", 1000);

        Thread t1 = new Thread(() -> {
            try {
                transfer(acc1, acc2, 200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            try {
                transfer(acc2, acc1, 300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Saldo akhir " + acc1.getName() + ": Rp" + acc1.getBalance());
        System.out.println("Saldo akhir " + acc2.getName() + ": Rp" + acc2.getBalance());
    }
}
