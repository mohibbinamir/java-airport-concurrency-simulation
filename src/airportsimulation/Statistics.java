package airportsimulation;

import java.util.ArrayList;

public class Statistics {

    private final ArrayList<Long> waitingTimes;
    private int aircraftServed;
    private int passengersBoarded;

    public Statistics() {
        this.waitingTimes = new ArrayList<>();
        this.aircraftServed = 0;
        this.passengersBoarded = 0;
    }

    public synchronized void addWaitingTime(long waitingTime) {
        waitingTimes.add(waitingTime);
    }

    public synchronized void incrementAircraftServed() {
        aircraftServed++;
    }

    public synchronized void addPassengersBoarded(int passengers) {
        passengersBoarded += passengers;
    }

    public synchronized void printStatistics() {
        System.out.println("\n---------- Statistics Report ----------");

        if (waitingTimes.isEmpty()) {
            System.out.println("No waiting time data available.");
        } else {
            long min = waitingTimes.get(0);
            long max = waitingTimes.get(0);
            long total = 0;

            for (long time : waitingTimes) {
                if (time < min) {
                    min = time;
                }

                if (time > max) {
                    max = time;
                }

                total += time;
            }

            double average = (double) total / waitingTimes.size();

            System.out.println("Minimum waiting time: " + min + " ms");
            System.out.println("Average waiting time: " + String.format("%.2f", average) + " ms");
            System.out.println("Maximum waiting time: " + max + " ms");
        }

        System.out.println("Number of aircraft served: " + aircraftServed);
        System.out.println("Total passengers boarded: " + passengersBoarded);
        System.out.println("---------------------------------------");
    }
}