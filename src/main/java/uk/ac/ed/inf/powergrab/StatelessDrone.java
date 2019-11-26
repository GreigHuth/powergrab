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
			
			chosenMove = decideDirection(legalMoves, bestStation);
		}
		
		return chosenMove;

	}
	
	private Direction decideDirection(ArrayList<Direction> legalMoves, Station bestStation) {
		
		Pair<Direction,Double> bestMove = new Pair<Direction, Double>(null, 1000.0); //dummy move with high distance
		for(Direction direction : legalMoves) {
			double distance = distance(this.position.nextPosition(direction), bestStation.position);
			if (distance < bestMove.getValue1()) {
				bestMove = new Pair<Direction,Double>(direction, distance);//idk if this is the best way, but it looks like it should work
			}
		}
		
		return bestMove.getValue0();
		
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
	
private Direction randomDirection(ArrayList<Direction> legalMoves) {
		int range = legalMoves.size() - 1;
		return legalMoves.get(this.rnd.nextInt(range));
		
	}
	
	
	
	//this method also ignores dangerous and already drained stations
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
