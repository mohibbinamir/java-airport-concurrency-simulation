# Java Airport Concurrency Simulation

This project is a multithreaded airport simulation developed in **Java** for the **Concurrent Programming (CT074-3-2-CCP)** module.

The system simulates airport operations where multiple aircraft compete for limited shared resources such as the runway, gates, and refueling truck. The program demonstrates the use of concurrency concepts including threads, synchronization, mutual exclusion, and inter-thread communication.

## Project Overview

The simulation models the safe landing, docking, servicing, and take-off of multiple aircraft in a small airport environment. Each aircraft runs as an independent thread and interacts with shared airport resources under controlled synchronization rules.

The project includes both the basic assignment requirements and additional concurrency features such as emergency landing priority, random aircraft arrival, and concurrent ground servicing operations.

## Features

- One shared runway for landing and take-off
- Maximum aircraft ground capacity control
- Gate assignment and docking process
- Complete aircraft lifecycle from landing to take-off
- No waiting area on the ground
- One shared refueling truck
- Emergency landing priority
- Six aircraft with random arrival time
- Concurrent passenger, cleaning, and supply servicing
- Final sanity check and airport statistics report

## Concurrency Concepts Demonstrated

- Java threads
- `synchronized` methods
- Mutual exclusion
- `wait()` and `notifyAll()`
- Shared resource protection
- Thread coordination
- Controlled access to runway, gates, and refueling truck

## Technologies Used

- Java
- Multithreading
- Object-Oriented Programming
- Concurrency control

## Project Structure

```text
src/airportsimulation/
├── Aircraft.java
├── Airport.java
├── AirportSimulation.java
├── AirTrafficCoordinator.java
├── Gate.java
├── RefuelTruck.java
└── Statistics.java
