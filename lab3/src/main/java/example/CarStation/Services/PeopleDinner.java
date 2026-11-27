package example.CarStation.Services;

import example.CarStation.Car;
import example.CarStation.Car.Passengers;
import example.Logger.LogLevel;
import example.Logger.Logger;

public class PeopleDinner implements Dineable {
    private final Stats stats; private final Logger log;
    public PeopleDinner(Stats s, Logger l) { 
        stats = s; 
        log = l; 
    }
    public boolean accepts(Passengers p) { return p == Passengers.PEOPLE; }
    public void serveDinner(Car c) {
        log.log(LogLevel.INFO, "Serving dinner to people in car " + c.id());
        stats.diningPeople++;
    }
}