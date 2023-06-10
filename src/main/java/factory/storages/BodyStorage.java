package factory.storages;

import factory.parts.Body;
import static factory.params.Params.bodyStorageSize;
import static factory.params.Params.log;

import java.util.Stack;

public class BodyStorage {
    private Stack<Body> storage = new Stack<>();
    private Integer storageCapacity = 0;
    private static final Object monitor = new Object();
    private boolean work = true;

    public void store(Body body) {
        synchronized (monitor) {
            while (isFull() && work) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in body storage");
                }
            }

            storage.add(body);
            ++storageCapacity;

            monitor.notify();
        }
    }
    public Body get() {
        Body body;

        synchronized (monitor) {
            while (isEmpty()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in body storage");
                }
            }

            if (isFull()){
                monitor.notify();
            }

            body = storage.pop();
            --storageCapacity;
        }

        return body;
    }

    public void resetStorage() {
        storage.clear();
        storageCapacity = 0;
    }

    public Integer getCapacity() {
        return storageCapacity;
    }

    private boolean isFull() {
        return storageCapacity.equals(bodyStorageSize);
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
