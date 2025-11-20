package esiee.lift.global.dispatcher;

import java.util.function.Supplier;

public enum DispatcherStrategy {
	CLOSEST_LIFT(ClosestLift::new),
	EMPTIEST_LIFT(EmptiestLift::new);

	private final Supplier<Dispatcher> factory;

	DispatcherStrategy(Supplier<Dispatcher> factory) {
		this.factory = factory;
	}

	public Dispatcher createDispatcher() {
		return factory.get();
	}
}