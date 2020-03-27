package Lesson1;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random r = new Random();

        Obstructive[] obstructives = new Obstructive[10];
        Active[] actives = new Active[10];

        for (int i = 0; i < 10; i++) {
            int size;
            if (i % 2 == 0) {
                size = 110 - r.nextInt(20);
                obstructives[i] = new Wall(size);
            } else {
                size = 600 - r.nextInt(200);
                obstructives[i] = new Treadmill(size);
            }
            int height, length;
            switch (i % 3) {
                case 0:
                    height = 110 - r.nextInt(20);
                    length = 600 - r.nextInt(200);
                    actives[i] = new Man(length, height);
                    break;
                case 1:
                    height = 120 - r.nextInt(20);
                    length = 700 - r.nextInt(200);
                    actives[i] = new Robot(length, height);
                    break;
                case 2:
                    height = 100 - r.nextInt(20);
                    length = 500 - r.nextInt(200);
                    actives[i] = new Cat(length, height);
            }
        }

        for (Active active : actives) {
            System.out.print(active.getClass().getSimpleName() + " overcame : ");
            for (Obstructive obstructive : obstructives) {
                if (obstructive.doOvercome(active)) {
                    System.out.print(obstructive.getClass().getSimpleName() + " " + obstructive.getSize() + " ");
                } else {
                    break;
                }
            }
            System.out.println();
        }

    }
}
