package example.CarStation;

public record Car(    
    int id,
    CarType type,
    Passengers passengers,
    boolean isDining,
    int consumption){
    public enum CarType { ELECTRIC, GAS }
    public enum Passengers { PEOPLE, ROBOTS }
}
