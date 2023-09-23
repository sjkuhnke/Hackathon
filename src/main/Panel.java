package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
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
		
		getBestHand(new Card[] {new Card(6), new Card(0), new Card(5), new Card(1), new Card(7), new Card(8), new Card(10)});
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	
	public Card[] getBestHand(Card[] cards) {
		Card[] result = new Card[5];
		ArrayList<ArrayList<Card>> possibleHands = generateCombinations(cards);
		
		for (ArrayList<Card> hand : possibleHands) {
			System.out.println(hand.toString());
		}
		
		return result;
	}

	private ArrayList<ArrayList<Card>> generateCombinations(Card[] cards) {
		ArrayList<ArrayList<Card>> combinations = new ArrayList<>();
		
		generateCombinationsHelper(cards, 0, new ArrayList<>(), combinations);

        return combinations;
	}

	private void generateCombinationsHelper(Card[] cards, int index, ArrayList<Card> currentCombination, ArrayList<ArrayList<Card>> combinations) {
		if (currentCombination.size() == 5) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        if (index >= cards.length) {
            return;
        }

        // Include the current card in the combination
        currentCombination.add(cards[index]);
        generateCombinationsHelper(cards, index + 1, currentCombination, combinations);
        currentCombination.remove(currentCombination.size() - 1); // Backtrack

        // Exclude the current card from the combination
        generateCombinationsHelper(cards, index + 1, currentCombination, combinations);
		
	}
}
