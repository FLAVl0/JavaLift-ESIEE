package esiee;

import esiee.lift.AGA;
import esiee.lift.builder.*;

public class Test {
	public static void main(String[] args) {
		AGA aga = new AGA();

		Lift lift1 = new Lift(10, 5, 3, 2, 2, 0.5, 0, 20, "Lift1");
		LiftManager lm1 = new LiftManager(lift1);

		aga.addLift(lm1);

		System.out.println("Number of lifts managed by AGA: " + aga.getLiftManagers().size());
	}
}
