package main;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Card {

	String suit;
	int rank;
	String rankName;
	BufferedImage icon;
	
	public Card(int id) {
		rank = id % 13; // 0 = Ace, 1 = Two, etc.
		suit = getSuit(id % 4); // 0 = Clubs, 1 = Diamonds, 2 = Hearts, 3 = Spades
		rankName = getRankName(rank);
		setIcon();
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
			return "Blank";
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
				return "Blank";
		}
	}
	
	private void setIcon() {
		BufferedImage image = null;
		
		String imageName = rankName + "_of_" + suit;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/cards/" + imageName + ".png"));
		} catch (Exception e) {
//			try {
//				image = ImageIO.read(getClass().getResourceAsStream("/sprites/001.png"));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			e.printStackTrace();
		}
		icon = image;
		
	}
	
}
