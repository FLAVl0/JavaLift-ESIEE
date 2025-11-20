package esiee.lift.local;

import java.util.Comparator;
import java.util.Optional;

import esiee.lift.local.builder.*;

/**
 * Enumeration of different heuristics for lift management.
 * Each heuristic defines a strategy for choosing the next target floor.
 * 
 * @see LiftManager
 */
public enum Heuristics {

	/* Minimize the total distance traveled */

	SHORTEST_PATH {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int currentFloor = lm.currentFloor();

			// Find the closest requested floor
			return lm.requestedFloors().stream()
				.min((f1, f2) -> Integer.compare(
					Math.abs(f1 - currentFloor),
					Math.abs(f2 - currentFloor)))
				.orElse(currentFloor);
		}
	},

	SHORTEST_ON_WAY {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int currentFloor = lm.currentFloor();
			int direction = lm.direction();

			Optional<Integer> inDirection = lm.requestedFloors().stream()
					.filter(f -> direction != 0 && Integer.signum(f - currentFloor) == direction)
					.min(Comparator.comparingInt(f -> Math.abs(f - currentFloor)));

			return inDirection.orElseGet(() -> 
				lm.requestedFloors().stream()
						.min(Comparator.comparingInt(f -> Math.abs(f - currentFloor)))
						.orElse(currentFloor)
			);
		}
	},

	/* Choose to only board going up */

	ALWAYS_UP {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int currentFloor = lm.currentFloor();
			Lift lift = lm.lift();

			return (currentFloor < lift.maxFloor()) ? currentFloor + 1 : lift.minFloor();
		}
	},

	/* Choose to only board going down */

	ALWAYS_DOWN {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int currentFloor = lm.currentFloor();
			Lift lift = lm.lift();

			return (currentFloor > lift.minFloor()) ? currentFloor - 1 : lift.maxFloor();
		}
	},
	
	/* Random choice */
	
	RANDOM {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			// Placeholder logic for random choice heuristic
			return (int) (Math.random() * lm.lift().numberOfFloors());
		}
	},

	/* FIFO */

	FIFO {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			return lm.requestedFloors().getLast();
		}
	},

	/* LIFO */

	LIFO {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			return lm.requestedFloors().getFirst();
		}
	},

	/* New heuristic */

	NEW_HEURISTIC {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int nextDest = 0;
			return nextDest;
		}
	};

	public abstract int chooseTargetFloor(LiftManager lm);
}
