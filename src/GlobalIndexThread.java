/*
    Project 6 (Advanced Multithreading in Java)
    Tyler Peters
    2017-12-4
    DS 730

    Each text files found at the input location is processed by
    this class (which is called from GlobalIndex.java).
 */

import java.io.*;
import java.util.*;

public class GlobalIndexThread extends Thread {

    private File file;
    private int numChars;

    public GlobalIndexThread(File file, int numChars) {
        this.file = file;
        this.numChars = numChars;
    }

    public void run() {
        try {
            // Instantiate a new data structure within which to store and order the index items:
            TreeMap<String, TreeSet<Integer>> index = new TreeMap<>();

            int charCount = 0;
            int page = 1;
            Scanner reader = new Scanner(new BufferedReader(new FileReader(file)));

            // Loop through the content within the file:
            while (reader.hasNext()) {
                String str = reader.next();
                if (charCount + str.length() <= numChars) {
                    charCount += str.length();
                    updateIndex(index, str, page);
                } else {
                    page++;
                    charCount = str.length();
                    updateIndex(index, str, page);
                }
            }

            // After reading the file, and populating the data structure, pass the file name and the index data
            // to the calling class.
            GlobalIndex.addIndex(file, index);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // This method adds an entry to the index if it does not exist; otherwise it appends the page number
    // to the existing entry:
    private static void updateIndex(TreeMap<String, TreeSet<Integer>> index, String str, int page) {
        if (index.containsKey(str.toLowerCase())) {
            index.get(str.toLowerCase()).add(page);
        } else {
            TreeSet<Integer> set = new TreeSet<>();
            set.add(page);
            index.put(str.toLowerCase(), set);
        }
    }
}
