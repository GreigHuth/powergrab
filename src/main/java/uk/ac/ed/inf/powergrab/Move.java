package uk.ac.ed.inf.powergrab;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

public class Move {
	public Direction direction; // the direction the move will go in
	public Feature station; // the nearest station in range
	

	public Move(Direction dir , Feature station) {
		this.direction = dir;
		this.station = station;
		
		
	}
	
}
	
