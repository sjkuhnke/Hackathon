package main;

import javax.swing.*;

class Main {
	Panel table = new Panel();
	
	public static void choiceHandler(int choice) {
		if (choice == 1) {
			//
		}
    }
	
	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Poker Game");
		
		Panel gamePanel = new Panel();
		
		String[] options = {"New Game", "Continue"};

        int choice = JOptionPane.showOptionDialog(
            null, 
            "Select an option:", 
            "Game Options", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]
        );
        
        
		window.add(gamePanel);
		gamePanel.add(new JLabel(choice + ""));
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
