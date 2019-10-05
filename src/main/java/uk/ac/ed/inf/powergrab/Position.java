package uk.ac.ed.inf.powergrab;

public class Position {
	public double latitude; //x coord
	public double longitude; // y coord
	
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

		//the play area is a square so we only need the bottom left and top right points to verify-- 
		//    --if a point is within the square or not
		//bl = bottom left
		//tr = top right
		Float bl_lat  = 55.942617;
		Float bl_long = -3.192473;
		Float tr_lat  = 55.946233;
		Float tr_long = -3.184319;
		
		//new variables for the current position to make the algorithm more readable
        Float lat = this.latitude;
        Float lon = this.longitude;

        if (lat > bl_lat && lat < tr_lat && lon > bl_long && lon < tr_long ){
            return true;
        } else{
            return false;
        }
		
	}

}
