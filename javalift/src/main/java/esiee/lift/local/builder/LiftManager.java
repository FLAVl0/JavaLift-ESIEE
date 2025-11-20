package esiee.lift.local.builder;

import esiee.lift.local.Heuristics;
import esiee.lift.local.LiftResponse;
import esiee.lift.local.LiftPhysics;

import java.util.LinkedHashSet;

public class LiftManager {

	/* Definition of the LiftManager class */

	private final Lift lift;
	private final LiftPhysics physics;

	private int currentCapacity; // The current number of people onboard
	private int currentFloor; // The current floor the lift is at
	private final LinkedHashSet<Integer> requestedFloors; // Contains all the floors that were requested (independent from )
	
	private int targetFloor; // Contains the next target for the move() method
	private int direction; // Will contain 0 (idle), 1 (up), or -1 (down)

	private Heuristics heuristics; // The heuristic the current Lift follows (responsible of determining next targetFloor)

	/**
	 * Constructor for LiftManager with specified initial capacity and floor.
	 * If not specified, default values are used:
	 * 
	 * <p>
	 * - initialCapacity is set to 0
	 * </p>
	 * <p>
	 * - initialFloor is set to lift.minFloor()
	 * </p>
	 * <p>
	 * - doorOpen is set to false
	 * </p>
	 * <p>
	 * - heuristics is set to Heuristics.RANDOM
	 * </p>
	 * 
	 * @param lift            the lift to manage
	 * @param initialCapacity the initial capacity of the lift
	 * @param initialFloor    the initial floor of the lift
	 * @param heuristics      the heuristic to use for target floor selection
	 * 
	 * @see esiee.lift.local.Heuristics
	 * @see esiee.lift.AGA
	 * @see esiee.lift.local.builder.Lift
	 */
	public LiftManager(Lift lift, int initialCapacity, int initialFloor, Heuristics heuristics) {
		this.lift = lift;
		this.physics = new LiftPhysics(lift);

		this.currentCapacity = initialCapacity;
		this.currentFloor = initialFloor;
		this.requestedFloors = new LinkedHashSet<>(lift.numberOfFloors());

		this.targetFloor = initialFloor;
		this.direction = 0;

		this.heuristics = heuristics;
	}

	/**
	 * Constructor for LiftManager with default values if not specified.
	 * 
	 * <p>
	 * - initialCapacity is set to 0
	 * </p>
	 * <p>
	 * - initialFloor is set to lift.minFloor()
	 * </p>
	 * <p>
	 * - doorOpen is set to false
	 * </p>
	 * <p>
	 * - heuristics is set to Heuristics.RANDOM
	 * </p>
	 * 
	 * @param lift the lift to manage
	 */
	public LiftManager(Lift lift) {
		this(lift, 0, lift.minFloor(), Heuristics.RANDOM);
	}

	/* Functions to manage lift state */

	/**
	 * Move the lift to the target floor and calculate the time taken for the
	 * movement.
	 * Updates the current floor to the target floor after movement.
	 * Updates the target floor to the next requested floor after movement.
	 * 
	 * @return the time taken for the movement
	 */
	public LiftResponse move() {
		
		this.direction = (byte) Integer.compare(targetFloor, currentFloor);

		if (requestedFloors.isEmpty()) {
			return new LiftResponse(lift.id(), 0, 0, 0); // No movement needed
		}
		
		// Calculate movement
		int floorsTraveled = Math.abs(targetFloor - currentFloor);
		int timeTaken = physics.movementTime(floorsTraveled);

		// Move to target floor and update state
		currentFloor = targetFloor;
		requestedFloors.remove(currentFloor);
		targetFloor = heuristics.chooseTargetFloor(this);

		return new LiftResponse(lift.id(), direction, floorsTraveled, timeTaken);
	}

	/**
	 * Update capacity based on passengers boarding/exiting.
	 * 
	 * @param inPassengers  passengers attempting to board
	 * @param outPassengers passengers exiting
	 * @return number of passengers unable to board
	 */
	public int updateCapacity(int inPassengers, int outPassengers) {
		// Remove exiting passengers
		currentCapacity = Math.max(0, currentCapacity - outPassengers);

		// Add boarding passengers up to capacity
		int availableSpace = lift.maxCapacity() - currentCapacity;
		int boardingPassengers = Math.min(inPassengers, availableSpace);
		currentCapacity += boardingPassengers;

		// Return number who couldn't board
		return inPassengers - boardingPassengers;
	}

	/**
	 * Set the heuristic strategy for this lift.
	 */
	public void setHeuristics(Heuristics heuristics) {
		this.heuristics = heuristics;
	}

	/**
	 * Register a passenger call.
	 * Adds both boarding and destination floors to the queue.
	 * 
	 * @param call the passenger's call request
	 */
	public void registerCall(int fromFloor, int toFloor) {
		// Add destination floor
		if (!requestedFloors.contains(toFloor)) {
			requestedFloors.add(toFloor);
		} else {
			requestedFloors.remove(toFloor);
			requestedFloors.add(toFloor); // Re-add to update order
		}

		// Set target floor to boarding floor
		targetFloor = fromFloor;
	}
	
	public boolean isFull() {
		return currentCapacity >= lift.maxCapacity();
	}

	public boolean isEmpty() {
		return currentCapacity == 0;
	}

	/* Getters */

	public int currentCapacity() {
		return this.currentCapacity;
	}

	public int currentFloor() {
		return this.currentFloor;
	}

	public LinkedHashSet<Integer> requestedFloors() {
		return new LinkedHashSet<>(this.requestedFloors); // Defensive copy
	}

	public int targetFloor() {
		return this.targetFloor;
	}

	public int direction() {
		return this.direction;
	}

	public Heuristics heuristics() {
		return this.heuristics;
	}

	public Lift lift() {
		return this.lift;
	}
}
