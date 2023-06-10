package factory.suppliers;

import factory.parts.Motor;
import factory.storages.MotorStorage;

import static factory.params.Params.defaultSupplyingTime;
import static factory.params.Params.log;

public class MotorSupplier implements Runnable {
    private MotorStorage storage;
    private static Integer supplyingDelay = defaultSupplyingTime;
    private static final Object monitor = new Object();
    private boolean work = true;

    private MotorSupplier() {}

    public MotorSupplier(MotorStorage storage) {
        this.storage = storage;
    }

    public void run() {
        while (work) {
            synchronized (monitor) {
                try {
                    monitor.wait(supplyingDelay);
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in motor supplier");
                }

                storage.store(new Motor());
            }
        }
    }

    public void stop(){
        work = false;
    }

    public static void setSupplyingDelay(int delay) {
        supplyingDelay = delay;
    }
}
