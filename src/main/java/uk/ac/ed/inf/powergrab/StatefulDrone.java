package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Iterator;

public class StatefulDrone extends Drone {
	
	private ArrayList<Station> goodStations = new ArrayList<Station>();
	private ArrayList<Station> badStations= new ArrayList<Station>();
	private ArrayList<Station> orderedStations= new ArrayList<Station>();
	
	
	public StatefulDrone(Position position, ArrayList<Station> stations) {
		super (position);
		seperateStations(stations);
		sortStations();
	}
	
	//TODO need to get perfect on 09/09 
	//there is an issue with danger stations being 
	
	//decides which direction to go in
	public Direction decideDirection(Station destination){
		
		ArrayList<Direction> legalMoves = legalDirections();
		
		ArrayList<Direction> orderedMoves = sortDirections(legalMoves, destination);
		
		Direction chosenMove = null;
		
		
		for (Direction move : orderedMoves) {
			//if the closest move is in range of a bad station then try the next closest one
			/*
			Position nextPos = this.getPosition().nextPosition(move);
			StatefulDrone lookahead = new StatefulDrone(nextPos, orderedStations);
			ArrayList<Direction> nextDirs = lookahead.sortDirections(legalDirections(), destination);
			
			Iterator<Direction> iter = nextDirs.listIterator();
			
			while (iter.hasNext()){
				if (inDanger(iter.next(),destination)) {
					iter.remove();
				}
			}
			Position nextNextPos = lookahead.getPosition().nextPosition(nextDirs.get(0));
			
			
			if(this.getPosition().equals(nextNextPos)) {
				continue;
			}
			*/
			if(inDanger(move, destination)) {
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
	
	
	//splits up stations into good and bad stations
	private void seperateStations(ArrayList<Station> stations) {
		
		for (Station station : stations) {
			if (station.getMarker().equals("danger")) {
				this.getBadStations().add(station);
			}
			else if (station.getMarker().equals("lighthouse")) {
				this.getGoodStations().add(station);
			}
		}
	}

	
	// sorts the stations by distance to the nearest station from the drones position at the time
	//      in ascending order
	private void sortStations(){
		
		Position currentPosition =  this.getPosition();
		
		//sort the stations based on which one is closest to the drone at that time
		while(this.getGoodStations().isEmpty() == false) {
			
			Station closestStation = getClosestStation(currentPosition, this.getGoodStations());
			this.getOrderedStations().add(closestStation);
			
			currentPosition = closestStation.getPosition();
			this.getGoodStations().remove(closestStation);
		}
		
	}
	
	
	//returns the closest station to the given position
	private static Station getClosestStation(Position position, ArrayList<Station> stations) {

		
		Position dummy = new Position (100,100);
	    Station closest = new Station("", dummy, 0, 0, "");
	    for (Station current : stations) {
	    	double nextDist = distance(position, current.getPosition());//distance to next drone to compare to
	    	double currentDist = distance(position, closest.getPosition());
	    	
	    	if (nextDist < currentDist) {
	    		closest = current;
	    	}
	    }    
	   return closest;
		
		
		
	}
	
	
	//returns a list of the possible moves the drone can make in ascending order of distance
	private ArrayList<Direction> sortDirections(ArrayList<Direction> directions, Station destination){
		
		
 		ArrayList<Direction> orderedMoves = new ArrayList<Direction>();
		
		while(directions.isEmpty() == false) {
			Direction closest = closestDirection(directions,destination);
			orderedMoves.add(closest);
			directions.remove(closest);
		}
		
		return orderedMoves;
	}
	
	
	//if the given direction results in a move that is in range of any bad stations, then return true
	private boolean inDanger(Direction chosenDirection, Station destination){
		
		boolean inRange = false;
		
		final double RANGE = 0.00025; 
		
		for (Station station : this.getBadStations()) {
			
			double distanceToBad = distance(
					this.getPosition().nextPosition(chosenDirection),
					station.getPosition());
			
			double distanceToDest = distance(
					this.getPosition().nextPosition(chosenDirection),
					destination.getPosition());
			
			if (distanceToDest <= RANGE && distanceToBad <= RANGE && distanceToDest < distanceToBad){
				inRange = false;
				break;
			}
			
			else if (distanceToBad < RANGE) {
				inRange = true;
				break;
			}
		
		}
		
		return inRange;
		
	}

	
	
	// -----GETTERS AND SETTERS-----
	public ArrayList<Station> getGoodStations() {
		return goodStations;
	}
	
	public void setGoodStations(ArrayList<Station> goodStations) {
		this.goodStations = goodStations;
	}

	public ArrayList<Station> getOrderedStations() {
		return orderedStations;
	}

	public void setOrderedStations(ArrayList<Station> orderedStations) {
		this.orderedStations = orderedStations;
	}

	public ArrayList<Station> getBadStations() {
		return badStations;
	}

	public void setBadStations(ArrayList<Station> badStations) {
		this.badStations = badStations;
	}

}
