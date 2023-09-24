package main;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Card {

	int id;
	String suit;
	int rank;
	String rankName;
	Image icon;
	
	public Card(int id) {
		this.id = id;
		this.rank = id % 13; // 0 = Two, 1 = Three, etc.
		this.suit = getSuit(id % 4); // 0 = Clubs, 1 = Diamonds, 2 = Hearts, 3 = Spades
		this.rankName = getRankName(rank);
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
				return "2";
			case 1:
				return "3";
			case 2:
				return "4";
			case 3:
				return "5";
			case 4:
				return "6";
			case 5:
				return "7";
			case 6:
				return "8";
			case 7:
				return "9";
			case 8:
				return "10";
			case 9:
				return "Jack";
			case 10:
				return "Queen";
			case 11:
				return "King";
			case 12:
				return "Ace";
			default:
				return "Blank";
		}
	}
	
	private void setIcon() {
		BufferedImage image = null;
		
		String imageName = rankName.toLowerCase() + "_of_" + suit.toLowerCase();
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/cards/" + imageName + ".png"));
		} catch (Exception e) {
//			try {
//				image = ImageIO.read(getClass().getResourceAsStream("/sprites/001.png"));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			System.out.println(imageName);
			e.printStackTrace();
		}
		int scaledWidth = (int) (image.getWidth(null) * 1);
		int scaledHeight = (int) (image.getHeight(null) * 1);
		
		Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
		icon = scaledImage;
		
	}
	
	@SuppressWarnings("unused")
	private String getSuitSymbol(String suit) {
	    switch (suit) {
	        case "Clubs":
	            return "♣"; // Club symbol (♣)
	        case "Diamonds":
	            return "♦"; // Diamond symbol (♦)
	        case "Hearts":
	            return "♥"; // Heart symbol (♥)
	        case "Spades":
	            return "♠"; // Spade symbol (♠)
	        default:
	            return ""; // Default to an empty string if the suit is not recognized
	    }
	}

	public String toString() {
	    String first = rankName.charAt(0) + "";
	    if (rankName.equals("10")) {
	        first = rankName;
	    }
	    String suitSymbol = "";
	    return first + suitSymbol;
	}
	
}
