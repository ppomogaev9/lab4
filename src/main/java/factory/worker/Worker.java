package factory.worker;

import factory.car.Car;
import factory.storages.AccessoryStorage;
import factory.storages.BodyStorage;
import factory.storages.MotorStorage;
import factory.storages.CarStorage;

public class Worker implements Runnable {
    private static int numOfWorkers = 0;
    private CarStorage carStorage;
    private BodyStorage bodyStorage;
    private MotorStorage motorStorage;
    private AccessoryStorage accessoryStorage;
    private boolean stop = false;

    private Worker() {}

    public Worker(CarStorage carStorage, BodyStorage bodyStorage, MotorStorage motorStorage, AccessoryStorage accessoryStorage) {
        this.carStorage = carStorage;
        this.bodyStorage = bodyStorage;
        this.motorStorage = motorStorage;
        this.accessoryStorage = accessoryStorage;
    }

    public void run() {
        numOfWorkers++;
        carStorage.store(new Car(motorStorage.get(), bodyStorage.get(), accessoryStorage.get()));
        numOfWorkers--;
    }

    void stop() {
        stop = true;
    }
}
