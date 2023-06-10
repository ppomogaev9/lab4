package factory.suppliers;

import factory.parts.Accessory;
import factory.storages.AccessoryStorage;

import static factory.params.Params.defaultSupplyingTime;
import static factory.params.Params.log;

public class AccessorySupplier implements Runnable{
    private AccessoryStorage storage;
    private static Integer supplyingDelay = defaultSupplyingTime;
    private static final Object monitor = new Object();
    private boolean work = true;

    private AccessorySupplier() {}

    public AccessorySupplier(AccessoryStorage storage){
        this.storage = storage;
    }

    public void run() {
        while (work) {
            synchronized (monitor) {
                try {
                    monitor.wait(supplyingDelay);
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in accessory supplier");
                }

                storage.store(new Accessory());
            }
        }
    }

    public void stop() {
        work = false;
    }

    public static void setSupplyingDelay(int delay){
        supplyingDelay = delay;
    }
}
