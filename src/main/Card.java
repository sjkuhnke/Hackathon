package main;

public class Card {

	String suit;
	int rank;
	String rankName;
	
	public Card(int id) {
		rank = id % 13; // 0 = Ace, 1 = Two, etc.
		suit = getSuit(id % 4); // 0 = Clubs, 1 = Diamonds, 2 = Hearts, 3 = Spades
		rankName = getRankName(rank);
	}

	private String getSuit(int i) {
		switch (i) {
		case 0:
			return "Clubs";
		case 1:
			return "Diamonds";
		case 2:
			return "Hearts";
		case 3:
			return "Spades";
		default:
			return "Empty";
		}
	}
		
	private String getRankName(int rank) {
		switch (rank) {
			case 0:
				return "Ace";
			case 1:
				return "Two";
			case 2:
				return "Three";
			case 3:
				return "Four";
			case 4:
				return "Five";
			case 5:
				return "Six";
			case 6:
				return "Seven";
			case 7:
				return "Eight";
			case 8:
				return "Nine";
			case 9:
				return "Ten";
			case 10:
				return "Jack";
			case 11:
				return "Queen";
			case 12:
				return "King";
			default:
				return "Empty";
		}
	}
	
}
