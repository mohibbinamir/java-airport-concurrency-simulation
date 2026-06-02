package airportsimulation;

public class AirTrafficCoordinator {

    private final Airport airport;

    public AirTrafficCoordinator(Airport airport) {
        this.airport = airport;
    }

    public Gate requestLanding(String aircraftName, boolean emergency) {
        return airport.requestLanding(aircraftName, emergency);
    }

    public void releaseRunwayAfterLanding(String aircraftName) {
        airport.releaseRunwayAfterLanding(aircraftName);
    }

    public void requestTakeOff(String aircraftName, Gate gate) {
        airport.requestTakeOff(aircraftName, gate);
    }

    public void completeTakeOff(String aircraftName, Gate gate) {
        airport.completeTakeOff(aircraftName, gate);
    }
}