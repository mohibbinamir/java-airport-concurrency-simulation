package airportsimulation;

public class Aircraft extends Thread {

    private final String aircraftName;
    private final boolean emergency;
    private final int passengersToDisembark;
    private final int passengersToBoard;

    private final AirTrafficCoordinator coordinator;
    private final RefuelTruck refuelTruck;
    private final Statistics statistics;

    public Aircraft(String aircraftName,
                    boolean emergency,
                    int passengersToDisembark,
                    int passengersToBoard,
                    AirTrafficCoordinator coordinator,
                    RefuelTruck refuelTruck,
                    Statistics statistics) {

        super(aircraftName);
        this.aircraftName = aircraftName;
        this.emergency = emergency;
        this.passengersToDisembark = passengersToDisembark;
        this.passengersToBoard = passengersToBoard;
        this.coordinator = coordinator;
        this.refuelTruck = refuelTruck;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        try {
            if (emergency) {
                System.out.println(aircraftName + ": Fuel shortage! Emergency landing required.");
            }

            Gate assignedGate = coordinator.requestLanding(aircraftName, emergency);

            land();
            coordinator.releaseRunwayAfterLanding(aircraftName);

            coastToGate(assignedGate);
            dockAtGate(assignedGate);

            performGroundServices();

            undockFromGate(assignedGate);
            coastToRunway();

            coordinator.requestTakeOff(aircraftName, assignedGate);
            takeOff();
            coordinator.completeTakeOff(aircraftName, assignedGate);

            statistics.incrementAircraftServed();
            statistics.addPassengersBoarded(passengersToBoard);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void land() throws InterruptedException {
        System.out.println(aircraftName + ": Landing.");
        Thread.sleep(1000);
        System.out.println(aircraftName + ": Landed.");
    }

    private void coastToGate(Gate gate) throws InterruptedException {
        System.out.println(aircraftName + ": Coasting to Gate-" + gate.getGateNumber() + ".");
        Thread.sleep(700);
    }

    private void dockAtGate(Gate gate) throws InterruptedException {
        System.out.println(aircraftName + ": Docked at Gate-" + gate.getGateNumber() + ".");
        Thread.sleep(500);
    }

    private void performGroundServices() throws InterruptedException {
        Thread passengerThread = new Thread(() -> {
            try {
                System.out.println(aircraftName + " Passengers: "
                        + passengersToDisembark + " passengers disembarking.");
                Thread.sleep(1000);

                System.out.println(aircraftName + " Passengers: "
                        + passengersToBoard + " passengers embarking.");
                Thread.sleep(1000);

                System.out.println(aircraftName + " Passengers: Passenger service completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, aircraftName + "-Passenger-Service");

        Thread cleaningThread = new Thread(() -> {
            try {
                System.out.println(aircraftName + " Cleaning Crew: Cleaning aircraft.");
                Thread.sleep(1200);

                System.out.println(aircraftName + " Supply Crew: Refilling food and supplies.");
                Thread.sleep(1000);

                System.out.println(aircraftName + " Cleaning/Supply Crew: Service completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, aircraftName + "-Cleaning-Supply-Service");

        Thread refuelThread = new Thread(() -> {
            try {
                refuelTruck.refuel(aircraftName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, aircraftName + "-Refuel-Service");

        passengerThread.start();
        cleaningThread.start();
        refuelThread.start();

        passengerThread.join();
        cleaningThread.join();
        refuelThread.join();

        System.out.println(aircraftName + ": All ground services completed.");
    }

    private void undockFromGate(Gate gate) throws InterruptedException {
        System.out.println(aircraftName + ": Undocking from Gate-" + gate.getGateNumber() + ".");
        Thread.sleep(500);
    }

    private void coastToRunway() throws InterruptedException {
        System.out.println(aircraftName + ": Coasting to runway.");
        Thread.sleep(700);
    }

    private void takeOff() throws InterruptedException {
        System.out.println(aircraftName + ": Taking off.");
        Thread.sleep(1000);
        System.out.println(aircraftName + ": Take-off completed.");
    }
}