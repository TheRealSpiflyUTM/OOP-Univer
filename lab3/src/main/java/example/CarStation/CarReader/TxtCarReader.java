package example.CarStation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.CarStation.Car.CarType;
import example.CarStation.Car.Passengers;
import example.CarStation.Exceptions.InvalidCarException;
import example.Logger.LogLevel;
import example.Logger.Logger;

public class TxtCarReader implements CarReaderInterface{
    private final static int IMPLEMENTATIONFIELDS = 5;
    private final Logger log;
    private int rejected = 0;
    public TxtCarReader(Logger log){
        this.log = log;
    }
    

    public List<Car> readAll(String path) throws IOException{
        List<Car> cars = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line;
            while((line = reader.readLine()) != null){
                if(line.isBlank()) continue;
                try{
                    cars.add(parse(line.trim()));
                }catch (InvalidCarException e){
                    rejected ++;
                    log.log(LogLevel.ERROR, "Car was not allowed because {0}", e.getMessage());
                }
            }

        }catch(IOException e){
            log.log(LogLevel.ERROR, "File {0} has the error {1}", path, e.getMessage());
        }


        return cars;
    }
    public int rejected(){return rejected;}

    // Helper Functions
    private Car parse(String line) throws InvalidCarException {
        String[] words = line.split("\\s+");
        String id = words[0];

        if(words.length > IMPLEMENTATIONFIELDS) throw new InvalidCarException("Car " + id + ": has too many fields ");
        if(words.length < IMPLEMENTATIONFIELDS) throw new InvalidCarException("Car " + id + ": has needs more fields");


        int carId;
        try{
            carId = Integer.parseInt(words[0]);
        }catch(NumberFormatException e){
            throw new InvalidCarException("car " + id + ": id ain't a number");
        }

        if (words[1].equals("null")) throw new InvalidCarException("car " + id + ": no type");
        CarType carType;
        try{
            carType = CarType.valueOf(words[1]);
        }catch(IllegalArgumentException e){
            throw new InvalidCarException("car " + id + ": unknown type " + words[1]);
        }

        if (words[2].equals("null")) throw new InvalidCarException("car " + id + ": no passengers");
        Passengers passengers;
        try{
            passengers = Passengers.valueOf(words[2]);
        }catch(IllegalArgumentException e){
            throw new InvalidCarException("car " + id + ": unknown passengers " + words[2]);
        }

        boolean isDining;
        if(words[3].equals("true")){
            isDining = true;
        }
        else if (words[3].equals("false")){
            isDining = false;
        }
        else throw new InvalidCarException("car " + id + ": isDining must be true or false");

        int consumption;
        try{
            consumption = Integer.parseInt(words[4]);
        }catch(NumberFormatException e){
            throw new InvalidCarException("car " + id + ": consumption is not a number: " + words[4]);
        }
        if(consumption < 0) throw new InvalidCarException("car " + id + ": negative consuption:  " + words[4]);
        

        return new Car(carId, carType, passengers, isDining, consumption);
    }
}
