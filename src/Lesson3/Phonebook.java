package Lesson3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Phonebook {
    private HashMap<String, HashSet<String>> records;

    public Phonebook() {
        records = new HashMap<>();
    }
    public String[] get(String surname) {
        Set<String> set = records.get(surname);
        return set.toArray(new String[set.size()]);
    }

    public void add(String surname, String ... phone) {
        if (!records.containsKey(surname)) {
            HashSet<String> phoneAdd = new HashSet<>(Arrays.asList(phone));
            records.put(surname, phoneAdd);
        } else {
            records.get(surname).addAll(Arrays.asList(phone));
        }
    }
}
