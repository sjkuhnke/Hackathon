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
	private double bet;
	
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

	public Player move(Panel panel) {
		if (!this.npc) return panel.underGun;
		if (panel.cardsShown == 6) {
			panel.reveal(this);
			return panel.underGun;
		}
		Random random = new Random();
		int choice = random.nextInt(4);
		if (choice == 0) {
			return fold(panel);
		}
		if (choice == 1) {
			return raise(panel.underGun.bet + 5, panel);
		} else {
			call(panel, panel.underGun.bet);
		}
		return panel.underGun;
	}

	public Player fold(Panel panel) {
		System.out.println(this.getName() + " folded!\n");
		this.in = false;
		panel.playersIn--;
		if (panel.underGun == this) {
			return this.next();
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
	
	public double getBet() {
		return bet;
	}
	
	public void setBet(double bet) {
		this.bet = bet;
	}
	
	public void addBet(double bet) {
		this.bet += bet;
	}
	
	public boolean bet(double betAmount, boolean announce) {
	    if (betAmount <= 0 || betAmount > wallet) {
	        return false;
	    }

	    if (announce) {
	        System.out.println(this.getName() + " called $" + betAmount);
	    }

	    this.bet += betAmount;
	    this.wallet -= betAmount;
	    return true;
	}

	public Player raise(double d, Panel panel) {
		if (d <= 0 || d > wallet) {
	        return null; // Invalid raise amount
	    }
		
		double currentBet = panel.underGun.bet;
	    double totalBet = currentBet + d;
		
	    if (!bet(totalBet, false)) {
	        // Call if the raise fails
	        call(panel, currentBet);
	        return panel.underGun;
	    }

	    // Update the current bet amount
	    panel.underGun.bet = totalBet;

	    panel.updateWallets(this);
	    System.out.println(this.getName() + " raised $" + d);
	    return this;
	}

	public void call(Panel panel, double bet) {
		double amountToCall = bet - this.bet;

	    if (amountToCall < 0 || amountToCall > wallet) {
	        amountToCall = wallet;
	    }

	    bet(amountToCall, true);
	    panel.updateWallets(panel.dealer);
		
	}
}
