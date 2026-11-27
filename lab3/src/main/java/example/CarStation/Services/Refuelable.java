package example.CarStation;

import example.CarStation.Car.CarType;

public interface Refuelable { 
    boolean accepts(CarType t); 
    void refuel(Car c); 
}