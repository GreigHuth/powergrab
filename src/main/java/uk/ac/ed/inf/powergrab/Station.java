package uk.ac.ed.inf.powergrab;

public class Station {

	private String id ;
	private Position position;
	private double coins;
	private double power;
	private String marker;
	//TODO think about removing this
	
	
	public Station(String id, Position position, double coins, double power, String marker) {
		this.setId(id);
		this.setPosition(position);
		this.setCoins(coins);
		this.setPower(power);
		this.setMarker(marker);
	}

	
	public double getScore() {
		return this.getCoins() + this.getPower();
	}

	// -----GETTERS AND SETTERS-----
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public double getCoins() {
		return coins;
	}

	public void setCoins(double coins) {
		this.coins = coins;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}
}
