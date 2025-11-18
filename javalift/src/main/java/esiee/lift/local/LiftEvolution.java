package esiee.lift.local;

public record LiftEvolution(int direction, int floorsTraveled, int timeTaken) {
	/**
	 * Constructor for LiftEvolution record. Performs validation on parameters.
	 * 
	 * @param direction      The direction of the lift movement (-1 for down, 0 for
	 *                       stationary, 1 for up)
	 * @param floorsTraveled The number of floors traveled during the movement
	 * @param timeTaken      The time taken for the movement
	 */
	public LiftEvolution {
		if (direction < -1 || direction > 1) {
			throw new IllegalArgumentException("Direction must be -1, 0, or 1");
		}
		if (floorsTraveled < 0) {
			throw new IllegalArgumentException("Floors traveled cannot be negative");
		}
		if (timeTaken < 0) {
			throw new IllegalArgumentException("Time taken cannot be negative");
		}
	}
}
