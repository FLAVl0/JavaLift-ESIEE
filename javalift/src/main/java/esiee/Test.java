package esiee;

import esiee.lift.*;

public class Test {
	public static void main(String[] args) {
		AGA aga = new AGA();

		System.out.println("Hello, JavaLift!");



		System.out.println("Number of lifts: " + aga.getLifts().size());
	}
}
