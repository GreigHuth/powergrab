package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

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
		
		
		
		
		
		
		
		return null;
	}

}
