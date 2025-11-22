package esiee.lift;

import java.util.ArrayList;

import esiee.lift.global.dispatcher.DispatcherStrategy;
import esiee.lift.global.request.Call;
import esiee.lift.local.builder.LiftManager;
import esiee.lift.local.builder.LiftManagerBuilder;

public class AGA {

	/* Definition of the AGA class */

	private final ArrayList<LiftManager> liftManagers = new ArrayList<>(); // Starts empty (created via builder)
	private DispatcherStrategy dispatcherStrategy;

	/**
	 * Constructor of the AGA class.
	 * 
	 * @param strategy the dispatcher strategy to use
	 */
	public AGA(DispatcherStrategy strategy) {
		this.dispatcherStrategy = strategy;
	}

	/* Functions to manage global lift state */

	/**
	 * Create a new lift and add its manager to this AGA instance.
	 * 
	 * @param lm the lift to create through its manager
	 */
	public void addLift(LiftManagerBuilder lmb) {
		liftManagers.add(lmb.build());
	}

	/**
	 * Set the dispatcher used to select lifts on calls.
	 * 
	 * @param dispatcher the dispatcher to use
	 */
	public void setDispatcher(DispatcherStrategy strategy) {
		this.dispatcherStrategy = strategy;
	}

	public int respondToCall(Call call) {
		int fromFloor = call.fromFloor();
		int toFloor = call.toFloor();
		int id = call.liftId();
		if (call.specificLift()) {
			liftManagers.stream()
				.filter(lm -> lm.lift().id() == call.liftId())
				.findFirst()
				.ifPresent(lm -> lm.registerCall(fromFloor, toFloor));
		} else {

			LiftManager currentlm = dispatcherStrategy.createDispatcher().selectLift(call, liftManagers);
			currentlm.registerCall(fromFloor, toFloor);
			id = currentlm.lift().id();
			System.out.println("// Inside // Lift " + id + " selected for call from floor " + fromFloor + " to floor " + toFloor);
		}
		return id;
	}

	/* Getters */

	public ArrayList<LiftManager> getLiftManagers() {
		return liftManagers;
	}

	public DispatcherStrategy getDispatcherStrategy() {
		return dispatcherStrategy;
	}
}
