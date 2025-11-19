package esiee.lift.builder;

import java.util.HashSet;
import java.util.Set;

import esiee.lift.algorithm.Heuristics;

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
	 * <p>- initialCapacity is set to 0</p>
	 * <p>- initialFloor is set to lift.minFloor()</p>
	 * <p>- doorOpen is set to false</p>
	 * <p>- heuristics is set to Heuristics.RANDOM</p>
	 * 
	 * @param lift            the lift to manage
	 * @param initialCapacity the initial capacity of the lift
	 * @param initialFloor    the initial floor of the lift
	 * @param doorOpen        the initial door state of the lift
	 * @param heuristics      the heuristic to use for target floor selection
	 * 
	 * @see Heuristics
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
	 * <p>- initialCapacity is set to 0</p>
	 * <p>- initialFloor is set to lift.minFloor()</p>
	 * <p>- doorOpen is set to false</p>
	 * <p>- heuristics is set to Heuristics.RANDOM</p>
	 * 
	 * @param lift the lift to manage
	 */
	public LiftManager(Lift lift) {
		this(lift, 0, lift.minFloor(), false, Heuristics.RANDOM);
	}

	/* Functions to manage lift state */

	public int move() {
		if (currentFloor != targetFloor) {
			int distance = Math.abs(currentFloor - targetFloor);

			return physics.movementTime(distance);
		}

		return 0;
	}

	/**
	 * Update the current capacity of the lift based on passengers entering and
	 * exiting.
	 * 
	 * @param inPassengers  Passengers that would like to enter the lift
	 * @param outPassengers Passengers that would like to exit the lift
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
	 * 	Update the current floor of the lift.
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
	 * @return the next target floor
	 * @see Heuristics
	 */
	public int decideNextTargetFloor() {
		int nextFloor = heuristics.chooseTargetFloor(this);
		setTargetFloor(nextFloor, false);
		return nextFloor;
	}

	/* Functions to query about lift state */

	/**
	 * Check if the lift door is open.
	 * 
	 * @return true if the door is open, false otherwise
	 */
	public boolean isDoorOpen() {
		return doorOpen;
	}

	/**
	 * Check if the lift is full.
	 * 
	 * @return true if the lift is full, false otherwise
	 */
	public boolean isFull() {
		return currentCapacity >= lift.maxCapacity();
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
	 * 
	 * @return current floor
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * Get the set of requested floors for the lift.
	 * 
	 * @return the set of requested floors
	 */
	public Set<Integer> getRequestedFloors() {
		return requestedFloors;
	}
	
	/**
	 * Get the target floor of the lift.
	 * 
	 * @return target floor
	 */
	public int getTargetFloor() {
		return targetFloor;
	}

	/**
	 * Get the current heuristic used by the lift manager.
	 * 
	 * @return the heuristic
	 */
	public Heuristics getHeuristics() {
		return heuristics;
	}

	/**
	 * Get the lift being managed.
	 * 
	 * @return the lift instance
	 */
	public Lift getLift() {
		return lift;
	}
}
