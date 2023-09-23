package main;

public class Player {	
	
	private double wallet;
	private String name;
	
	private int card1;
	private int card2;
	
	public Player() {
		card1 = -1;
		card2 = -1;
		
		name = ""; // TODO: prompt user if null
		wallet = 0.0;
	}
	
	public String getName() {
		return name;
	}
	

}
