package example.CarStation.Services;

import example.CarStation.Car;
import example.CarStation.Car.CarType;
import example.Logger.LogLevel;
import example.Logger.Logger;

public class GasStation implements Refuelable {
    private final Stats stats; private final Logger log;
    public GasStation(Stats s, Logger l) { 
        stats = s; 
        log = l; 
    }
    public boolean accepts(CarType t) { return t == CarType.GAS; }
    public void refuel(Car c) {
        log.log(LogLevel.INFO, "Refueling gas car " + c.id());
        stats.gas++; stats.consGas += c.consumption();
    }
}
// GasStation: same with GAS, stats.gas++, stats.consGas