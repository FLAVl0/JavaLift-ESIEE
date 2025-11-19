package esiee.lift.global.request;

/**
 * Represents a call request in the lift system.
 */
public record Call(int fromFloor, int toFloor, boolean specificLift, int liftId) {

	/**
	 * General call request from a floor to another floor.
	 *
	 * @param fromFloor the floor where the call is made
	 * @param toFloor   the destination floor
	 */
	public Call(int fromFloor, int toFloor) {
		this(fromFloor, toFloor, false, -1);
	}

	/**
	 * Specific call request for a particular lift from a floor to another floor.
	 *
	 * @param fromFloor the floor where the call is made
	 * @param toFloor   the destination floor
	 * @param liftId    the identifier of the specific lift to call
	 */
	public Call(int fromFloor, int toFloor, int liftId) {
		this(fromFloor, toFloor, true, liftId);
	}
}
