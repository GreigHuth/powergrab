package uk.ac.ed.inf.powergrab;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

import org.javatuples.Pair;

public class Drone {
	public ArrayList<Direction> directions = new ArrayList<Direction>();
	public Position position;
	public Random rnd ; //  need to have this here to ensure the seed works as intended
	public int seed;

	
	
	// constructor for drone same for every drone
	public Drone(Position position, int seed) {
		this.rnd = new Random(this.seed);
		this.position = position;
		this.seed = seed;
		
		//when the drone is initialised add all the directions to the attribute
		directions.add(Direction.N);
		directions.add(Direction.NNE);
		directions.add(Direction.NE);
		directions.add(Direction.ENE);
		directions.add(Direction.E);
		directions.add(Direction.ESE);
		directions.add(Direction.SE);
		directions.add(Direction.SSE);
		directions.add(Direction.S);
		directions.add(Direction.SSW);
		directions.add(Direction.SW);
		directions.add(Direction.WSW);
		directions.add(Direction.W);
		directions.add(Direction.WNW);
		directions.add(Direction.NW);
		directions.add(Direction.NNW);

	}

	// returns a list of all the moves that are legal (within the play area) from the drones current position
    public ArrayList<Direction> legalMoves(ArrayList<Direction> directions){
    
        ArrayList<Direction> legalMoves = new ArrayList<Direction>();
        
        for (Direction direction : this.directions) {	
		    if (this.position.nextPosition(direction).inPlayArea()) {
				legalMoves.add(direction);
			}
		}
        
        return legalMoves;
    }

	// picks the closest direction to the given station
	public Direction closestDirection(ArrayList<Direction> legalMoves, Station station) {
			
		Pair<Direction,Double> closestDirection = new Pair<Direction, Double>(null, 1000.0); //dummy move with high distance
		for(Direction direction : legalMoves) {
			double distance = distance(this.position.nextPosition(direction), station.position);
			if (distance < closestDirection.getValue1()) {
				closestDirection = new Pair<Direction,Double>(direction, distance);//idk if this is the best way, but it looks like it should work
			}
		}
			
			return closestDirection.getValue0();
			
		}
	
	//calculates the distance between the given points
	public static double distance(Position pos1, Position pos2) {
		
		return Math.sqrt( Math.pow((pos1.latitude - pos2.latitude),2) + Math.pow((pos1.longitude - pos2.longitude),2) );
		
	}
}
