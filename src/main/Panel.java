package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Image img;
	public Player p;

	public Panel(Image img, Player p) {
		this.img = img;
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null);
		
		JLabel card1 = new JLabel();
		card1.setIcon(new ImageIcon(p.getCard1().icon));
		card1.setBounds(100, 100, 200, 200);
		add(card1);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	
	public Card[] getBestHand(Card[] cards) {
		Card[] result = new Card[5];
		List<List<Card>> possibleHands = generateCombinations(cards);
		return result;
	}

	private List<List<Card>> generateCombinations(Card[] cards) {
		// TODO Auto-generated method stub
		return null;
	}
}
