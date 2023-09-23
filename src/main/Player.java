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
	public double getWallet() {
		return wallet;
	}
	public Card getCard1(){
		return card1;
	}
	public Card getCard2() {
		return card2;
	}
	public String setName(String newName) {
		return this.name = newName;
	}
	public double setWallet(double newAmount) {
		return this.wallet = newAmount;
	}
	public Card setCard1(Card card) {
		return this.card1 = card;
	}
	public Card setCard2(Card card) {
		return this.card2 = card;
	}
}
