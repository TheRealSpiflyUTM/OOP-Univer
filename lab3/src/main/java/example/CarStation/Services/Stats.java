package example.CarStation;

import example.Logger.LogLevel;
import example.Logger.Logger;

public class Stats {
    public int total, rejected, unserved, served, electric, gas, people, robots, dining, notDining;
    public int diningPeople, diningRobots, consElectric, consGas;

    public void print(Logger log) { log.log(LogLevel.INFO,toJson()); }
    public String toJson() {
        return """
        {"TOTAL": %d, "REJECTED": %d, "UNSERVED": %d, "SERVED": %d,
         "ELECTRIC": %d, "GAS": %d, "PEOPLE": %d, "ROBOTS": %d,
         "DINING": %d, "NOT_DINING": %d,
         "DINING_BY": {"PEOPLE": %d, "ROBOTS": %d},
         "CONSUMPTION": {"ELECTRIC": %d, "GAS": %d}}"""
        .formatted( total, rejected, unserved, served, electric, gas, people, robots,
                    dining, notDining, diningPeople, diningRobots, consElectric, consGas);
    }
}