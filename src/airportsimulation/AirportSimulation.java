package airportsimulation;

import java.util.Random;

public class AirportSimulation {

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("   Asia Pacific Airport Simulation");
        System.out.println("=======================================\n");

        Statistics statistics = new Statistics();
        RefuelTruck refuelTruck = new RefuelTruck();
        Airport airport = new Airport(statistics);
        AirTrafficCoordinator coordinator = new AirTrafficCoordinator(airport);

        Aircraft[] aircraftList = new Aircraft[6];
        Random random = new Random();

        for (int i = 0; i < aircraftList.length; i++) {
            String aircraftName = "Aircraft-" + (i + 1);

            // Aircraft-5 is used as the emergency aircraft.
            boolean emergency = (i == 4);

            int passengersToDisembark = random.nextInt(50) + 1;
            int passengersToBoard = random.nextInt(50) + 1;

            aircraftList[i] = new Aircraft(
                    aircraftName,
                    emergency,
                    passengersToDisembark,
                    passengersToBoard,
                    coordinator,
                    refuelTruck,
                    statistics
            );
        }

        for (Aircraft aircraft : aircraftList) {
            aircraft.start();

            try {
                // New aircraft arrives every 0, 1, or 2 seconds.
                Thread.sleep(random.nextInt(3) * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (Aircraft aircraft : aircraftList) {
            try {
                aircraft.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n=======================================");
        System.out.println("       Simulation Finished");
        System.out.println("=======================================");

        airport.performSanityCheck();
        statistics.printStatistics();
    }
}