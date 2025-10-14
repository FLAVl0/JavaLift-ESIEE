package esiee.lift.builder;

public class LiftBuilder {
	private static int liftCount = 0;

	public Lift createLift() {
		return new Lift(liftCount++);
	}
}
