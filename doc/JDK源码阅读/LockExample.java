/**
 * 死锁 实现
 * 其实就是竞争统一资源
 */
public class LockExample {
    public static void main(String[] args) {
        Object var1 = new Objcet();
        Object var2 = new Objcet();
        new Thread(() -> {
            synchronized (var1) {
                System.out.println("获取 var1 成功");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 试图获取锁 lock2
                synchronized (var2) {
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (var2) {
                System.out.println("获取 var2 成功");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 试图获取锁 lock2
                synchronized (var1) {
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }).start();
    }
}