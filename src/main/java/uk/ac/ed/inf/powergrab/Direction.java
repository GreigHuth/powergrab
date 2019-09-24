package uk.ac.ed.inf.powergrab;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;


public enum Direction {
	
	//enum class to represent the directions
	//can all be called with Direction.X
	// they are given fixed w and h values as the drone always moves fixed 
	// 		distances for every direction
	
	 
	// D(width, height)
	// the width is amount to add to the longitude to get the new position
	N(0,  0.0003),
	NNE(0.0003*cos(toRadians(67.5)) ,0.003*sin(toRadians(67.5)) ),
	NE(0.0003*cos(toRadians(45)) , 0.003*sin(toRadians(45))),
	ENE(0.0003*cos(toRadians(22.5)),0.003*sin(toRadians(22.5))),
	E(0.0003, 0),
	
	ESE(0,-0),
	SE(0,-0),
	SSE(0,-0),
	S(0,-0),
	SSW(-0,-0),
	SW(-0,-0),
	WSW(-0,-0),
	W(-0,0),
	WNW(-0,0),
	NW(-0,0),
	NNW(-0,0);
	
	private double w; 
	private double h;
	
	Direction(double w, double h){
		this.w = w;
		this.h = h;
	}
	
	
	public double getWidth() {
		
		return this.w;
		
	}
	
	public double getHeight(){
		// the width is amount to add to the latitude to get the new position
		
		return this.h;
		
	}
	
}








