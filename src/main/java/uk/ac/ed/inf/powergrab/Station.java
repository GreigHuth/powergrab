package uk.ac.ed.inf.powergrab;

public class Station {

	public String id ;
	public Position position;
	public double coins;
	public double power;
	public String marker;
	
	
	
	public Station(String id, Position position, double coins, double power, String marker) {
		this.id = id;
		this.position = position;
		this.coins = coins;
		this.power = power;
		this.marker = marker;
	}

}
