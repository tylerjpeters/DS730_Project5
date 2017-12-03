import java.io.*;

public class IndexThread extends Thread {

    private File file;
    private int numChars;

    public IndexThread (File file, int numChars) {
        this.file = file;
        this.numChars = numChars;
    }

    public void run() {

    }

}
