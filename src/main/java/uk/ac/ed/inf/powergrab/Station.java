package uk.ac.ed.inf.powergrab;

public class Station {

	public String id ;
	public Position position;
	public double coins;
	public double power;
	public String marker;
	public double score; // used when in comparisons to other stations
	
	
	
	public Station(String id, Position position, double coins, double power, String marker) {
		this.id = id;
		this.position = position;
		this.coins = coins;
		this.power = power;
		this.marker = marker;
		this.score = this.coins + this.power;
	}

	
	public double getScore() {
		return this.coins + this.power;
	}
}
