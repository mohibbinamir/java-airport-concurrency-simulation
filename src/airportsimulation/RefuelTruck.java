package airportsimulation;

public class RefuelTruck {

    public synchronized void refuel(String aircraftName) throws InterruptedException {
        System.out.println(aircraftName + ": Waiting for refuel truck.");
        System.out.println("Refuel Truck: Refueling " + aircraftName + ".");
        Thread.sleep(1500);
        System.out.println("Refuel Truck: Refueling completed for " + aircraftName + ".");
    }
}