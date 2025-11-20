package esiee.lift.global.dispatcher;

import esiee.lift.global.request.Call;
import esiee.lift.local.builder.LiftManager;

import java.util.ArrayList;
import java.util.Optional;

public class ClosestLift implements Dispatcher {

	@Override
	public Optional<LiftManager> selectLift(Call call, ArrayList<LiftManager> liftManagers) {
		int fromFloor = call.fromFloor();

		return liftManagers.stream().min((lm1, lm2) -> Integer.compare(
				Math.abs(lm1.currentFloor() - fromFloor),
				Math.abs(lm2.currentFloor() - fromFloor)));
	}
}
