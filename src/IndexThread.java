/*
    Project 5 (Mulithreading in Java)
    Tyler Peters
    2017-12-1
    DS 730

    Each text files found at the input location is processed by
    this class (which is called from IndexThreads.java).
 */

import java.io.*;
import java.util.*;

public class IndexThread extends Thread {

    private File file;
    private int numChars;

    public IndexThread(File file, int numChars) {
        this.file = file;
        this.numChars = numChars;
    }

    public void run() {
        try {
            // Instantiate a new data structure within which to store and order the index items:
            TreeMap<String, TreeSet<Integer>> index = new TreeMap<>();

            int charCount = 0;
            int page = 1;
            index.clear();
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
            // For each entry in the TreeMap (index), build a string in the specified format:
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, TreeSet<Integer>> entry : index.entrySet()) {
                StringBuilder pages = new StringBuilder();
                sb.append(entry.getKey());
                if (entry.getValue() != null) {
                    for (Integer value : entry.getValue()) {
                        pages.append(", ").append(value.toString());
                    }
                    sb.append(pages.substring(1, pages.length()));
                }
                sb.append("\n");
            }
            // Build the output filename:
            String fileName = file.getParent() + "/output/" + file.getName().replace(".txt", "") + "_output.txt";
            // Select the path to the output directory
            File output = new File(new File(fileName).getParent());
            // If the output directory does not exist, make it:
            if (!output.exists() || !output.isDirectory()) {
                output.mkdirs();
            }

            // Open a buffered file writer at the output location:
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            // Write the index to the file:
            writer.write(sb.toString());
            // Close the writer:
            writer.close();
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
