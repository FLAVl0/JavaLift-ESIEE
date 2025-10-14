package esiee.lift;

import java.util.ArrayList;

import esiee.lift.builder.Lift;
import esiee.lift.builder.LiftBuilder;

public class AGA {
	private static ArrayList<Lift> lifts = new ArrayList<>();

	public void createLift() {
		LiftBuilder lb = new LiftBuilder();

		Lift lift = lb.createLift();
		lifts.add(lift);
	}

	public void addLift(Lift lift) {
		lifts.add(lift);
	}

	public ArrayList<Lift> getLifts() {
		return lifts;
	}
}
