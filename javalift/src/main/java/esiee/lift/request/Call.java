package esiee.lift.request;

/**
 * Represents a call request in the lift system.
 */
public record Call(int fromFloor, int toFloor, boolean specificLift, String liftId) {

	/**
	 * General call request from a floor to another floor.
	 *
	 * @param fromFloor the floor where the call is made
	 * @param toFloor   the destination floor
	 */
	public Call(int fromFloor, int toFloor) {
		this(fromFloor, toFloor, false, null);
	}

	/**
	 * Specific call request for a particular lift from a floor to another floor.
	 *
	 * @param fromFloor the floor where the call is made
	 * @param toFloor   the destination floor
	 * @param liftId    the identifier of the specific lift to call
	 */
	public Call(int fromFloor, int toFloor, String liftId) {
		this(fromFloor, toFloor, true, liftId);
	}
}
