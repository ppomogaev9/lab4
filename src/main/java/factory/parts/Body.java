package factory.parts;

public class Body {
    private static Integer nextID = 0;
    private final int ID;

    public Body() {
        ID = nextID;
        ++nextID;
    }

    public int getID() {
        return ID;
    }
}
