package uk.ac.ed.inf.powergrab;

public class Drone {
	public Position position;
	public int seed;
	public double coins; // coins the drone has 
	public double power; // how much power the drone has
	public int moves; // how many moves the drone as left
	
	
	// constructor for drone same for every drone
	public Drone(Position position, int seed) {
		
		this.position = position;
		this.seed = seed;
		this.coins = 0.0;
		this.power = 250.0;
		this.moves = 250;
	}
	
	public void updateCoins(double newCoins) {	
		// coins can have both negative and positive values so just adding them to the current coin value will be fine
		this.coins += newCoins;	
	}
	
	public void updatePower(double newPower) {	
		this.power += newPower;
	}
	
	public void decrementMoves() {
		this.moves--;
	}
	
	public Position getPos() {
		return this.position;
	}
	
	public int getMoves() {
		return this.moves;
	}
	
	public double getCoins() {
		return this.coins;
	}

	public double getPower() {
		return this.power;
	}
	
}
