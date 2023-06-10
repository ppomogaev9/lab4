package factory.storages;

import factory.parts.Motor;

import static factory.params.Params.log;
import static factory.params.Params.motorStorageSize;

import java.util.Stack;


public class MotorStorage {
    private Stack<Motor> storage = new Stack<>();
    private Integer storageCapacity = 0;
    private static final Object monitor = new Object();
    private boolean work = true;

    public void store(Motor motor) {
        synchronized (monitor) {
            if (isFull() && work){
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in motor storage");
                }
            }

            monitor.notify();

            storage.add(motor);
            ++storageCapacity;
        }
    }

    public Motor get() {
        Motor motor;

        synchronized (monitor) {
            while (isEmpty()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in motor storage");
                }
            }

            if (isFull()) {
                monitor.notify();
            }

            motor = storage.pop();
            --storageCapacity;
        }

        return motor;
    }

    public Integer getCapacity() {
        return storageCapacity;
    }

    public void resetStorage() {
        storage.clear();
        storageCapacity = 0;
    }

    private boolean isEmpty() {
        return storage.isEmpty();
    }

    private boolean isFull() {
        return storageCapacity.equals(motorStorageSize);
    }

    public void stop() {
        work = false;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
}
