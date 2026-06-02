package airportsimulation;

public class Airport {

    private final Gate[] gates;
    private boolean runwayAvailable;
    private int aircraftOnGround;
    private final int maxAircraftOnGround;
    private boolean emergencyWaiting;

    private final Statistics statistics;

    public Airport(Statistics statistics) {
        this.gates = new Gate[3];

        for (int i = 0; i < gates.length; i++) {
            gates[i] = new Gate(i + 1);
        }

        this.runwayAvailable = true;
        this.aircraftOnGround = 0;
        this.maxAircraftOnGround = 3;
        this.emergencyWaiting = false;
        this.statistics = statistics;
    }

    public synchronized Gate requestLanding(String aircraftName, boolean emergency) {
        long requestTime = System.currentTimeMillis();

        System.out.println(aircraftName + ": Requesting landing.");

        if (emergency) {
            emergencyWaiting = true;
            System.out.println("ATC: Emergency detected for " + aircraftName + " due to fuel shortage.");
        }

        while (!canLand(emergency)) {
            try {
                if (aircraftOnGround >= maxAircraftOnGround) {
                    System.out.println("ATC: Landing permission denied for "
                            + aircraftName + ", airport ground is full.");
                } else if (!runwayAvailable) {
                    System.out.println("ATC: " + aircraftName
                            + " waiting because runway is occupied.");
                } else if (getFreeGate() == null) {
                    System.out.println("ATC: " + aircraftName
                            + " waiting because no gate is available.");
                } else if (!emergency && emergencyWaiting) {
                    System.out.println("ATC: " + aircraftName
                            + " waiting because emergency aircraft has priority.");
                }

                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Gate assignedGate = getFreeGate();
        assignedGate.occupy();

        runwayAvailable = false;
        aircraftOnGround++;

        if (emergency) {
            emergencyWaiting = false;
        }
        

        long permissionTime = System.currentTimeMillis();
        long waitingTime = permissionTime - requestTime;
        statistics.addWaitingTime(waitingTime);

        System.out.println("ATC: Landing permission granted for " + aircraftName + ".");
        System.out.println("ATC: Gate-" + assignedGate.getGateNumber()
                + " assigned for " + aircraftName + ".");

        return assignedGate;
    }

    private boolean canLand(boolean emergency) {
        if (!runwayAvailable) {
            return false;
        }

        if (aircraftOnGround >= maxAircraftOnGround) {
            return false;
        }

        if (getFreeGate() == null) {
            return false;
        }
        
        if (!emergency && emergencyWaiting) {
            return false;
        }

        return true;
    }

    private Gate getFreeGate() {
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                return gate;
            }
        }
        return null;
    }

    public synchronized void releaseRunwayAfterLanding(String aircraftName) {
        runwayAvailable = true;
        System.out.println("ATC: Runway is now free after " + aircraftName + " landed.");
        notifyAll();
    }

    public synchronized void requestTakeOff(String aircraftName, Gate gate) {
        System.out.println(aircraftName + ": Requesting take-off.");

        while (!runwayAvailable) {
            try {
                System.out.println("ATC: " + aircraftName
                        + " waiting for runway to become free for take-off.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        runwayAvailable = false;
        System.out.println("ATC: Take-off permission granted for " + aircraftName + ".");
    }

    public synchronized void completeTakeOff(String aircraftName, Gate gate) {
        runwayAvailable = true;
        aircraftOnGround--;

        gate.release();

        System.out.println("ATC: " + aircraftName + " has departed.");
        System.out.println("ATC: Gate-" + gate.getGateNumber() + " is now empty.");
        System.out.println("ATC: Runway is now free.");

        notifyAll();
    }

    public synchronized void performSanityCheck() {
        System.out.println("\n---------- Sanity Check ----------");

        boolean allGatesEmpty = true;

        for (Gate gate : gates) {
            if (gate.isOccupied()) {
                allGatesEmpty = false;
                System.out.println("Gate-" + gate.getGateNumber() + " is still occupied.");
            } else {
                System.out.println("Gate-" + gate.getGateNumber() + " is empty.");
            }
        }

        System.out.println("All gates empty: " + allGatesEmpty);
        System.out.println("----------------------------------");
    }
}