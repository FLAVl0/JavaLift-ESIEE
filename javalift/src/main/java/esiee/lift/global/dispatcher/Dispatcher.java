package esiee.lift.global.dispatcher;

import esiee.lift.global.request.Call;
import esiee.lift.local.builder.LiftManager;

import java.util.ArrayList;

public interface Dispatcher {
	/**
	 * Select the best lift manager to handle the given call request.
	 * 
	 * @param call         the call request
	 * @param liftManagers the list of available lift managers
	 * 
	 * @return an Optional containing the selected LiftManager, or empty if no
	 *         suitable lift is found
	 */
	LiftManager selectLift(Call call, ArrayList<LiftManager> liftManagers);
}
