package main;

public class Player {	
	
	private double wallet;
	private String name;
	
	private Card card1;
	private Card card2;
	
	public Player() {
		card1 = new Card(-1);
		card2 = new Card(-1);
		
		name = ""; // TODO: prompt user if null
		wallet = 0.0;
	}
	
	public String getName() {
		return name;
	}
	

}
