package Lesson2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ExceptionTest {

    private static int dimension;

    public static void main(String[] args) {

        System.out.print("Enter the dimension of the array: ");

        try (Scanner s = new Scanner(System.in)) {

            dimension = s.nextInt();
            String[][] array = new String[dimension][dimension];

            System.out.println("Enter the elements of the array: ");
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    System.out.print(i + " " + j + ": ");
                    array[i][j] = s.next();
                }
            }
            System.out.println("The sum of the elements in the array is " + summation(array));

        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int summation(String[][] array) throws MyArraySizeException, MyArrayDataException {
        if (dimension != 4) throw new MyArraySizeException();
        int sum = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException(i, j);
                }
            }
        }
        return sum;
    }
}
