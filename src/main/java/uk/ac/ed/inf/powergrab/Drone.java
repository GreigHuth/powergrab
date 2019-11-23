package uk.ac.ed.inf.powergrab;

import java.lang.Math;
import java.util.Random;

public class Drone {
	public Position position;
	public Random rnd ; //  need to have this here to ensure the seed works as intended
	public int seed;

	
	
	// constructor for drone same for every drone
	public Drone(Position position, int seed) {
		this.rnd = new Random(this.seed);
		this.position = position;
		this.seed = seed;

	}


	public Position getPos() {
		return this.position;
	}
	
	
	public double distance(Position pos1, Position pos2) {
		
		return Math.sqrt( Math.pow((pos1.latitude - pos2.latitude),2) + Math.pow((pos1.longitude - pos2.longitude),2) );
		
	}
}
