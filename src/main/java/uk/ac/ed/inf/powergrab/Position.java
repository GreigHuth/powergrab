package uk.ac.ed.inf.powergrab;

public class Position {
	public double latitude;
	public double longitude;
	
	public Position(double latitude, double longitude) { 
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position nextPosition(Direction direction) { 
		
		Position nextPos = new Position(this.latitude, this.longitude);   
		
		nextPos.latitude  += direction.getHeight();
		nextPos.longitude += direction.getWidth();
		
		return nextPos;
		
	}
	
	
	
	public boolean inPlayArea() { 
		
		
		
		
		return false;
	}

}
