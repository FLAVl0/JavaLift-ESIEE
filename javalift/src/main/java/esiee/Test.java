package esiee;

import esiee.lift.AGA;
import esiee.lift.global.request.Call;
import esiee.lift.local.Heuristics;
import esiee.lift.local.LiftResponse;
import esiee.lift.local.builder.LiftManagerBuilder;
import esiee.lift.global.dispatcher.DispatcherStrategy;

/**
 * This function is here to test the behaviour of Lift, LiftManager, and AGA
 * given a bunch of calls and ticks. It's to evaluate whether or not the
 * algorithms are responding properly.
 */
public class Test {
	private static AGA aga;
	private static LiftResponse evolution;

	public static void main(String[] args) {

		aga = new AGA(DispatcherStrategy.CLOSEST_LIFT);

		aga.addLift(new LiftManagerBuilder()
				.setHeuristics(Heuristics.SHORTEST_PATH));
		aga.addLift(new LiftManagerBuilder()
				.setMaxFloor(5)
				.setMaxCapacity(3)
				.setHeuristics(Heuristics.SHORTEST_PATH));

		System.out.println("Responding to call from floor 2 to 7");
		evolution = aga.respondToCall(new Call(2, 7));
		System.out.println(evolution);
		
		System.out.println("Lift information after responding to call:");
		getLiftManagerInformation();
		
		System.out.println("Responding to call from floor 2 to 7");
		evolution = aga.respondToCall(new Call(2, 7));
		System.out.println(evolution);

		aga.getLiftManagers().get(0).updateCapacity(10, 0);

		System.out.println("Lift information after responding to call:");
		getLiftManagerInformation();
		
		System.out.println("Responding to call from floor 2 to 7");
		evolution = aga.respondToCall(new Call(2, 7));
		System.out.println(evolution);

		System.out.println();
		System.out.println("Lift information after moving:");
		getLiftManagerInformation();

		System.out.println();
		System.out.println("Moving lifts...");
		aga.getLiftManagers().forEach(lm -> {
			System.out.println("Lift ID: " + lm.lift().id());
		});

		System.out.println();
		System.out.println("Lift information after moving:");
		getLiftManagerInformation();

		System.out.println();
		System.out.println("Moving lifts...");
		aga.getLiftManagers().forEach(lm -> {
			System.out.println("Lift ID: " + lm.lift().id());
		});

		System.out.println();
		System.out.println("Lift information after moving:");
		getLiftManagerInformation();
	}

	private static void getLiftManagerInformation() {
		for (esiee.lift.local.builder.LiftManager lm : aga.getLiftManagers()) {
			System.out.println();
			System.out.println("Lift: " + lm.lift().id());
			System.out.println("  Current capacity: " + lm.currentCapacity());
			System.out.println("  Direction: " + lm.direction());
			System.out.println("  Current Floor: " + lm.currentFloor());
			System.out.println("  Target Floor: " + lm.targetFloor());
			System.out.println("  Requested Floors: " + lm.requestedFloors());
		}
	}
}
