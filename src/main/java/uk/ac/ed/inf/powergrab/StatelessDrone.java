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
 		
		
		ArrayList<Direction> legalMoves = legalMoves(this.directions);
		ArrayList<Station> stationsInRange = findStationsInRange(map, legalMoves);
		
		
		// if there arent any stations in range then pick a random direction to move in
		if (stationsInRange.isEmpty()) {
			chosenMove = randomDirection(legalMoves);	
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
	
	//returns a random direction to move in given the legal moves the drone can make
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
