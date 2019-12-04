package uk.ac.ed.inf.powergrab;

public class Position {
	private double latitude; //x coord
	private double longitude; // y coord
	
	public Position(double latitude, double longitude) { 
		// constructor
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}
	
	//possibly rename
	public Position nextPosition(Direction direction) { 
		//calculates the next position the drone moves to
		Position nextPos = new Position(this.getLatitude(), this.getLongitude());   
		
		//height and width refer to the sides of a triangle used to calculate the next position
		nextPos.setLatitude(nextPos.getLatitude() + direction.getHeight());
		nextPos.setLongitude(nextPos.getLongitude() + direction.getWidth());
		
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
        double lat = this.getLatitude();
        double lon = this.getLongitude();

        if (lat > BL_LAT && lat < TR_LAT && lon > BL_LON && lon < TR_LON ){
            return true;
        } else{
            return false;
        }
		
	}
	
	
	public boolean equals(Position pos) {
	
		if(this.getLatitude() == pos.getLatitude() && this.getLongitude() == pos.getLongitude()) {
			return true;
		} else {
			return false;
		}
	}

	public double getLatitude() {
		return latitude;
	}

	private void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	private void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
