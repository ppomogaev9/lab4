package factory.dealer;

import factory.car.Car;
import factory.storages.CarStorage;

import static factory.params.Params.defaultSupplyingTime;
import static factory.params.Params.log;

public class Dealer implements Runnable {
    private CarStorage storage;
    private int ID;
    private static int lastID = 0;
    private static Integer buyingDelay = defaultSupplyingTime;
    private boolean work = true;
    private static final Object monitor = new Object();

    private Dealer() {}

    public Dealer(CarStorage newStorage){
        lastID++;
        ID = lastID;
        storage = newStorage;
    }

    public void run() {
        while (work) {
            synchronized (monitor) {
                try {
                    monitor.wait(buyingDelay);
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in Dealer");
                }

                Car car = storage.get();
                if (work) {
                    log.info("Dealer: " + ID + ", " + car.getCarInfo());
                }
            }
        }
    }

    public void stop() {
        work = false;
    }

    public static void setBuyingDelay (int delay) {
        buyingDelay = delay;
    }

    public static void resetID() {
        lastID = 0;
    }
}
