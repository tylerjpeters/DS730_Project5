import java.util.*;
import java.io.*;

public class Index {

    public static String location = "Input";
    public static int numCharacters = 1000;

    public static void main(String[] args) throws Exception {

        TreeMap<String, TreeSet<Integer>> index = new TreeMap<>();

        // Create an array with File objects for each of the text files within the supplied directory:
        File path = new File(location);
        File[] files = path.listFiles((File filename) -> filename.getName().endsWith(".txt"));

        // Loop through each of the text files within the directory location:
        for (File f : files) {
            int charCount = 0;
            int page = 1;
            index.clear();
            Scanner reader = new Scanner(new BufferedReader(new FileReader(f)));
            // Loop through the content within the file:
            while (reader.hasNext()) {
                String str = reader.next();
                if (charCount + str.length() <= numCharacters) {
                    charCount += str.length();
                    if (index.containsKey(str.toLowerCase())) {
                        index.get(str.toLowerCase()).add(page);
                    } else {
                        TreeSet<Integer> set = new TreeSet<>();
                        set.add(page);
                        index.put(str.toLowerCase(), set);
                    }
                } else {
                    page++;
                    charCount = str.length();
                    if (index.containsKey(str.toLowerCase())) {
                        index.get(str.toLowerCase()).add(page);
                    } else {
                        TreeSet<Integer> set = new TreeSet<>();
                        set.add(page);
                        index.put(str.toLowerCase(), set);
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, TreeSet<Integer>> entry : index.entrySet()) {
                StringBuilder pages = new StringBuilder();
                sb.append(entry.getKey());
                if (entry.getValue() != null) {
                    for (Integer value : entry.getValue()) {
                        pages.append(", ").append(value.toString());
                    }
                    sb.append(pages.substring(1,pages.length()));
                }
                sb.append("\n");
            }

            String fileName = f.getParent() + "/" + f.getName().replace(".txt","") + "_output.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(sb.toString());
            writer.close();
        }
    }
}
