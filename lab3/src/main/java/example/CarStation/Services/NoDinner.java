package example.CarStation.Services;

import example.CarStation.Car;
import example.CarStation.Car.Passengers;

public class NoDinner implements Dineable {
    public boolean accepts(Passengers p) { return false; }
    public void serveDinner(Car c) { }
}