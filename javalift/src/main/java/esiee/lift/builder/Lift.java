package esiee.lift.builder;

/**
 * Represents a Lift with its properties.
 *
 * @param maxCapacity  the maximum capacity of the lift
 * @param speed        the speed of the lift
 * @param doorTime     the time taken for the doors to open/close
 * @param acceleration the acceleration of the lift
 * @param deceleration the deceleration of the lift
 * @param energyCost   the energy cost per operation of the lift
 * @param id           the unique identifier of the lift
 * @see AGA
 */
public record Lift(int maxCapacity, int speed, int doorTime, int acceleration, int deceleration, double energyCost, String id) {}
