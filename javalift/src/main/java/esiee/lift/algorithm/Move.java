package esiee.lift.algorithm;

import esiee.lift.builder.Lift;

public class Move {
	public int moveLift(Lift lift, int dest) {
		int time = 0;

		if (dest == lift.getFloor())
			return time;
		else
			time += (Math.abs(dest - lift.getFloor()) * lift.getSpeed());

		return time;
	}
}
