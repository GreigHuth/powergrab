package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.ListIterator;

public class StatefulDrone extends Drone {

	public StatefulDrone(Position position, int seed) {
		super (position, seed);
	}
	
	//The stateful drone will always move to whatever unvisited "good" station is closest
	//if there is a bad station in the way 
	//	it will move to the second closest direction to its destination
	
	public Direction calculateDirection(Station destination, ArrayList<Station> badStations){
		// destination - the next closest station its trying to move to
		// badStations -  list of stations to avoid
		
		//----Structure-----
		//1. get a list of all potential directions to move in
		//2. rank all the directions to move in in terms of which is closest to the destination
		//3. for every element in this list
		//3.1	 pick the first direction in this list (ie the closest one)
		//3.2 	 if that direction is in range of any bad stations then move on to the next one
		//3.3    keep looping through until you find a direction that is not in range of a bad station 	
		//4. if no move has been found then pick the closest one to the destination
		//5. return that direction
		
		
		
		ArrayList<Direction> legalMoves = legalMoves(this.directions);
		
		ArrayList<Direction> orderedMoves = sortLegalMoves(legalMoves, destination);
		
		Direction chosenMove = null;
		
		
		for (Direction move : orderedMoves) {
			//if the closest move is in range of a bad station then try the next closest one
			if(inRange(move, badStations)) {
				continue;
				
			} else {
				
				chosenMove = move;
				break;
				
			}
		}
		
		// if all the above moves are in the range of a bad station then pick the original closest one
		if (chosenMove == null) {
			chosenMove = orderedMoves.get(0);
		}
		
		return chosenMove;
	}
	
	
	//returns a list of the possible moves the drone can make in ascending order of distance
	public ArrayList<Direction> sortLegalMoves(ArrayList<Direction> legalMoves, Station destination){
		
		
		ArrayList<Direction> orderedMoves = new ArrayList<Direction>();
		
		ListIterator<Direction> iter = legalMoves.listIterator();
		
		while(legalMoves.isEmpty() == false) {
			Direction closest = closestDirection(legalMoves,destination);
			orderedMoves.add(closest);
			legalMoves.remove(closest);
		}
		
		return orderedMoves;
	}
	
	//if the given direction results in a move that is in range of any bad stations, then return true
	public boolean inRange(Direction direction, ArrayList<Station> badStations) {
		//TODO if the move results in being in range of a bad Station but its still closer to the good Station
		//      then move in the direction closest to the good station
		
		
		
		
		boolean inRange = false;
		
		final double RANGE = 0.00025; 
		
		for (Station station : badStations) {
			double distance = distance(this.position.nextPosition(direction),station.position);
			if (distance < RANGE) {
				inRange = true;
				break;
			}
		}
		
		return inRange;
		
	}

}
