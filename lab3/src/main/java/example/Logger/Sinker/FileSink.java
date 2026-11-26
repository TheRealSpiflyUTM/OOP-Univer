package example.Logger.Sinker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSink implements Sink{
    private static String defaultPath = "Here";
    private File writeFile;

    public FileSink(String writeFileName){
        this.writeFile = new File(writeFileName);
    }
    public FileSink(){
        this.writeFile = new File(defaultPath);
    }

    public void write(String msg) {
        try (PrintWriter out = new PrintWriter(new FileWriter(writeFile, true))) {
            out.println(msg);
        } catch (IOException e) {
            System.err.println("FileSink: could not write to " + writeFile.getPath() + " -> " + e.getMessage());
        }
    }

    public File getWriteFile() {
        return writeFile;
    }
    public void setWriteFile(String writeFileName) {
        this.writeFile = new File(writeFileName);
    }

    public static void setDefaultPath(String defaultPath) {
        FileSink.defaultPath = defaultPath;
    }
    public static String getDefaultPath() {
        return defaultPath;
    }
}
