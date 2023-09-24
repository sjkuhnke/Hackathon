package main;

import java.util.Random;

public class Player {	
	
	private double wallet;
	private String name;
	
	private Card card1;
	private Card card2;
	
	public Player next;
	public boolean npc;
	public boolean in;
	
	public Player(boolean npc) {
		card1 = new Card(-1);
		card2 = new Card(-1);
		
		name = "Angel"; // TODO: prompt user if null
		wallet = 100;
		
		this.npc = npc;
		in = true;
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
	public void setWallet(double d) {
		this.wallet = d;
	}
	public void setCard1(Card c) { 
		card1 = c;
	}
	public void setCard2(Card c) {
		card2 = c;
	}
	
	public Player next() {
		Player current = this;
		do {
			current = current.next;
			if (current.in) break;
		} while (current != this);
		return current;
	}

	public void move(Panel panel) {
		if (panel.cardsShown == 6) {
			panel.reveal(this);
			return;
		}
		Random random = new Random();
		int choice = random.nextInt(2);
		if (choice == 0) {
			panel.underGun = fold(panel);
		}
	}

	public Player fold(Panel panel) {
		System.out.println(this.getName() + " folded!");
		this.in = false;
		panel.playersIn--;
		if (panel.underGun == this) {
			panel.underGun = this.next();
		}
		return panel.underGun;
		
	}

	public void remove() {
		Player current = this;
		while (current.next != this) {
			current = current.next;
		}
		current.next = this.next.next;
		this.next = null;
		
	}
}
