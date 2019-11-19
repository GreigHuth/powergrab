package uk.ac.ed.inf.powergrab;

public class Position {
	public double latitude; //x coord
	public double longitude; // y coord
	
	public Position(double latitude, double longitude) { 
		// constructor
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	//possibly rename
	public Position nextPosition(Direction direction) { 
		//calculates the next position the drone moves to
		Position nextPos = new Position(this.latitude, this.longitude);   
		
		//height and width refer to the sides of a triangle used to calculate the next position
		nextPos.latitude  += direction.getHeight();
		nextPos.longitude += direction.getWidth();
		
		return nextPos;
		
	}
	
	
	
	public boolean inPlayArea() { 

		//the play area is a square so we only need the bottom-left and top-right points to verify 
		//    if a point is within the square or not
		//BL = bottom left
		//TR = top right
		final double BL_LAT  = 55.942617;
		final double BL_LON = -3.192473;
		final double TR_LAT  = 55.946233;
		final double TR_LON = -3.184319;
		
		//new variables for the current position to make the algorithm more readable
        double lat = this.latitude;
        double lon = this.longitude;

        if (lat > BL_LAT && lat < TR_LAT && lon > BL_LON && lon < TR_LON ){
            return true;
        } else{
            return false;
        }
		
	}

}
