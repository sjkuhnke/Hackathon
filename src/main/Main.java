package main;

import javax.swing.*;

class Main {

	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Poker Game");
		
		String[] options = {"New Game", "Continue"};
		
		

        int choice = JOptionPane.showOptionDialog(
            null, 
            "Welcome to our poker game!\nby Shae K., Josh B., Tommy T. and Angel V.\n\nPlease, select an option:", 
            "Game Options", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]
        );
        String[] numOpponentsOptions = {"1", "2", "3", "4"};

        int numPlayersEntry = JOptionPane.showOptionDialog(
            null, 
            "How many opponents?", 
            "Game Options", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            numOpponentsOptions, 
            numOpponentsOptions[0]
        );
        int numPlayers = Integer.valueOf(numPlayersEntry) + 1;
        Player p = choiceHandler(choice);
        Panel gamePanel = new Panel(new ImageIcon("background.png").getImage(), p, numPlayers);
        
		window.add(gamePanel);
		gamePanel.add(new JLabel(choice + ""));
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public static Player choiceHandler(int choice) {
		if (choice == JOptionPane.YES_OPTION) {
			return new Player(false);
			//create a new game
		}
		else if (choice == JOptionPane.NO_OPTION) {
			//load an existing game
			return new Player(false);
		}
		else {
			return new Player(false);
		}
    }
}
