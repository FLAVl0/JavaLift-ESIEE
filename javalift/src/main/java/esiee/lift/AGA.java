package esiee.lift;

import java.util.ArrayList;

import esiee.lift.builder.LiftManager;

public class AGA {

	/* Definition of the AGA class */

	private final ArrayList<LiftManager> liftManagers = new ArrayList<>(); // Starts empty (created via builder)

	/* Functions to manage global lift state */

	/**
	 * Create a new lift and add its manager to this AGA instance.
	 * @param lm the lift to create through its manager
	 */
	public void addLift(LiftManager lm) {
		liftManagers.add(lm);
	}

	/* Functions to query about global lift state */

	/**
	 * Get the list of lifts managed by this AGA.
	 */
	public ArrayList<LiftManager> getLiftManagers() {
		return liftManagers;
	}
}
