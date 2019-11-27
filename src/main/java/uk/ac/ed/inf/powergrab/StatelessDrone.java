package uk.ac.ed.inf.powergrab;

import org.javatuples.Pair;

import java.util.*;
import java.util.Map.Entry;
import java.util.Random;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}

	public Direction calcMove(ArrayList<Station> map){
		
		Direction chosenMove = null;
		// THE PLAN
		// get all the legal moves, ie make sure it stays in the play area [x]
		// find all the stations that are in range of the drone after 1 move [x]
		// if #stations in range == 0 
				//      decide direction via rng
				// if #stations in range == 1
				        // if the station is a good one  
				        //     get the closest direction to the station
		                // if the station is a bad one
				        //     move in a random direction thats not in range of the station
				// if #stations in range >= 2 
				       //give each station a score
				       // pick the direction that is closest to the one with the highest score
		
		// list of all the directions the drone can move in
 		
		
		ArrayList<Direction> legalMoves = new ArrayList<Direction>();
		ArrayList<Station> stationsInRange = new ArrayList<Station>();
		
		// makes sure it is moving within the play area
		for (Direction direction : this.directions) {

			
			if (this.position.nextPosition(direction).inPlayArea()) {
				legalMoves.add(direction);
			}
		}
		
		stationsInRange = findStationsInRange(map, legalMoves);
		
		
		
		
		if (stationsInRange.isEmpty()) {
			chosenMove = randomDirection(legalMoves);	
		} 
		
		else if (stationsInRange.size() > 0){
			
			// pick the station with the highest score
			// pick the direction that is closest to the highest scoring station
			Station bestStation = new Station(null,null,0,0,null); // dummy station with low score
			
			//finds the best station
			for (Station station : stationsInRange) {
				if (station.getScore() > bestStation.getScore()) {
					bestStation = station;
				}
			}
			
			chosenMove = closestDirection(legalMoves, bestStation);
		}
		
		return chosenMove;

	}
	
	
	
	
	/*
	private Direction randomDirection(ArrayList<Direction> legalMoves, ArrayList<Station> stationsInRange) {
		
		Direction chosenMove = null;
		
		while (chosenMove ==  null) {
			//randomly picks a legal move to make
			Direction nextMove = legalMoves.get(this.rnd.nextInt(legalMoves.size()-1));
			for (Station station : stationsInRange) {
				double distance = distance(this.position.nextPosition(nextMove),station.position);
				if ( distance < 0.00025 && station.marker.equals("danger")  ) {
					continue;
				}
				chosenMove = nextMove;
			}
		}
		return chosenMove;
		
	}
	*/
	
	//returns a random direction to move in
    private Direction randomDirection(ArrayList<Direction> legalMoves) {
		int range = legalMoves.size() - 1;
		return legalMoves.get(this.rnd.nextInt(range));
		
	}
	
	
	
	//returns a list of all the stations in range of the moves the drone can make from its current position
	private ArrayList<Station> findStationsInRange(ArrayList<Station> mapCopy, ArrayList<Direction> legalMoves){
		
		ArrayList<Station> stationsInRange = new ArrayList<Station>();
		final double RANGE = 0.00025; // aoe of each station 
		
		for (Direction move : legalMoves) {
			Position nextPos = this.position.nextPosition(move);
			
			Iterator<Station> iter = mapCopy.iterator();
			
			while(iter.hasNext()) {
				Station station = iter.next();
				double distance = distance(nextPos, station.position);
		
				if (distance < RANGE && (station.marker.equals("danger") ==  false) && (station.getScore() > 0)) {
					stationsInRange.add(station);
					
				}
			}	
		}
		return stationsInRange;
	}
		
	
	
	
	
	


	
}
