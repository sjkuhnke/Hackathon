package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
			Card card = dealCard();
			community[i] = card;
			cardLabel = new JLabel();
			cardLabel.setIcon(new ImageIcon(card.icon));
			
			communityPanel.add(cardLabel);
			//cardLabel.setVisible(false);
		}
		
		communityPanel.setBounds(100, 100, 1000, 200);
		add(communityPanel);
		
		ArrayList<Card> yourCards = new ArrayList<>();
		for (Card card : community) {
			yourCards.add(card);
		}
		yourCards.add(p.getCard1());
		yourCards.add(p.getCard2());
		
		getBestHand(yourCards);
		
//		addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                // Get the coordinates of the mouse click
//                int x = e.getX();
//                int y = e.getY();
//                
//                // Print the coordinates
//                System.out.println("Mouse Clicked at: X=" + x + ", Y=" + y);
//            }
//        });
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
	
	private Card[] getBestHand(ArrayList<Card> yourCards) {
		Card[] result = new Card[5];
		ArrayList<ArrayList<Card>> possibleHands = generateCombinations(yourCards);
		
		Map<ArrayList<Card>, Integer> scores = new HashMap<>();
		for (ArrayList<Card> hand : possibleHands) {
			scores.put(hand, getHandValue(hand));
		}
		
		System.out.print(Collections.max(scores.values()));
		
		return result;
	}

	private ArrayList<ArrayList<Card>> generateCombinations(ArrayList<Card> yourCards) {
		ArrayList<ArrayList<Card>> combinations = new ArrayList<>();
		
		generateCombinationsHelper(yourCards, 0, new ArrayList<>(), combinations);

        return combinations;
	}

	private void generateCombinationsHelper(ArrayList<Card> yourCards, int index, ArrayList<Card> currentCombination, ArrayList<ArrayList<Card>> combinations) {
		if (currentCombination.size() == 5) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        if (index >= yourCards.size()) {
            return;
        }

        // Include the current card in the combination
        currentCombination.add(yourCards.get(index));
        generateCombinationsHelper(yourCards, index + 1, currentCombination, combinations);
        currentCombination.remove(currentCombination.size() - 1); // Backtrack

        // Exclude the current card from the combination
        generateCombinationsHelper(yourCards, index + 1, currentCombination, combinations);
		
	}
	
	private int getHandValue(ArrayList<Card> cards) {
	    Map<Integer, Integer> rankCount = new HashMap<Integer, Integer>();
	    Map<String, Integer> suitCount = new HashMap<String, Integer>();

	    // Count each suit
	    for (Card card : cards) {
	    	suitCount.put(card.suit, suitCount.getOrDefault(card.suit, 0) + 1);
	    }

	    // Count each rank
	    for (Card card : cards) {
	        rankCount.put(card.rank, rankCount.getOrDefault(card.rank, 0) + 1);
	    }

	    // Check for straight
	    boolean isStraight = true;
	    ArrayList<Integer> rankList = new ArrayList<Integer>(rankCount.keySet());
	    Collections.sort(rankList);

	    for (int i = 0; i < rankList.size() - 1; i++) {
	        if (rankList.get(i + 1) != rankList.get(i) + 1) {
	            isStraight = false;
	            break;
	        }
	    }

	    int maxRankCount = Collections.max(rankCount.values());
	    int maxSuitCount = Collections.max(suitCount.values());

	    // Check for straight flush
	    if (maxSuitCount == 5 && isStraight) {
	        return 9;
	    }
	    // Check for quads
	    else if (maxRankCount == 4) {
	        return 8;
	    }
	    // Check for full house
	    else if (maxRankCount == 3 && Collections.frequency(rankCount.values(), 2) == 1) {
	        return 7;
	    }
	    // Check for flush
	    else if (maxSuitCount == 5) {
	        return 6;
	    }
	    // Check for straight
	    else if (isStraight) {
	        return 5;
	    }
	    // Check for trips
	    else if (maxRankCount == 3) {
	        return 4;
	    }
	    // Check for two pair
	    else if (Collections.frequency(rankCount.values(), 2) == 2) {
	        return 3;
	    }
	    // Check for pair
	    else if (maxRankCount == 2) {
	        return 2;
	    }
	    return 1;
	}

}
