package esiee.lift.algorithm;

import esiee.lift.builder.*;

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
			int currentFloor = lm.getCurrentFloor(); // Get the current floor

			// Look for the closest target floor
			int targetFloor = currentFloor;

			return targetFloor; // Return the closest target floor
		}
	},

	/* Choose to only board going up */

	ALWAYS_UP {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int currentFloor = lm.getCurrentFloor();
			Lift lift = lm.getLift();

			return (currentFloor < lift.maxFloor()) ? currentFloor + 1 : lift.minFloor();
		}
	},

	/* Choose to only board going down */

	ALWAYS_DOWN {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			int currentFloor = lm.getCurrentFloor();
			Lift lift = lm.getLift();

			return (currentFloor > lift.minFloor()) ? currentFloor - 1 : lift.maxFloor();
		}
	},

	/* Minimize the number of stops */

	ENERGY_SAVING {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			// Placeholder logic for energy saving heuristic
			return lm.getCurrentFloor();
		}
	},
	
	/* Random choice */
	
	RANDOM {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			// Placeholder logic for random choice heuristic
			return (int) (Math.random() * lm.getLift().numberOfFloors());
		}
	},

	/* FIFO */

	FIFO {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			// Placeholder logic for FIFO heuristic
			return 0;
		}
	},

	/* Just stay put */
	
	IDLE {
		@Override
		public int chooseTargetFloor(LiftManager lm) {
			return lm.getCurrentFloor();
		}
	};

	public abstract int chooseTargetFloor(LiftManager lm);
}
