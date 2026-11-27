package example.CarStation.Services;

import example.CarStation.Car;
import example.CarStation.Car.Passengers;
import example.Logger.LogLevel;
import example.Logger.Logger;

public class RobotDinner implements Dineable {
    private final Stats stats; private final Logger log;
    public RobotDinner(Stats s, Logger l) { 
        stats = s; 
        log = l; 
    }
    public boolean accepts(Passengers p) { return p == Passengers.ROBOTS; }
    public void serveDinner(Car c) {
        log.log(LogLevel.INFO, "Serving dinner to robots in car " + c.id());
        stats.diningRobots++;
    }
}