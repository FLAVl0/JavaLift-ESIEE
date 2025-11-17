package esiee.lift.algorithm;

import esiee.lift.builder.LiftManager;
import esiee.lift.request.Call;

import java.util.List;
import java.util.Optional;

public class DispatcherStrategy implements Dispatcher {

	@Override
	public Optional<LiftManager> selectLift(Call call, List<LiftManager> liftManagers) {
		if (liftManagers.isEmpty()) {
			return Optional.empty();
		} else {
			// Select the closest lift to the call's fromFloor
			int fromFloor = call.fromFloor();

			return liftManagers.stream().min((a, b) -> Integer.compare(
				Math.abs(a.getCurrentFloor() - fromFloor), 
				Math.abs(b.getCurrentFloor() - fromFloor)
			));
		}
	}
}
