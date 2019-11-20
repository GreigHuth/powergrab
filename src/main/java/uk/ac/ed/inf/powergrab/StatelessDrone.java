package uk.ac.ed.inf.powergrab;

import org.javatuples.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.Random;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}

	public Pair<Direction, Position> calcNextMove(ArrayList<Feature> features){
		
		// find stations in range
		// if there is  > 0 stations in range
		//    decide which one is better to move to
		//    find the closest of the 16 directions to the chosen station
		//    output direction and new postion
		
		// else
		//    decide direction via RNG and a seed
		//    output direction and new position
		
		ArrayList<Feature> inRange = findStationsInRange(features);
		Direction dir;
		Pair<Direction, Position> chosenMove = null;
		
		
		
		if (inRange.size() > 0) {
			Feature chosenStation = decideStation(inRange);
			
			chosenMove = decideMove(chosenStation); 
		} else {
			switch (rnd.nextInt(16)) {
			case 1  :dir = Direction.N;
			case 2  :dir = Direction.NNE;
			case 3  :dir = Direction.NE;
			case 4  :dir = Direction.ENE;
			case 5  :dir = Direction.E;
			case 6  :dir = Direction.ESE;
			case 7  :dir = Direction.SE;
			case 8  :dir = Direction.SSE;
			case 9  :dir = Direction.S;
			case 10 :dir = Direction.SSW;
			case 11 :dir = Direction.SW;
			case 12 :dir = Direction.WSW;
			case 13 :dir = Direction.W;
			case 14 :dir = Direction.WNW;
			case 15 :dir = Direction.NW;
			case 16 :dir = Direction.NNW;
			
			chosenMove = new Pair<Direction, Position>(dir, this.position.nextPosition(dir));
			}
			
			
		}
		
		return chosenMove;
		
		
		

	}
		
	
	
	//decides the direction to move in once the station to move to has been decided
	private Pair<Direction, Position> decideMove(Feature station) {
		//only called if a drone is in range
		ArrayList<Pair<Direction, Position>> possibleMoves = new ArrayList<Pair<Direction,Position>>();
		
		for (int i = 0; i < 16; i++) {
		
		}
		
		// calculate all the possible positions the drone can move to
		// remove all positions not in play area
		// pick position thats the shortest distance from the station
		// return that Direction and Point
		
		return null;
	}
	
	
	//decides which station is the best one to move to
	private Feature decideStation(ArrayList<Feature> inRange) {
		
		return null;
	}
	
	
	
	
	//finds all the station in range of the drone
	private ArrayList<Feature> findStationsInRange(ArrayList<Feature> features){
		
		ArrayList<Feature> inRange = new ArrayList<Feature>();// stores the features in range of the drone 
		
		double droneRange = 0.0006; // allowed lookahead for the drone is 1 move so the range is double what it can do in one move
        double stationRange = 0.00025; // if a drone comes within this range of a station it counts as being in range of the station
		
		for (int i = 0; i <features.size(); i++ ) {	
			
			//puts the point into a position to make it easier to pass to the distance    
			Position station = unpackCoords(features.get(i));
			
			if (station == null) { continue;} // if the current feature isnt a point move on
			
			if (distance(station, this.position) > (droneRange + stationRange)) {
				continue;
			}else {
				inRange.add(features.get(i));		
			}	
		}
		
		return inRange;
		
	}
	
	// method to unpack the lat and lon from the Feature object
	private Position unpackCoords(Feature feature) {
		
		Point point ;
		if (feature.geometry() instanceof Point) { // makes sure we actually get an object thats a Point
			point = (Point) feature.geometry(); //cast to a point
		}else {
			return null;
		}
		Position station = new Position(point.latitude(), point.longitude());
		return station;
	}
	
	


	
}
