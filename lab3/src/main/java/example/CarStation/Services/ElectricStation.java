package example.CarStation;

import example.CarStation.Car.CarType;
import example.Logger.LogLevel;
import example.Logger.Logger;

public class ElectricStation implements Refuelable {
    private final Stats stats; private final Logger log;
    public ElectricStation(Stats s, Logger l) { 
        stats = s; 
        log = l; 
    }
    public boolean accepts(CarType t) { return t == CarType.ELECTRIC; }
    public void refuel(Car c) {
        log.log(LogLevel.INFO, "Refueling electric car " + c.id());
        stats.electric++; stats.consElectric += c.consumption();
    }
}
// GasStation: same with GAS, stats.gas++, stats.consGas