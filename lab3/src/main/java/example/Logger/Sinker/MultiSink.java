package example.Logger.Sinker;

import java.util.List;

public class MultiSink implements Sink{

    private final List <Sink> sinks;

    public MultiSink(List<Sink> sinks){
        this.sinks = sinks;
    }

    @Override 
    public void write(String msg){
        for(Sink sink : sinks){
            sink.write(msg);
        }
    }
}
