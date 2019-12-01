package uk.ac.ed.inf.powergrab;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

import org.javatuples.Pair;

public class Drone {
	private final ArrayList<Direction> directions = new ArrayList<Direction>();
	private Position position;
	public final Random rnd ; 
	private final int seed;

	
	
	// constructor both drones use this constructor
	public Drone(Position position, int seed) {
		this.rnd = new Random(this.seed);
		this.setPosition(position);
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
    public ArrayList<Direction> legalDirections(){
    
        ArrayList<Direction> legalMoves = new ArrayList<Direction>();
        
        for (Direction direction : this.directions) {	
		    if (this.getPosition().nextPosition(direction).inPlayArea()) {
				legalMoves.add(direction);
			}
		}
        
        return legalMoves;
    }

	// finds the closest direction to the given station
	public Direction closestDirection(ArrayList<Direction> legalDirections, Station station) {
			
		//initialise with a dummy value so you can find the smallest one
		Pair<Direction,Double> closestDirection = new Pair<Direction, Double>(null, 1000.0);
		
		for(Direction direction : legalDirections) {
			
			double distance = distance(
					this.getPosition().nextPosition(direction), 
					station.getPosition());
			
			//if current direction is closer than the previous closest, then overwrite it
			if (distance < closestDirection.getValue1()) {
				closestDirection = new Pair<Direction,Double>(direction, distance);
			}
		}
			
			return closestDirection.getValue0();
		}
	
	//calculates the distance between the given points
	public static double distance(Position pos1, Position pos2) {
		
		return Math.sqrt( 
				Math.pow((pos1.getLatitude() - pos2.getLatitude()),2) 
				+ Math.pow((pos1.getLongitude() - pos2.getLongitude()),2) );
		
	}

	
	// -----GETTERS AND SETTERS-----
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
