package example.Logger.Sinker;

public class ConsoleSink implements Sink  {
    
    public void write(String line) {
        System.out.println(line);
    }
}
