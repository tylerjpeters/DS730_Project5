/*
    Project 5 (Mulithreading in Java)
    Tyler Peters
    2017-12-1
    DS 730

    Multi-threaded program that accepts the location of an
    input directory followed by the number of characters per page.

    Each text files found at the input location will be processed on
    a dedicated thread (defined within IndexThread.java).
 */

import java.io.*;

public class IndexThreads {
    public static void main (String[] args) throws Exception {

        // Collect the start time of the program:
        final long startTime = System.currentTimeMillis();

        // Parse and assign the input variables:
        String location = args[0];
        int numCharacters = Integer.parseInt(args[1]);

        // Load the input file paths into a list:
        File path = new File(location);
        File[] inputFiles = path.listFiles((File filename) -> filename.getName().endsWith(".txt"));
        // Create an array of uninstantiated IndexThread objects (one for each file):
        IndexThread[] indices = new IndexThread[inputFiles.length];

        // Instantiate each of the IndexThread objects with the file name and character count:
        for (int i = 0; i < inputFiles.length; i++) {
            indices[i] = new IndexThread(inputFiles[i], numCharacters);
            indices[i].start();
        }

        // Test and join the threads:
        for (IndexThread index : indices) {
            if (index.isAlive()) {
                index.join();
            }
        }

        // Print the runtime of the program to the console window (in milliseconds):
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
