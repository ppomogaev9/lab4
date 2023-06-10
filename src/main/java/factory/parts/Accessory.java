package factory.parts;

public class Accessory {
    static private int nextID = 0;
    private final int ID;

    public Accessory() {
        ID = nextID;
        ++nextID;
    }

    public int getID() {
        return ID;
    }
}
