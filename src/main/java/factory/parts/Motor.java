package factory.parts;

public class Motor {
    private static Integer nextID = 0;
    private final int ID;

    public Motor() {
        ID = nextID;
        ++nextID;
    }

    public int getID() {
        return ID;
    }
}
