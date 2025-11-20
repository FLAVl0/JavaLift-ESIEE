package esiee.lift.local.builder;

import esiee.lift.local.Heuristics;

public class LiftManagerBuilder {

	private static int newId = 0;

	/* Lift parameters */

	private int maxCapacity = 10;
	private int maxSpeed = 1;
	private int doorTime = 2;

	private int acceleration = 1;
	private int deceleration = 1;
	private double energyCost = 0.1;

	private int minFloor = 0;
	private int maxFloor = 10;

	private int id;

	/* LiftManager parameters */

	private int initialCapacity = 0;
	private Integer initialFloor = null;
	private Heuristics heuristics = Heuristics.RANDOM;

	/* Methods */

	public LiftManagerBuilder() {
		this.id = newId++;
	}

	// Lift configuration methods

	public LiftManagerBuilder setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		return this;
	}

	public LiftManagerBuilder setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
		return this;
	}

	public LiftManagerBuilder setDoorTime(int doorTime) {
		this.doorTime = doorTime;
		return this;
	}

	public LiftManagerBuilder setAcceleration(int acceleration) {
		this.acceleration = acceleration;
		return this;
	}

	public LiftManagerBuilder setDeceleration(int deceleration) {
		this.deceleration = deceleration;
		return this;
	}

	public LiftManagerBuilder setEnergyCost(double energyCost) {
		this.energyCost = energyCost;
		return this;
	}

	public LiftManagerBuilder setMinFloor(int minFloor) {
		this.minFloor = minFloor;
		return this;
	}

	public LiftManagerBuilder setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
		return this;
	}

	public LiftManagerBuilder setId(int id) {
		this.id = id;
		return this;
	}

	// LiftManager configuration methods

	public LiftManagerBuilder setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
		return this;
	}

	public LiftManagerBuilder setInitialFloor(Integer initialFloor) {
		this.initialFloor = initialFloor;
		return this;
	}

	public LiftManagerBuilder setHeuristics(Heuristics heuristics) {
		this.heuristics = heuristics;
		return this;
	}

	public LiftManager build() {
		Lift lift = new Lift(
				maxCapacity,
				maxSpeed,
				doorTime,
				acceleration,
				deceleration,
				energyCost,
				minFloor,
				maxFloor,
				id);

		int startFloor = (initialFloor != null) ? initialFloor : minFloor;

		return new LiftManager(
				lift,
				initialCapacity,
				startFloor,
				heuristics);
	}
}
