package esiee.lift.local.builder;

/**
 * Represents a Lift with its properties.
 *
 * @param maxCapacity  	the maximum capacity of the lift
 * @param maxSpeed    	the maximum speed of the lift
 * @param doorTime     	the time taken for the doors to open/close
 * @param acceleration 	the acceleration of the lift
 * @param deceleration 	the deceleration of the lift
 * @param energyCost   	the energy cost per operation of the lift
 * @param minFloor     	the minimum floor the lift can reach
 * @param maxFloor     	the maximum floor the lift can reach
 * @param id           	the unique identifier of the lift
 * 
 * @see LiftManager
 */
public record Lift(int maxCapacity, int maxSpeed, int doorTime, int acceleration, int deceleration, double energyCost, int minFloor, int maxFloor, int id) {
	/**
	 * Method to get the number of floors the lift can serve.
	 * 
	 * @return number of floors
	 */
	public int numberOfFloors() {
		return maxFloor - minFloor > 0 ? maxFloor - minFloor + 1 : 0;
	}
}
