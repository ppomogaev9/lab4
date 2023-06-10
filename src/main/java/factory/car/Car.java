package factory.car;

import factory.parts.Accessory;
import factory.parts.Body;
import factory.parts.Motor;

public class Car {
    private Body body;
    private Motor motor;
    private Accessory accessory;
    private static int nextID = 0;
    private int ID;

    private Car() {}

    public Car(Motor motor, Body body, Accessory accessory){
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
        ID = nextID;
        nextID++;
    }

    public String getCarInfo(){
        return "Auto " + ID + " (Body: " + body.getID() + ", Motor: " + motor.getID() + ", Accessory: " + accessory.getID() + ")";
    }

    public static Integer lastCarId(){
        return nextID;
    }

    public static void resetID(){
        nextID = 0;
    }
}
