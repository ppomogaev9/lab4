package factory.fabric;

import factory.storages.AccessoryStorage;
import factory.suppliers.AccessorySupplier;
import factory.storages.BodyStorage;
import factory.suppliers.BodySupplier;
import factory.car.Car;
import factory.storages.CarStorage;
import factory.storages.CarStorageController;
import factory.worker.Worker;
import factory.storages.MotorStorage;
import factory.suppliers.MotorSupplier;
import factory.dealer.Dealer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static factory.params.Params.*;

public class Fabric {
    private MotorStorage motorStorage = new MotorStorage();
    private MotorSupplier motorSupplier = new MotorSupplier(motorStorage);
    private Thread motorSupplierThread = new Thread(motorSupplier);
    private CarStorageController controller;
    private CarStorage carStorage;
    private AccessoryStorage accessoryStorage = new AccessoryStorage();
    private BodyStorage bodyStorage = new BodyStorage();
    private BodySupplier bodySupplier = new BodySupplier(bodyStorage);
    private Thread bodySupplierThread = new Thread(bodySupplier);
    private List<AccessorySupplier> accsSuppliersList = new ArrayList<>();
    private List<Thread> accsSuppliersThreadList = new ArrayList<>();
    private List<Dealer> dealerList = new ArrayList<>();
    private List<Thread> dealersThreadList = new ArrayList<>();
    private final ExecutorService workers = Executors.newFixedThreadPool(workersNum);
    private boolean isStarted = false;

    public Fabric() {
        for (int i = 0; i < suppliersNum; ++i) {
            accsSuppliersList.add(new AccessorySupplier(accessoryStorage));
            accsSuppliersThreadList.add(new Thread(accsSuppliersList.get(i)));
        }

        controller = new CarStorageController();
        controller.setFabric(this);

        carStorage = new CarStorage(controller);

        Dealer.resetID();

        for (int i = 0; i < dealersNum; ++i) {
            dealerList.add(new Dealer(carStorage));
            dealersThreadList.add(new Thread(dealerList.get(i)));
        }
    }

    public void makeCar() {
        workers.execute(new Worker(carStorage, bodyStorage, motorStorage, accessoryStorage));
    }

    public void start() {
        if (isStarted) {
            return;
        }

        accessoryStorage.resetStorage();
        bodyStorage.resetStorage();
        motorStorage.resetStorage();
        Car.resetID();
        isStarted = true;
        motorSupplierThread.start();
        bodySupplierThread.start();

        for (Thread supplier : accsSuppliersThreadList) {
            supplier.start();
        }

        for (Thread dealer : dealersThreadList) {
            dealer.start();
        }

    }

    public void stop() {
        if (!isStarted) {
            return;
        }

        for (Dealer dealer : dealerList){
            dealer.stop();
        }
        carStorage.stop();
        motorSupplier.stop();
        motorStorage.stop();
        bodySupplier.stop();
        bodyStorage.stop();
        for (AccessorySupplier supplier : accsSuppliersList){
            supplier.stop();
        }
        accessoryStorage.stop();

        try {
            workers.shutdown();
        }
        catch (RejectedExecutionException ex){
            log.info("REE exception");
        }

        isStarted = false;
    }

    public List<Integer> getInfo() {
        List<Integer> info = new ArrayList<>();

        BlockingQueue<Runnable> queue = ((ThreadPoolExecutor)workers).getQueue();
        int size = queue.size();
        int tasksToDo = size;
        info.add(tasksToDo);
        info.add(Car.lastCarId());
        info.add(motorStorage.getCapacity());
        info.add(bodyStorage.getCapacity());
        info.add(accessoryStorage.getCapacity());

        return info;
    }
    public boolean isStarted(){
        return isStarted;
    }
}
