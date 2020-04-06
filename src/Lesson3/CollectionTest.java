package Lesson3;

import java.util.*;

public class CollectionTest {

    public static void main(String[] args) {
        arrayTest();
        phonebookTest();
    }

    public static void arrayTest() {
        String[] arrayWords = { "Forest", "Home", "Friend", "Water", "Man", "Home",
                                "Phone", "Man", "Home", "Car", "Street", "Friend",
                                "Home", "Forest", "Phone", "Friend" };
        System.out.println(Arrays.toString(arrayWords));

        Set<String> setWords = new HashSet<>(Arrays.asList(arrayWords));
        System.out.println(setWords);

        for (String setWord : setWords) {
            int number = 0;
            for (String arrayWord : arrayWords) {
                if (setWord.equals(arrayWord)) number++;
            }
            System.out.println(setWord + " : " + number);
        }
    }

    public static void phonebookTest() {
        Phonebook phonebook = new Phonebook();
        phonebook.add("Ivanov", "363");
        phonebook.add("Smirnov", "672");
        phonebook.add("Ivanov", "486");
        System.out.println("Ivanov : " + Arrays.toString(phonebook.get("Ivanov")));
        System.out.println("Smirnov : " + Arrays.toString(phonebook.get("Smirnov")));
        phonebook.add("Smirnov", "649", "275");
        System.out.println("Smirnov : " + Arrays.toString(phonebook.get("Smirnov")));
    }
}
