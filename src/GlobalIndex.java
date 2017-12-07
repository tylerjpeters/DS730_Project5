/*
    Project 6 (Advanced Multithreading in Java)
    Tyler Peters
    2017-12-1
    DS 730
 */

import java.io.*;
import java.util.*;

public class GlobalIndex {

    // Stores the file name and related index (which includes the word and pages of occurence):
    private static TreeMap<File, TreeMap<String, TreeSet<Integer>>> fileIndices = new TreeMap<>();

    public static void main(String[] args) throws Exception {

        // Collect the start time of the program:
        final long startTime = System.currentTimeMillis();

        // Parse and assign the input variables:
        String location = args[0];
        int numCharacters = Integer.parseInt(args[1]);

        // Load the input file paths into a list:
        File path = new File( new File(location).getAbsolutePath() );
        File[] inputFiles = path.listFiles((File filename) -> filename.getName().endsWith(".txt"));
        // Create an array of uninstantiated IndexThread objects (one for each file):
        GlobalIndexThread[] indices = new GlobalIndexThread[inputFiles.length];

        // Instantiate each of the IndexThread objects with the file name and character count:
        for (int i = 0; i < inputFiles.length; i++) {
            indices[i] = new GlobalIndexThread(inputFiles[i], numCharacters);
            indices[i].start();
        }

        // Test and join the threads:
        for (GlobalIndexThread index : indices) {
            if (index.isAlive()) {
                index.join();
            }
        }

        // Build a structure ready to store the processed index prior to printing/writing:
        TreeMap<String, ArrayList<StringBuilder>> finalIndex = new TreeMap<>();

        // Write all of the words/keys to the final object, and instantiate a new array of strings:
        for (Map.Entry<File, TreeMap<String, TreeSet<Integer>>> file : fileIndices.entrySet()) {
            for (Map.Entry<String, TreeSet<Integer>> entry : file.getValue().entrySet()) {
                if (!finalIndex.containsKey(entry.getKey().toLowerCase())) {
                    finalIndex.put(entry.getKey().toLowerCase(), new ArrayList<>());
                }
            }
        }

        // Process each object in fileIndices. If it contains a given key, add the value to the array.
        // Otherwise add an empty string.
        for (Map.Entry<File, TreeMap<String, TreeSet<Integer>>> file : fileIndices.entrySet()) {
            for (Map.Entry<String, ArrayList<StringBuilder>> entry : finalIndex.entrySet()) {
                if (file.getValue().containsKey(entry.getKey())) {
                    StringBuilder val = new StringBuilder();
                    val.append(joinInts(file.getValue().get(entry.getKey()), ":"));
                    entry.getValue().add(val);
                }
                else {
                    StringBuilder filler = new StringBuilder();
                    entry.getValue().add(filler);
                }
            }
        }

        // Process and print the global index:

        // 1. Collect and store the header row:
        StringBuilder header = new StringBuilder();
        header.append("Word");
        for (Map.Entry<File, TreeMap<String, TreeSet<Integer>>> entry : fileIndices.entrySet()) {
            header.append(", ").append(entry.getKey().getName());
        }
        header.append("\n");

        // 2. Create each row of content:
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, ArrayList<StringBuilder>> entry : finalIndex.entrySet()) {
            content.append(entry.getKey());
            for (StringBuilder sb : entry.getValue()) {
                content.append(", ").append(sb);
            }
            content.append("\n");
        }

        // 3. Write the index to a text file:
        try {
            // Build the output filepath:
            File parent = new File(path.getParent());
            String fileName = parent.getAbsolutePath() + "/output.txt";

            // Open a buffered file writer at the output location:
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            // Write the index to the file:
            writer.write(header.toString());
            writer.write(content.toString());
            // Close the writer:
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Print the runtime of the program to the console window (in milliseconds):
        System.out.println(System.currentTimeMillis() - startTime);
    }

    // Adds the object produced by a given thread to the master object:
    public synchronized static void addIndex(File fileName, TreeMap<String, TreeSet<Integer>> index) {
        if (!fileIndices.containsKey(fileName)) {
            fileIndices.put(fileName, index);
        } else {
            System.out.println("There exist files with the same name in the input location.");
        }
    }

    // Concatenates page numbers, separated by a given delimiter:
    private static String joinInts(TreeSet<Integer> vals, String delim) {
        StringBuilder output = new StringBuilder();
        for (Integer val : vals) {
            output.append(delim).append(val.toString());
        }
        return output.substring(1, output.length());
    }
}
