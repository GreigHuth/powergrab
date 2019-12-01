package uk.ac.ed.inf.powergrab;

//import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}

	public Direction calculateDirection(ArrayList<Station> stations){
		
		Direction chosenMove = null;
 		
		
		ArrayList<Direction> legalDirections = legalDirections();
		ArrayList<Station> stationsInRange = stationsInRange(stations, legalDirections);
		
		
		// if there aren't any stations in range then pick a random direction to move in
		if (stationsInRange.isEmpty()) {
			chosenMove = randomDirection(legalDirections);	
		} 
		
		else {
			
			// pick the station with the highest score
			// pick the direction that is closest to the highest scoring station
			Station bestStation = new Station(null,null,0,0,null); // dummy station with low score
			
			//finds the best station
			for (Station station : stationsInRange) {
				if (station.getScore() > bestStation.getScore()) {
					bestStation = station;
				}
			}
			
			chosenMove = closestDirection(legalDirections, bestStation);
		}
		
		return chosenMove;

	}
	
	
	//TODO get this method to work. the drone sucks without it
	/*
	private Direction randomDirection(ArrayList<Direction> legalDirections, ArrayList<Station> stationsInRange) {
		
		Direction chosenMove = null;
		
		while (chosenMove ==  null) {
			//randomly picks a legal move to make
			Direction nextMove = legalDirections.get(this.rnd.nextInt(legalDirections.size()-1));
			for (Station station : stationsInRange) {
				double distance = distance(this.getPosition().nextPosition(nextMove),station.position);
				if ( distance < 0.00025 && station.marker.equals("danger")  ) {
					continue;
				}
				chosenMove = nextMove;
			}
		}
		return chosenMove;
		
	}
	*/
	
	//returns a random direction to move in given the legal moves the drone can make
    private Direction randomDirection(ArrayList<Direction> legalDirections) {                      
		int range = legalDirections.size() - 1;
		return legalDirections.get(this.rnd.nextInt(range));
		
	}
	
	
	//returns a list of all the stations in range of the moves the drone can make from its current position
    //using list so it fits on the line in the report 
	private ArrayList<Station> stationsInRange(List<Station> map, List<Direction> directions){
		
		ArrayList<Station> stationsInRange = new ArrayList<Station>();
		final double RANGE = 0.00025; // aoe of each station 
		
		for (Direction move : directions) {
			Position nextPos = this.getPosition().nextPosition(move);
			
			Iterator<Station> iter = map.iterator();
			
			while(iter.hasNext()) {
				Station station = iter.next();
				double distance = distance(nextPos, station.getPosition());
		
				if (distance < RANGE && (station.getMarker().equals("danger")==false)
						&& (station.getScore()>0)){
					
					stationsInRange.add(station);
					
				}
			}	
		}
		return stationsInRange;
	}
		
	
}
