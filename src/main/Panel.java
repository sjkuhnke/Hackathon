package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

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
	public ArrayList<Card> deck;
	JLabel card1, card2;
	JPanel communityPanel;
	JLabel[] communityDisplay;
	Card[] community;
	

	public Panel(Image img, Player p) {
		this.p = p;
		
		this.img = img;
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null);
		
		card1 = new JLabel();
		card1.setIcon(new ImageIcon(p.getCard1().icon));
		card1.setBounds(400, 535, 200, 200);
		add(card1);
		
		// card 2 TODO
		card2 = new JLabel();
		card2.setIcon(new ImageIcon(p.getCard2().icon));
		card2.setBounds(500, 535, 200, 200);
		add(card2);
		
		// Initialize deck (put in method?)
		deck = new ArrayList<>();
		for (int i = 0; i < 52; i++) {
			deck.add(new Card(i));
		}
		
		Player lastDeal = p; // Make blind
		while (lastDeal.getCard2().id == -1) {
			//Player current = lastDeal.next;
			if (p.getCard1().id == -1) p.setCard1(dealCard());
			if (p.getCard2().id == -1) p.setCard2(dealCard());
			// current = current.next
		}
		updateCards();
		
		communityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		communityDisplay = new JLabel[5];
		community = new Card[5];
		for (int i = 0; i < 5; i++) {
			JLabel cardLabel = communityDisplay[i];
			Card card = community[i];
			card = dealCard();
			cardLabel = new JLabel();
			cardLabel.setIcon(new ImageIcon(card.icon));
			
			communityPanel.add(cardLabel);
			//cardLabel.setVisible(false);
		}
		
		communityPanel.setBounds(100, 100, 1000, 200);
		add(communityPanel);
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the coordinates of the mouse click
                int x = e.getX();
                int y = e.getY();
                
                // Print the coordinates
                System.out.println("Mouse Clicked at: X=" + x + ", Y=" + y);
            }
        });
	}
	
	private void updateCards() {
		card1.setIcon(new ImageIcon(p.getCard1().icon));
		card2.setIcon(new ImageIcon(p.getCard2().icon));
	}

	private Card dealCard() {
		if (deck.size() == 0) throw new IndexOutOfBoundsException("No cards left in deck");
		Random random = new Random();
		int index = random.nextInt(deck.size());
		Card result = deck.get(index);
		deck.remove(result);
		return result;
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
