package factory.storages;

import factory.fabric.Fabric;

public class CarStorageController {
    Fabric fabric;

    public void setFabric(Fabric factory){
        fabric = factory;
    }

    public void makeCar(){
        fabric.makeCar();
    }
}
