package uk.ac.ed.inf.powergrab;

public class StatefulDrone extends Drone {

	public StatefulDrone(Position position, int seed) {
		super (position, seed);
	}
	
	public Direction calcNextMove(){
		
		// find stations in range
		// if there is  >1 station in range
		//    decide which one is better to move to
		//    find the closest of the 16 directions
		//    output that direction
		// else
		//    decide direction via RNG and a seed
		//    output that direction
		return null;
	}

}
