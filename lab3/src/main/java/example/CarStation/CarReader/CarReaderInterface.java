package example.CarStation;

import java.io.IOException;
import java.util.List;
public interface CarReaderInterface {
    List<Car> readAll(String path) throws IOException;
    int rejected();
}