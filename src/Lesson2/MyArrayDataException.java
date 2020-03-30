package Lesson2;

public class MyArrayDataException extends Exception {
    public MyArrayDataException(int i, int j) {
        super("Incorrect data entered in cell (" + i + ", " + j + ")");
    }
}
