package example.CarStation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import example.CarStation.Car.CarType;
import example.CarStation.Car.Passengers;
import example.CarStation.Exceptions.InvalidCarException;
import example.Logger.LogLevel;
import example.Logger.Logger;

public class JsonCarReader implements CarReaderInterface{
    private final Logger log;
    private int rejected = 0;
    public JsonCarReader(Logger log){
        this.log = log;
    }


    public List<Car> readAll(String path) throws IOException{
        List<Car> cars = new ArrayList<>();

        try{
            JSONArray array = new JSONArray(Files.readString(Path.of(path)));

            for(int i = 0; i < array.length(); i++){
                try{
                    Object item = array.opt(i);
                    if(!(item instanceof JSONObject)){
                        throw new InvalidCarException("Entry " + i + ": is not an object");
                    }
                    cars.add(parse((JSONObject) item));
                }catch (InvalidCarException e){
                    rejected ++;
                    log.log(LogLevel.ERROR, "Car was not allowed because {0}", e.getMessage());
                }
            }

        }catch(JSONException e){
            log.log(LogLevel.ERROR, "File {0} is not a valid json array: {1}", path, e.getMessage());
        }catch(IOException e){
            log.log(LogLevel.ERROR, "File {0} has the error {1}", path, e.getMessage());
        }


        return cars;
    }
    public int rejected(){return rejected;}

    // Helper Functions
    private Car parse(JSONObject object) throws InvalidCarException {
        Object idValue = object.opt("id");
        if(!(idValue instanceof Integer)){
            throw new InvalidCarException("Car " + idValue + ": id is missing or ain't a number");
        }
        int carId = (Integer) idValue;
        String id = String.valueOf(carId);

        Object typeValue = object.opt("type");
        if(typeValue == null || JSONObject.NULL.equals(typeValue)){
            throw new InvalidCarException("car " + id + ": no type");
        }
        CarType carType;
        try{
            carType = CarType.valueOf(typeValue.toString());
        }catch(IllegalArgumentException e){
            throw new InvalidCarException("car " + id + ": unknown type " + typeValue);
        }

        Object passengersValue = object.opt("passengers");
        if(passengersValue == null || JSONObject.NULL.equals(passengersValue)){
            throw new InvalidCarException("car " + id + ": no passengers");
        }
        Passengers passengers;
        try{
            passengers = Passengers.valueOf(passengersValue.toString());
        }catch(IllegalArgumentException e){
            throw new InvalidCarException("car " + id + ": unknown passengers " + passengersValue);
        }

        Object diningValue = object.opt("isDining");
        if(diningValue == null){
            throw new InvalidCarException("car " + id + ": isDining is missing");
        }
        boolean isDining;
        if(Boolean.TRUE.equals(diningValue)){
            isDining = true;
        }
        else if(Boolean.FALSE.equals(diningValue)){
            isDining = false;
        }
        else{
            throw new InvalidCarException("car " + id + ": isDining must be true or false");
        }

        Object consumptionValue = object.opt("consumption");
        if(consumptionValue == null){
            throw new InvalidCarException("car " + id + ": consumption is missing");
        }
        if(!(consumptionValue instanceof Integer)){
            throw new InvalidCarException("car " + id + ": consumption is not a number: " + consumptionValue);
        }
        int consumption = (Integer) consumptionValue;
        if(consumption < 0){
            throw new InvalidCarException("car " + id + ": negative consumption: " + consumption);
        }


        return new Car(carId, carType, passengers, isDining, consumption);
    }
}