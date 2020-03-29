package Lesson1;

public class Robot implements Active {

    int runningLimit = 0;
    int jumpLimit = 0;

    public Robot(int runningLimit, int jumpLimit) {
        this.runningLimit = runningLimit;
        this.jumpLimit = jumpLimit;
    }

    @Override
    public int run() {
        return runningLimit;
    }

    @Override
    public int jump() {
        return jumpLimit;
    }
}
