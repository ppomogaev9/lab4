package factory.storages;

import factory.parts.Accessory;
import static factory.params.Params.accessoryStorageSize;
import static factory.params.Params.log;

import java.util.Stack;


public class AccessoryStorage {
    private Stack<Accessory> storage = new Stack<>();
    private Integer storageCapacity = 0;
    private static final Object monitor = new Object();
    private boolean work = true;

    public void store(Accessory accessory) {
        synchronized (monitor) {
            while (isFull() && work) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in accessory storage");
                }
            }

            monitor.notify();

            ++storageCapacity;
            storage.add(accessory);
        }
    }
    public Accessory get() {
        Accessory accessory;

        synchronized (monitor) {
            while (isEmpty()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in accessory storage");
                }
            }

            if (isFull()){
                monitor.notifyAll();
            }

            accessory = storage.pop();
            --storageCapacity;
        }

        return accessory;
    }

    public Integer getCapacity(){
        return storageCapacity;
    }

    public void resetStorage() {
        storage.clear();
        storageCapacity = 0;
    }

    private boolean isFull() {
        return storageCapacity.equals(accessoryStorageSize);
    }

    private boolean isEmpty() {
        return storage.isEmpty();
    }

    public void stop() {
        work = false;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
}
