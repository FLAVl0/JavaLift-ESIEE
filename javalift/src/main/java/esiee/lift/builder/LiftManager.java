package esiee.lift.builder;

public class LiftManager {

	/* Definition of the LiftManager class */

	private final Lift lift;
	private int currentCapacity;
	private int currentFloor;

	/**
	 * Constructor for LiftManager
	 * 
	 * @param lift the lift to manage
	 */
	public LiftManager(Lift lift) {
		this.lift = lift;
		this.currentCapacity = 0;
	}

	/* Functions to manage lift state */

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

	public void updateCurrentFloor(int floor) {
		this.currentFloor = floor;
	}

	/* Functions to query about lift state */

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
	 * Get the lift being managed.
	 * 
	 * @return the lift instance
	 */
	public Lift getLift() {
		return lift;
	}
}
