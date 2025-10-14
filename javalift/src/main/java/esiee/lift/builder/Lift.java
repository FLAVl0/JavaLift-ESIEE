package esiee.lift.builder;

/**
 * Class (record) representing a lift with its attibutes and methods.
 * 
 * @param id     Unique identifier of the lift
 */
public record Lift(int id) {

	/* Create the attributes of a lift */

	private static int currentCapacity = 0; // Number of passengers currently in the elevator
	private static final int maxCapacity = 20; // Max capacity, considering an average weight

	private static int currentFloor = 0; // Current floor at which the elevator is
	
	private static final int speed = 2; // Time in seconds to move one floor

	/* Methods definition */

	/**
	 * Method to change the number of passengers in the lift.
	 * 
	 * @param inPassengers  Passengers that would like to enter the lift
	 * @param outPassengers Passengers that would like to exit the lift
	 * @return The number of passengers left outside the lift (not able to enter because of max capacity), 0 if all passengers were able to enter.
	 */
	public int changeCurrentCapacity(int inPassengers, int outPassengers) {
		currentCapacity -= outPassengers;

		if (currentCapacity < 0)
			currentCapacity = 0; // Prevent negative capacity

		currentCapacity += inPassengers;

		if (!isFull())
			return 0;

		int leftOutside = currentCapacity - maxCapacity;
		currentCapacity = maxCapacity; // Set to max capacity
		return leftOutside; // Return number of passengers left outside
	}

	/**
	 * Method to get the current floor of the lift.
	 * @return Current floor of the lift
	 */
	public int getFloor() {
		return currentFloor;
	}

	public int getSpeed() {
		return speed;
	}

	public boolean isFull() {
		return currentCapacity >= maxCapacity;
	}
}
