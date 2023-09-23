package main;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Card {

	String suit;
	int rank;
	String rankName;
	Image icon;
	
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
				return "2";
			case 2:
				return "3";
			case 3:
				return "4";
			case 4:
				return "5";
			case 5:
				return "6";
			case 6:
				return "7";
			case 7:
				return "8";
			case 8:
				return "9";
			case 9:
				return "10";
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
		int scaledWidth = (int) (image.getWidth(null) * 0.1);
		int scaledHeight = (int) (image.getHeight(null) * 0.1);
		
		Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
		icon = scaledImage;
		
	}
	
	public String toString() {
		return rankName.charAt(0) + "";
	}
	
}
