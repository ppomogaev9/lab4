package factory.suppliers;

import factory.parts.Body;
import factory.storages.BodyStorage;

import static factory.params.Params.defaultSupplyingTime;
import static factory.params.Params.log;

public class BodySupplier implements Runnable{
    private BodyStorage storage;
    private static Integer supplyingDelay = defaultSupplyingTime;
    private static final Object monitor = new Object();
    private boolean work = true;

    private BodySupplier() {}

    public BodySupplier(BodyStorage storage) {
        this.storage = storage;
    }

    public void run() {
        while (work) {
            synchronized (monitor) {
                try {
                    monitor.wait(supplyingDelay);
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in body supplier");
                }

                storage.store(new Body());
            }
        }
    }

    public void stop() {
        work = false;
    }

    public static void setSupplyingDelay(int delay) {
        supplyingDelay = delay;
    }
}
