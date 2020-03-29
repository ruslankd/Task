package Lesson1;

public class Treadmill implements Obstructive {

    private int length = 0;

    public Treadmill(int length) {
        this.length = length;
    }

    @Override
    public boolean doOvercome(Active entity) {
        return (entity.run() >= length);
    }

    @Override
    public int getSize() {
        return length;
    }
}
