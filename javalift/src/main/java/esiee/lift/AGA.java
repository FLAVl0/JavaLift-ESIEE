package esiee.lift;

import java.util.ArrayList;

import esiee.lift.global.dispatcher.DispatcherStrategy;
import esiee.lift.local.LiftResponse;
import esiee.lift.local.builder.LiftManager;
import esiee.lift.local.builder.LiftManagerBuilder;
import esiee.lift.global.request.Call;

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

	/**
	 * Function to handle calls accordingly with the chosen dispatcher strategy.
	 * Will return the ID of the selected lift for the UI to update.
	 * 
	 * @param call the call that was just triggered, containing from and to floors.
	 * 
	 * @return the LiftResponse (id + information about physical evolution)
	 */
	public LiftResponse respondToCall(Call call) {
		int fromFloor = call.fromFloor();
		int toFloor = call.toFloor();

		LiftManager selectedLiftManager = call.specificLift()
				? liftManagers.stream().filter(lm -> lm.lift().id() == call.liftId()).findFirst().get()
				: dispatcherStrategy.createDispatcher().selectLift(call, liftManagers);

		selectedLiftManager.registerCall(fromFloor, toFloor);
		
		return selectedLiftManager.move();
	}

	/* Getters */

	public ArrayList<LiftManager> getLiftManagers() {
		return liftManagers;
	}

	public DispatcherStrategy getDispatcherStrategy() {
		return dispatcherStrategy;
	}
}
