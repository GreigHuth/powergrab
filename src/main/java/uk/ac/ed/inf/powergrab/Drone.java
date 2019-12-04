package uk.ac.ed.inf.powergrab;

import java.lang.Math;
import java.util.ArrayList;

import org.javatuples.Pair;

public class Drone {
	private Position position;
	
	// constructor both drones use this constructor
	public Drone(Position position) {
		this.setPosition(position);
	}

	// returns a list of all the moves that are within the play area from the drones current position
    public ArrayList<Direction> legalDirections(){
    
        ArrayList<Direction> legalMoves = new ArrayList<Direction>();
        
        for (Direction direction :Direction.values()) {	
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
