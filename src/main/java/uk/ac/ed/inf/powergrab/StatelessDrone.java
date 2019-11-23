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
 		ArrayList<Direction> directions = new ArrayList<Direction>();
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
		
		ArrayList<Direction> legalMoves = new ArrayList<Direction>();
		ArrayList<Station> stationsInRange = new ArrayList<Station>();
		
		// makes sure it is moving within the play area
		for (Direction direction : directions) {
			if (this.position.nextPosition(direction).inPlayArea()) {
				legalMoves.add(direction);
			}
		}
		
		stationsInRange = findStationsInRange(map, legalMoves);
		
		if (stationsInRange.isEmpty()) {
			
		} 
		else if (stationsInRange.size() == 1){
				
		}
		else {
			
		}
		
		
		
		
		
		
		return null;

	}
	
	private ArrayList<Station> findStationsInRange(ArrayList<Station> map, ArrayList<Direction> legalMoves){
		
		ArrayList<Station> stationsInRange = new ArrayList<Station>();
		final double RANGE = 0.0025; // aoe of each station 
		
		for (Direction move : legalMoves) {
			Position nextPos = this.position.nextPosition(move);
			for (Station station : map) {
				double distance = distance(nextPos, station.position);
				if (distance < RANGE) {
					stationsInRange.add(station);
					map.remove(station);
				}
			}	
		}
		
		return stationsInRange;
		
		
	}
		
	
	
	
	
	


	
}
