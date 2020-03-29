package Lesson1;

public class Wall implements Obstructive{

    private int height = 0;

    public Wall(int height) {
        this.height = height;
    }

    @Override
    public boolean doOvercome(Active entity) {
        return (entity.jump() >= height);
    }

    @Override
    public int getSize() {
        return height;
    }
}
