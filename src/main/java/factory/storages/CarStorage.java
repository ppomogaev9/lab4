package factory.storages;

import factory.car.Car;
import static factory.params.Params.autoStorageSize;
import static factory.params.Params.log;

import java.util.Stack;


public class CarStorage {
    private Stack<Car> storage = new Stack<>();
    private Integer storageCapacity = 0;
    private CarStorageController controller;
    private static final Object monitor = new Object();
    private boolean work = true;

    private CarStorage() {}

    public CarStorage(CarStorageController controller){
        this.controller = controller;
    }

    public void store(Car car) {
        synchronized (monitor) {
            while (isFull() && work) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in car storage");
                }
            }

            boolean empty = isEmpty();

            storage.add(car);
            ++storageCapacity;

            if (empty){
                monitor.notify();
            }
        }
    }

    public Car get() {
        Car car;

        synchronized (monitor) {
            while (isEmpty() && work) {
                try {
                    controller.makeCar();
                    monitor.wait();
                } catch (InterruptedException e) {
                    log.info("Interrupted Exception in car storage");
                }
            }

            if (!work) {
                return null;
            }

            car = storage.pop();
            --storageCapacity;

            if (isFull()) {
                monitor.notify();
            }
        }

        return car;
    }

    private boolean isEmpty() {
        return storage.isEmpty();
    }

    private boolean isFull() {
        return storageCapacity.equals(autoStorageSize);
    }

    public void stop() {
        work = false;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
}
