package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
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
	
	//Number of players playing the hand
	int playersIn;
	
	// Pot fields
	double pot;
	JLabel potIcon;
	JLabel potText;
	
	// Keeps track of all players hands at end of each hand to compare
	Map<Player, ArrayList<Card>> playersHands;
	
	// Game state
	int cardsShown;

	public Panel(Image img, Player p, int playerCount) {
		this.p = p;
		playersIn = playerCount + 1;
		
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
			playerWallets[i] = new JLabel("<html><center><b>" + "$" + players[i].getWallet() + "</b><br>" + players[i].getName() + "<br>" + players[i].getBet() + "</center></html");
			playerWallets[i].setBounds(xPositions[i], yPositions[i], 60, 50);
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
		
		// Initializes pot
		potIcon = new JLabel("");
		potIcon.setIcon(new ImageIcon("pot.png"));
		potIcon.setBounds(460, 230, 55, 55);
		
		potText = new JLabel();
		potText.setText("$" + pot);
		potText.setFont(new Font(potText.getFont().getFontName(), Font.BOLD, 15));
		potText.setBounds(460, 265, 55, 55);
		
		potIcon.setOpaque(false);
		potText.setOpaque(false);
		add(potIcon);
		add(potText);
		
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
		console.setFont(new Font(console.getFont().getName(), Font.BOLD, 20));
		PrintStream printStream = new PrintStream(new CustomOutputStream(console));
		System.setOut(printStream);
		
		// Sets console in a scrollPane so you can scroll
		JScrollPane scrollPane = new JScrollPane(console);
		scrollPane.setBounds(200, 10, 600, 175);
		add(scrollPane);
		
		// Sets dealer initially to be one counterclockwise of user
		Player current = p;
		while (current.next() != p) {
			current = current.next();
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
			Player newGun = p.fold(this);
			advance();
			underGun = newGun;
		});
		userActions.add(foldButton);
		
		// Fold button initialization
		callButton = new JButton("Call");
		callButton.addActionListener(e -> {
			p.call(this, underGun.getBet());
			advance();
		});
		userActions.add(callButton);

		// Fold button initialization
		raiseButton = new JButton("Raise");
		raiseButton.addActionListener(e -> {
			underGun = actor.raise(5, this);
			advance();
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
		
		// Initializes the community cards
		communityDisplay = new JLabel[5];
		communityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		// Deals to all players (put in method) TODO
		dealCards(true);
		add(communityPanel);
	}
	
	private void dealCards(boolean init) {
		deck = new ArrayList<>();
		for (int i = 0; i < 52; i++) {
			deck.add(new Card(i));
		}
		
		Player currentDeal = dealer.next();
		removeCards(currentDeal);
		while (dealer.getCard2().id == -1) {
			if (currentDeal.getCard1().id == -1) {
				currentDeal.setCard1(dealCard());
			} else if (currentDeal.getCard2().id == -1) {
				addToPot(currentDeal, 5);
				currentDeal.setCard2(dealCard());
			}
			currentDeal = currentDeal.next();
		}
		System.out.println("\nThe hand has started and everyone put in $5.\n");
		updateCards();
		updatePot();
		updateWallets(dealer);
		
		community = new Card[5];
		for (int i = 0; i < 5; i++) {
			Card card = dealCard();
			community[i] = card;
			if (init) communityDisplay[i] = new JLabel();
			communityDisplay[i].setIcon(new ImageIcon(card.icon));
			
			if (init) communityPanel.add(communityDisplay[i]);
			if (i < 3) {
				communityDisplay[i].setVisible(true);
			} else {
				communityDisplay[i].setVisible(false);
			}
		}
		if (init) {
			communityPanel.setBounds(275, 315, 500, 150);
			communityPanel.setOpaque(false);
		}
		cardsShown = 3; // Initializes game state
	}

	public void updateWallets(Player dealer) {
		Player current = dealer;
		do {
			playerWallets[getIndex(current)].setText("<html><center><b>" + "$" + String.format("%.2f", current.getWallet()) + "</b><br>" + current.getName() + "<br>" + current.getBet() + "</center></html");
			current = current.next;
		} while (current != dealer);
		
	}

	private void updatePot() {
		potText.setText("$" + pot);
	}

	private void addToPot(Player current, int amt) {
		if (amt > current.getWallet()) {
			if (!current.npc) {
				JOptionPane.showMessageDialog(this, "Not enough money!");
				return;
			}
		}
		current.setWallet(current.getWallet() - amt);
		pot += amt;
		
	}

	private void removeCards(Player currentDeal) {
		Player current = currentDeal;
		do {
			current.setCard1(new Card(-1));
			current.setCard2(new Card(-1));
			current = current.next();
		} while (current != currentDeal);
		
	}

	private void advance() {
		Player newGun = actor.move(this);
		if (actor.next() == underGun) {
			boolean playing = endRound();
			if (!playing) return;
		}
		updateActor(actor);
		actor = actor.next();
		underGun = newGun;
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

	private boolean endRound() {
		if (cardsShown == 6) {
			endHand();
			return false;
		}
		Player current = underGun;
		do {
			pot += current.getBet();
			current.setBet(0);
			current = current.next;
		} while (current != underGun);
		updatePot();
		updateWallets(dealer);
		cardsShown++;
		if (cardsShown <= 5) {
			communityDisplay[cardsShown - 1].setVisible(true);
		}
		return true;
	}

	private String awardPot() {
		Map<Player, Integer> playersScores = new HashMap<>();
		
		for (Map.Entry<Player, ArrayList<Card>> e : playersHands.entrySet()) {
			Player player = e.getKey();
			ArrayList<Card> hand = e.getValue();
			int score = getHandValue(hand);
			playersScores.put(player,score);
		}
		int maxHandValue = Collections.max(playersScores.values());
		ArrayList<Player> winners = new ArrayList<>();
		for (Map.Entry<Player,Integer> e : playersScores.entrySet()) {
			if (e.getValue() == maxHandValue) {
				winners.add(e.getKey());
			}
		}
		distributePot(winners);
		return displayWinners(winners);
	}
	private void distributePot(ArrayList<Player> winners) {
		// Check if there are winners
	    if (winners.isEmpty()) {
	        // No winners, do nothing
	        return;
	    }

	    // Calculate the amount to be distributed to each winner
	    double potSize = pot; // Assuming you have a getPot() method to retrieve the pot size
	    double amountPerWinner = potSize / winners.size();

	    // Increment each winner's wallet by the calculated amount
	    for (Player winner : winners) {
	        double currentWallet = winner.getWallet(); // Get the current wallet balance
	        winner.setWallet(currentWallet + amountPerWinner); // Update the wallet balance
	    }

	    // Reset the pot to zero or whatever is appropriate in your game
		pot = 0;
	}

	private void endHand() {
		String winners = awardPot();
		
		JOptionPane.showMessageDialog(this, "Game is over!\n\n" + winners);
		
		setNumPlayers();
		playersHands = new HashMap<>();
		
		// Setting old dealer chip to not active
		int oldIndex = getIndex(dealer);
		dealerButtons[oldIndex].setVisible(false);
		
		updateActor(dealer);
		dealer = dealer.next();
		
		// Setting new dealer chip to active
		int dealerIndex = getIndex(dealer);
		dealerButtons[dealerIndex].setVisible(true);
		
		// Initally setting user to yellow (your turn)
		updateActor(dealer);
		actor = dealer.next();
		underGun = dealer.next();
		
		dealCards(false);
		updateActions();
	}

	private String displayWinners(ArrayList<Player> winners) {
		String result = "";
		String hand = scoreToString(getHandValue(playersHands.get(winners.get(0))));
		if (winners.size() > 1) {
			for (Player player : winners) {
				result += player.getName();
				result += ", ";
			}
			result += "all won with a " + hand + "!";
		} else {
			result += winners.get(0).getName() + " won with a " + hand + "!";
		}
		return result;
	}

	private void setNumPlayers() {
		Player current = dealer;
		do {
			if (current.getWallet() <= 0.00) {
				current.remove();
			} else if (!current.in) {
				playersIn++;
				current.in = true;
				int index = getIndex(current);
				playerWallets[index].setBackground(Color.white);
			}
			current = current.next;
		} while (current != dealer);
		
	}

	/**
	 * Updates visually which is active
	 * @param current - the one that was active, sets the next preemptively to yellow
	 */
	private void updateActor(Player current) {
		int currentIndex = getIndex(current);
		playerWallets[currentIndex].setBackground(Color.white);
		if (!current.in) playerWallets[currentIndex].setBackground(Color.gray);
		int nextIndex = getIndex(current.next());
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
	
	public void reveal(Player current){
		ArrayList<Card> bestHand = getBestHand(getYourCards(current));
		playersHands.put(current, bestHand);
		System.out.println("---------------------\n\n" + current.getName() + " had a " + scoreToString(getHandValue(bestHand)) + "!\n" + bestHand.toString() + "\n");
	}

}
