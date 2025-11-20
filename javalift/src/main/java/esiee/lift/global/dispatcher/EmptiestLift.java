package esiee.lift.global.dispatcher;

import esiee.lift.global.request.Call;
import esiee.lift.local.builder.LiftManager;

import java.util.ArrayList;

public class EmptiestLift implements Dispatcher {
	
	@Override
	public LiftManager selectLift(Call call, ArrayList<LiftManager> liftManagers) {
		return liftManagers.stream()
				.min((lm1, lm2) -> Integer
					.compare(lm1.currentCapacity(), lm2.currentCapacity()))
				.get();
	}
}
