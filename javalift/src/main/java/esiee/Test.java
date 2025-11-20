package esiee;

import esiee.lift.AGA;
import esiee.lift.global.request.Call;
import esiee.lift.local.Heuristics;
import esiee.lift.local.LiftEvolution;
import esiee.lift.local.builder.LiftManagerBuilder;
import esiee.lift.global.dispatcher.DispatcherStrategy;

public class Test {
	private static AGA aga;

	public static void main(String[] args) {

		aga = new AGA(DispatcherStrategy.CLOSEST_LIFT);
		
		aga.addLift(new LiftManagerBuilder()
				.setHeuristics(Heuristics.SHORTEST_PATH));
		aga.addLift(new LiftManagerBuilder()
				.setMaxFloor(5)
				.setMaxCapacity(3)
				.setHeuristics(Heuristics.SHORTEST_PATH));
				
		System.out.println("Responding to call from floor 2 to 7");
		aga.respondToCall(new Call(2, 0));
		
		System.out.println("Lift information after responding to call:");
		getLiftManagerInformation();
		
		System.out.println();
		System.out.println("Moving lifts...");
		aga.getLiftManagers().forEach(lm -> {
			LiftEvolution evolution = lm.move();
			System.out.println("Lift ID: " + lm.lift().id() + " moved with evolution: " + evolution);
		});
		
		System.out.println();
		System.out.println("Lift information after moving:");
		getLiftManagerInformation();

		System.out.println();
		System.out.println("Moving lifts...");
		aga.getLiftManagers().forEach(lm -> {
			LiftEvolution evolution = lm.move();
			System.out.println("Lift ID: " + lm.lift().id() + " moved with evolution: " + evolution);
		});

		System.out.println();
		System.out.println("Lift information after moving:");
		getLiftManagerInformation();

		System.out.println();
		System.out.println("Moving lifts...");
		aga.getLiftManagers().forEach(lm -> {
			LiftEvolution evolution = lm.move();
			System.out.println("Lift ID: " + lm.lift().id() + " moved with evolution: " + evolution);
		});

		System.out.println();
		System.out.println("Lift information after moving:");
		getLiftManagerInformation();
	}

	private static void getLiftManagerInformation() {
		for (esiee.lift.local.builder.LiftManager lm : aga.getLiftManagers()) {
			System.out.println();
			System.out.println("Lift: " + lm.lift().id());
			System.out.println("  Direction: " + lm.direction());
			System.out.println("  Current Floor: " + lm.currentFloor());
			System.out.println("  Target Floor: " + lm.targetFloor());
			System.out.println("  Requested Floors: " + lm.requestedFloors());
		}
	}
}
