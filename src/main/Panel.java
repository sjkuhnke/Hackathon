package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import library.CustomOutputStream;

public class Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Background image
	private Image img;
	
	// Setting player pointers
	public Player p;
	public Player dealer;
	public Player actor;
	public Player underGun;
	
	// Deck
	public ArrayList<Card> deck;
	
	// User card1 and card2 labels
	JLabel card1, card2;
	
	// Community card fields
	JPanel communityPanel;
	JLabel[] communityDisplay;
	Card[] community;
	
	// All player fields
	JLabel[] playerWallets;
	Player[] players;
	JLabel[] dealerButtons;
	
	// User action fields
	JPanel userActions;
	JButton foldButton;
	JButton callButton;
	JButton raiseButton;
	
	// User isn't up button to advance turn
	JButton nextButton;
	JButton revealButton;
	
	// Keeps track of all players hands at end of each hand to compare
	Map<Player, ArrayList<Card>> playersHands;
	
	// Game state
	int cardsShown;

	public Panel(Image img, Player p, int playerCount) {
		this.p = p;
		
		// Set background image
		this.img = img;
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null);
		
		// Initialize player wallets, dealer buttons, and Player array
		playerWallets = new JLabel[playerCount + 1];
		dealerButtons = new JLabel[playerCount + 1];
		players = new Player[playerCount + 1];
		
		// Sets positions of each wallet JLabel
		int[] xPositions = new int[] {465, 195, 195, 724, 724};
		int[] yPositions = new int[] {484, 446, 280, 280, 446};
		for (int i = 0; i <= playerCount; i++) {
			// Sets user player
			if (i == 0) {
				players[i] = p;
			} else {
				// Sets AI player
				players[i] = new Player(true);
				players[i].setName("Player " + (i + 1));
				players[i - 1].next = players[i];
			}
			// Makes list cyclic
			if (i == playerCount) {
				players[i].next = p;
			}
			
			// Sets text and bounds of each Player wallet
			playerWallets[i] = new JLabel("<html><center><b>" + "$" + players[i].getWallet() + "</b><br>" + players[i].getName() + "</center></html");
			playerWallets[i].setBounds(xPositions[i], yPositions[i], 60, 35);
			playerWallets[i].setOpaque(true);
			playerWallets[i].setBackground(Color.WHITE);
			
			// Sets bounds of dealer buttons
			dealerButtons[i] = new JLabel("");
			dealerButtons[i].setEnabled(true);
			dealerButtons[i].setIcon(new ImageIcon("dealer.png"));
			dealerButtons[i].setBounds(xPositions[i], yPositions[i] + 30, 44, 44);
			dealerButtons[i].setOpaque(false);
			dealerButtons[i].setVisible(false);
			
			add(playerWallets[i]);
			add(dealerButtons[i]);
		}
		
		// Initializes user's card 1 slot
		card1 = new JLabel();
		card1.setIcon(new ImageIcon(p.getCard1().icon));
		card1.setBounds(400, 535, 200, 200);
		add(card1);
		
		// Initializes user's card 1 slot
		card2 = new JLabel();
		card2.setIcon(new ImageIcon(p.getCard2().icon));
		card2.setBounds(500, 535, 200, 200);
		add(card2);
		
		// Initializes console area and sets System.out to it
		JTextArea console = new JTextArea();
		console.setEditable(false);
		console.setFont(new Font(console.getFont().getName(), Font.BOLD, 16));
		PrintStream printStream = new PrintStream(new CustomOutputStream(console));
		System.setOut(printStream);
		
		// Sets console in a scrollPane so you can scroll
		JScrollPane scrollPane = new JScrollPane(console);
		scrollPane.setBounds(200, 10, 600, 175);
		add(scrollPane);
		
		// Initialize deck (put in method?) TODO
		deck = new ArrayList<>();
		for (int i = 0; i < 52; i++) {
			deck.add(new Card(i));
		}
		
		// Sets dealer initially to be one counterclockwise of user
		Player current = p;
		while (current.next != p) {
			current = current.next;
		}
		// Sets the dealer field to be the one before the player
		dealer = current;
		
		// Initally setting dealer to one counterclockwise of user
		int dealerIndex = getIndex(dealer);
		dealerButtons[dealerIndex].setVisible(true);
		
		// Initally setting user to yellow (your turn)
		updateActor(dealer);
		actor = p;
		underGun = p;
		
		userActions = new JPanel(new GridLayout(1, 0));
		
		// Fold button initialization
		foldButton = new JButton("Fold");
		foldButton.addActionListener(e -> {
			System.out.println("not implemented");
		});
		userActions.add(foldButton);
		
		// Fold button initialization
		callButton = new JButton("Call");
		callButton.addActionListener(e -> {
			advance();
		});
		userActions.add(callButton);

		// Fold button initialization
		raiseButton = new JButton("Raise");
		raiseButton.addActionListener(e -> {
			System.out.println("not implemented");
		});
		userActions.add(raiseButton);
		
		userActions.setBounds(40, 595, 300, 60);
		add(userActions);
		
		// Next button initialization
		nextButton = new JButton("Next");
		nextButton.addActionListener(e -> {
			advance();
		});
		nextButton.setBounds(710, 595, 100, 60);
		nextButton.setVisible(false);
		add(nextButton);
		
		// Reveal button initialization
		revealButton = new JButton("Reveal");
		revealButton.addActionListener(e -> {
			reveal(p);
			advance();
		});
		revealButton.setBounds(150, 595, 100, 60);
		revealButton.setVisible(false);
		add(revealButton);
		
		// Players hands initialization
		playersHands = new HashMap<>();
		
		// Deals to all players (put in method) TODO
		Player currentDeal = dealer.next;
		while (dealer.getCard2().id == -1) {
			if (currentDeal.getCard1().id == -1) {
				currentDeal.setCard1(dealCard());
			} else if (currentDeal.getCard2().id == -1) {
				currentDeal.setCard2(dealCard());
			}
			currentDeal = currentDeal.next;
		}
		updateCards();
		
		// Flips the community cards initally setting only the flop to visible
		communityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		communityDisplay = new JLabel[5];
		community = new Card[5];
		for (int i = 0; i < 5; i++) {
			Card card = dealCard();
			community[i] = card;
			communityDisplay[i] = new JLabel();
			communityDisplay[i].setIcon(new ImageIcon(card.icon));
			
			communityPanel.add(communityDisplay[i]);
			if (i < 3) {
				communityDisplay[i].setVisible(true);
			} else {
				communityDisplay[i].setVisible(false);
			}
		}
		communityPanel.setBounds(284, 310, 500, 125);
		communityPanel.setOpaque(false);
		cardsShown = 3; // Initializes game state
		add(communityPanel);
		
		revealAll(dealer);
		
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
//		Player test = p;
//		while(test.next != null) {
//			System.out.println(test.getName());
//			test = test.next;
//		}
	}
	
	private void advance() {
		if (actor.npc) actor.move(this);
		if (actor.next == underGun) {
			endRound();
		}
		updateActor(actor);
		actor = actor.next;
		updateActions();
	}
	
	private void updateActions() {
		if (cardsShown == 6 && actor == p) {
			revealButton.setVisible(true);
			userActions.setVisible(false);
			nextButton.setVisible(false);
			return;
		}
		revealButton.setVisible(false);
		if (actor == p) {
			userActions.setVisible(true);
			nextButton.setVisible(false);
		} else {
			userActions.setVisible(false);
			nextButton.setVisible(true);
		}
		
	}

	private void endRound() {
		if (cardsShown == 6) {
			endHand();
			return;
		}
		cardsShown++;
		if (cardsShown <= 5) {
			communityDisplay[cardsShown - 1].setVisible(true);
		}
	}

	private void endHand() {
		JOptionPane.showMessageDialog(this, "Game is over!");
		
	}

	/**
	 * Updates visually which is active
	 * @param current - the one that was active, sets the next preemptively to yellow
	 */
	private void updateActor(Player current) {
		int currentIndex = getIndex(current);
		playerWallets[currentIndex].setBackground(Color.white);
		int nextIndex = getIndex(current.next);
		playerWallets[nextIndex].setBackground(Color.yellow);
	}

	private int getIndex(Player dealer) {
		Player current = p;
		int di = 0;
		do {
			if (current == dealer) return di;
			di++;
			current = current.next;
		}
		while(current != p);
		return -1;
	}

	private ArrayList<Card> getYourCards(Player current) {
		ArrayList<Card> yourCards = new ArrayList<>();
		for (Card card : community) {
			yourCards.add(card);
		}
		
		yourCards.add(current.getCard1());
		yourCards.add(current.getCard2());
		
		return yourCards;
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
	
	private ArrayList<Card> getBestHand(ArrayList<Card> yourCards) {
		ArrayList<ArrayList<Card>> possibleHands = generateCombinations(yourCards);
		
		Map<ArrayList<Card>, Integer> scores = new HashMap<>();
		for (ArrayList<Card> hand : possibleHands) {
			scores.put(hand, getHandValue(hand));
		}
		
		int bestScore = 0;
		ArrayList<Card> bestHand = null;
		
		for (Map.Entry<ArrayList<Card>, Integer> entry : scores.entrySet()) {
			int score = entry.getValue();
			if (score > bestScore) {
				bestScore = score;
				bestHand = entry.getKey();
			}
		}
		
		return bestHand;
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

	    if (rankList.size() == 5) {
		    for (int i = 0; i < rankList.size() - 1; i++) {
		        if (rankList.get(i + 1) != rankList.get(i) + 1) {
		            isStraight = false;
		            break;
		        }
		    }
	    }
	    else {
	    	isStraight = false;
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
	String scoreToString(int score) {
		switch (score) {
			case 9:
				return "Straight Flush";
			case 8:
				return "Four of a Kind";
			case 7:
				return "Full House";
			case 6:
				return "Flush";
			case 5:
				return "Straight";
			case 4:
				return "Three of a Kind";
			case 3:
				return "Two Pair";
			case 2:
				return "Pair";
			default:
				return "High Card";
			}
		}
	public void revealAll(Player dealer){

		Player currentReveal = dealer.next;
		do {
			//System.out.println(getBestHand(getYourCards(currentReveal)) + "\n");
			currentReveal = currentReveal.next;
		} while (currentReveal != dealer.next);
	}
	
	public void reveal(Player current){
		ArrayList<Card> bestHand = getBestHand(getYourCards(current));
		playersHands.put(current, bestHand);
		System.out.println("---------------------\n\n" + current.getName() + " had a " + scoreToString(getHandValue(bestHand)) + "!\n" + bestHand.toString() + "\n");
	}

}
