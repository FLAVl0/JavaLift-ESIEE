package esiee.lift;

import java.util.ArrayList;

import esiee.lift.global.dispatcher.*;
import esiee.lift.local.builder.LiftManager;
import esiee.lift.global.request.Call;

public class AGA {

	/* Definition of the AGA class */

	private final ArrayList<LiftManager> liftManagers = new ArrayList<>(); // Starts empty (created via builder)
	private Dispatcher dispatcher;

	/**
	 * Constructor of the AGA class.
	 * 
	 * @param dispatcher the dispatcher to use
	 */
	public AGA(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/* Functions to manage global lift state */

	/**
	 * Create a new lift and add its manager to this AGA instance.
	 * 
	 * @param lm the lift to create through its manager
	 */
	public void addLift(LiftManager lm) {
		liftManagers.add(lm);
	}

	/**
	 * Set the dispatcher used to select lifts on calls.
	 * 
	 * @param dispatcher the dispatcher to use
	 */
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void respondToCall(Call call) {
		if (call.specificLift()) {
			liftManagers.stream()
				.filter(lm -> lm.getLift().id() == call.liftId())
				.findFirst()
				.ifPresent(lm -> lm.registerCall(call));
		} else {
			dispatcher.selectLift(call, liftManagers).ifPresent(lm -> lm.registerCall(call));
		}
	}

	/* Functions to query about global lift state */

	/**
	 * Get the list of lifts managed by this AGA.
	 */
	public ArrayList<LiftManager> getLiftManagers() {
		return liftManagers;
	}

	/**
	 * Get the dispatcher used by this AGA.
	 */
	public Dispatcher getDispatcher() {
		return dispatcher;
	}
}
