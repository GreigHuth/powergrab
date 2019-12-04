package uk.ac.ed.inf.powergrab;

//import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StatelessDrone extends Drone {
	
	private final Random rnd; 

	public StatelessDrone(Position position, int seed) {
		super(position);
		this.rnd = new Random(seed);
	}

	public Direction calculateDirection(ArrayList<Station> stations){
		
		Direction chosenMove = null;
 		
		
		ArrayList<Direction> legalDirections = legalDirections();
		ArrayList<Station> stationsInRange = stationsInRange(stations, legalDirections);
		
		//TODO consider fixing this
		
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
				
				//don't consider moving towards bad stations
				if(station.getMarker().equals("danger")) {
					continue;
				}
				
				if (station.getScore() > bestStation.getScore()) {
					bestStation = station;
				}
			}
			
			chosenMove = closestDirection(legalDirections, bestStation);
		}
		
		return chosenMove;

	}
	
	
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
		
				if (distance < RANGE && (station.getScore()>0)){
					
					stationsInRange.add(station);
					
				}
			}	
		}
		return stationsInRange;
	}
		
	
}
