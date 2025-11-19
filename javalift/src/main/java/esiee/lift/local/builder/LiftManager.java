package esiee.lift.local.builder;

import esiee.lift.local.Heuristics;
import esiee.lift.local.LiftEvolution;
import esiee.lift.local.LiftPhysics;

import java.util.Set;
import java.util.HashSet;

public class LiftManager {

	/* Definition of the LiftManager class */

	private final Lift lift;
	private final LiftPhysics physics;

	private boolean doorOpen;

	private int currentCapacity;
	private int currentFloor;
	private final Set<Integer> requestedFloors;
	private int targetFloor;

	private Heuristics heuristics;

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
	 * @param doorOpen        the initial door state of the lift
	 * @param heuristics      the heuristic to use for target floor selection
	 * 
	 * @see esiee.lift.local.Heuristics
	 * @see esiee.lift.AGA
	 * @see esiee.lift.local.builder.Lift
	 */
	public LiftManager(Lift lift, int initialCapacity, int initialFloor, boolean doorOpen, Heuristics heuristics) {
		this.lift = lift;
		this.physics = new LiftPhysics(lift);

		this.currentCapacity = initialCapacity;
		this.currentFloor = initialFloor;
		this.doorOpen = doorOpen;
		this.requestedFloors = new HashSet<>(lift.numberOfFloors());

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
		this(lift, 0, lift.minFloor(), false, Heuristics.RANDOM);
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
	public LiftEvolution move() {
		int direction = 0, floorsTraveled = 0, timeTaken = 0;

		if (currentFloor != targetFloor && !isEmpty()) {
			direction = (targetFloor == currentFloor) ? 0 : (targetFloor > currentFloor) ? 1 : -1; // Direction
			floorsTraveled = Math.abs(currentFloor - targetFloor); // Floors traveled
			timeTaken = physics.movementTime(floorsTraveled); // Time taken
		}

		currentFloor = targetFloor;
		this.requestedFloors.remove(targetFloor);

		if (!requestedFloors.isEmpty()) {
			targetFloor = heuristics.chooseTargetFloor(this);
		}
		
		return new LiftEvolution(direction, floorsTraveled, timeTaken);
	}

	/**
	 * Update the current capacity of the lift based on passengers entering and
	 * exiting.
	 * 
	 * @param inPassengers  Passengers that would like to enter the lift
	 * @param outPassengers Passengers that would like to exit the lift
	 * 
	 * @return The number of passengers left outside the lift (not able to enter
	 *         because of max capacity), 0 if all passengers were able to enter.
	 */
	public int updateCapacity(int inPassengers, int outPassengers) {
		currentCapacity -= (outPassengers <= currentCapacity) ? outPassengers : currentCapacity;

		int canEnter = lift.maxCapacity() - currentCapacity;

		if (inPassengers > canEnter) {
			currentCapacity += canEnter;
			return inPassengers - canEnter;
		}

		currentCapacity += inPassengers;

		return 0;
	}

	/**
	 * Update the current floor of the lift.
	 * 
	 * @param floor the new current floor
	 */
	public void updateCurrentFloor(int floor) {
		this.currentFloor = floor;
	}

	/**
	 * Set the heuristic for the lift manager to use.
	 * 
	 * @param heuristics the heuristic to set
	 */
	public void setHeuristics(Heuristics heuristics) {
		this.heuristics = heuristics;
	}

	/**
	 * Set the next target floor for the lift.
	 * 
	 * @param floor the target floor to set
	 */
	public void setTargetFloor(int floor, boolean external) {
		this.requestedFloors.add(floor);
	}

	/* Execution */

	/**
	 * Execute the lift movement based on the current heuristic.
	 * 
	 * @see Heuristics
	 */
	public int decideNextTargetFloor() {
		int nextFloor = heuristics.chooseTargetFloor(this);
		setTargetFloor(nextFloor, false);
		return nextFloor;
	}

	/**
	 * Register a call request for the lift.
	 * 
	 * @param call the call request to register
	 */
	public void registerCall(esiee.lift.global.request.Call call) {
		this.targetFloor = call.fromFloor();
		this.requestedFloors.add(call.toFloor());
	}

	/* Functions to query about lift state */

	/**
	 * Check if the lift door is open.
	 */
	public boolean isDoorOpen() {
		return doorOpen;
	}

	/**
	 * Check if the lift is full.
	 */
	public boolean isFull() {
		return currentCapacity >= lift.maxCapacity();
	}

	/**
	 * Check if the lift is empty.
	 * 
	 * @return true if the lift is empty, false otherwise
	 */
	private boolean isEmpty() {
		return currentCapacity == 0;
	}

	/**
	 * Get the current capacity of the lift.
	 * 
	 * @return current capacity
	 */
	public int getCurrentCapacity() {
		return currentCapacity;
	}

	/**
	 * Get the current floor of the lift.
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * Get the set of requested floors for the lift.
	 */
	public Set<Integer> getRequestedFloors() {
		return requestedFloors;
	}

	/**
	 * Get the target floor of the lift.
	 */
	public int getTargetFloor() {
		return targetFloor;
	}

	/**
	 * Get the current heuristic used by the lift manager.
	 */
	public Heuristics getHeuristics() {
		return heuristics;
	}

	/**
	 * Get the lift being managed.
	 */
	public Lift getLift() {
		return lift;
	}
}
