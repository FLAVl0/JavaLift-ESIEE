package esiee.lift.builder;

public class LiftPhysics {

	/* Definition of the LiftPhysics class */

	private final Lift lift;
	private int distanceTraveled;

	/**
	 * Constructor for LiftPhysics.
	 * 
	 * @param lift the lift for which to manage physics
	 */
	public LiftPhysics(Lift lift) {
		this.lift = lift;
		this.distanceTraveled = 0;
	}

	/**
	 * Calculate the distance required to accelerate from rest to the lift's speed.
	 * 
	 * @return the acceleration distance
	 */
	public int getAccelerationDistance() {
		return (lift.maxSpeed() * lift.maxSpeed()) / (2 * lift.acceleration());
	}

	/**
	 * Calculate the distance required to decelerate from current speed to zero.
	 * 
	 * @return the deceleration distance
	 */
	public int getDecelerationDistance() {
		return (lift.maxSpeed() * lift.maxSpeed()) / (2 * lift.deceleration());
	}

	public int movementTime(int distance) {
		int acceleration = lift.acceleration();
		int deceleration = lift.deceleration();
		int maxSpeed = lift.maxSpeed();

		int accelDistance = getAccelerationDistance();
		int decelDistance = getDecelerationDistance();

		if (distance <= accelDistance + decelDistance) {
			// Triangular profile
			double peakSpeed = Math.sqrt((2 * acceleration * deceleration * distance) / (acceleration + deceleration));
			int timeToPeak = (int) Math.ceil(peakSpeed / acceleration);
			int timeToStop = (int) Math.ceil(peakSpeed / deceleration);

			return timeToPeak + timeToStop;
		}

		// Trapezoidal profile
		int cruiseDistance = distance - accelDistance - decelDistance;
		int cruiseTime = cruiseDistance / maxSpeed;
		int timeToAccel = (int) Math.ceil((double) maxSpeed / acceleration);
		int timeToDecel = (int) Math.ceil((double) maxSpeed / deceleration);

		return timeToAccel + cruiseTime + timeToDecel;
	}

	/* Information about the lift's physics */

	/**
	 * Get the total distance traveled by the lift.
	 * 
	 * @return the distance traveled in metres
	 */
	public int getDistanceTraveled() {
		return distanceTraveled;
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
