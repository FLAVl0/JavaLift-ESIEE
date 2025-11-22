package esiee.lift.global.dispatcher;

import java.util.ArrayList;

import esiee.lift.global.request.Call;
import esiee.lift.local.builder.LiftManager;

public class ClosestLift implements Dispatcher {

	@Override
	public LiftManager selectLift(Call call, ArrayList<LiftManager> liftManagers) {
		int fromFloor = call.fromFloor();
		System.out.println("// Inside ClosestLift // Selecting lift for call from floor " + fromFloor);
		System.out.println("// Inside ClosestLift // Available lifts: ");
		liftManagers.forEach(lm -> System.out.println(" - Lift " + lm.lift().id() + " at floor " + lm.currentFloor()));
		return liftManagers.stream().min((lm1, lm2) -> Integer.compare(
				Math.abs(lm1.currentFloor() - fromFloor),
				Math.abs(lm2.currentFloor() - fromFloor))).get();
	}
}
