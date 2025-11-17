package esiee.lift.algorithm;

import esiee.lift.request.Call;
import esiee.lift.builder.LiftManager;

import java.util.List;
import java.util.Optional;

public interface Dispatcher {
	/**
	 * Select the best lift manager to handle the given call request.
	 * 
	 * @param call          the call request
	 * @param liftManagers  the list of available lift managers
	 * @return an Optional containing the selected LiftManager, or empty if no suitable lift is found
	 */
	Optional<LiftManager> selectLift(Call call, List<LiftManager> liftManagers);
}
