/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/*
 *
 * LeaderBoard.java is a class that updates, maintains, and
 * displays a leaderboard.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class LeaderBoard extends JFrame {
	private ArrayList<String> users;
	private ArrayList<String> highScores;
	private ArrayList<String> times;
	private ArrayList<String> difficulties;
	private ArrayList<String> sizes;
	private JPanel mainPanel;
	private User user;
	
    /**
	 * Constructor
	 * @param currentUser  sets the User of the game
	 */
	public LeaderBoard(User currentUser) {
		user = currentUser;
		users = new ArrayList<String>();
		highScores = new ArrayList<String>();
		times = new ArrayList<String>();
		difficulties = new ArrayList<String>();
		sizes = new ArrayList<String>();
		this.setLayout(new BorderLayout());
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		JPanel spacePanel = new JPanel();
		spacePanel.setLayout(new BoxLayout(spacePanel,BoxLayout.X_AXIS));
		spacePanel.add(Box.createRigidArea(new Dimension(50,0)));
		spacePanel.setBackground(Color.WHITE);
		JPanel backPanel = new JPanel(new FlowLayout());
		backPanel.setBackground(Color.WHITE);
		JButton backToMenu = new JButton("Back To Main Menu");
		backToMenu.addActionListener (new ActionListener() {
            
            /**
			 * Action Event after backToMenu button is pressed
			 * It send the user back to the main menu
			 * @param ae begins Action Event
			 */
	    	public void actionPerformed(ActionEvent ae) {
	    		MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");
				dispose();
	    	}
	    });
		backPanel.add(backToMenu);
		JTextPane titleMessage = new JTextPane();
		titleMessage.setEditable(false);
		StyledDocument doc2 = (StyledDocument) titleMessage.getDocument();
		SimpleAttributeSet messageFont = new SimpleAttributeSet();
	    StyleConstants.setFontFamily(messageFont, "Serif");
	    StyleConstants.setFontSize(messageFont, 32);
	    StyleConstants.setForeground(messageFont, Color.darkGray);
	    StyleConstants.setAlignment(messageFont, StyleConstants.ALIGN_CENTER);
	    try {
	    	doc2.insertString(doc2.getLength(), "Leader Board for High Scores", messageFont );
	 	} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	 	}	
	    this.add(titlePanel, BorderLayout.NORTH);
	    this.add(mainPanel, BorderLayout.CENTER);
	    this.add(spacePanel, BorderLayout.WEST);
	    this.add(backPanel, BorderLayout.SOUTH);
	    
	    titlePanel.add(titleMessage);
	    createLeaderBoard();
	}
    
    /**
     * This method creates and displays the leader board
     */
	public void createLeaderBoard() {
		int scoreNumber = numberOfScores();
		sortScoreInfo();
		mainPanel.setLayout(new GridLayout(scoreNumber+1, 6));
		JLabel rankDisplay = new JLabel("Rank");
		JLabel userNameDisplay = new JLabel("User Name");
		JLabel highScoreDisplay = new JLabel("Score");
		JLabel timeDisplay = new JLabel("Time Taken");
		JLabel difficultyDisplay = new JLabel("Difficulty");
		JLabel sizeDisplay = new JLabel("Board Size");
		mainPanel.add(rankDisplay);
		mainPanel.add(userNameDisplay);
		mainPanel.add(highScoreDisplay);
		mainPanel.add(timeDisplay);
		mainPanel.add(difficultyDisplay);
		mainPanel.add(sizeDisplay);
		ListIterator<String> userIterator, scoreIterator, timeIterator, difficultyIterator, sizeIterator;
		userIterator = users.listIterator();
		scoreIterator = highScores.listIterator();
		timeIterator = times.listIterator();
		difficultyIterator = difficulties.listIterator();
		sizeIterator = sizes.listIterator();
		for(int i = 1; i <= scoreNumber; i++) {
			mainPanel.add(new JLabel(String.valueOf(i)));
			mainPanel.add(new JLabel(userIterator.next()));
			mainPanel.add(new JLabel(scoreIterator.next()));
			mainPanel.add(new JLabel(timeIterator.next()+" seconds"));
			mainPanel.add(new JLabel(difficultyIterator.next()));
			mainPanel.add(new JLabel(sizeIterator.next()));
		}
	}
    
    /**
     * This method returns the number of scores saved in the system.
     * @return int the number of scores returned
     */
	public int numberOfScores() {
		File file;
		file = new File("Scores.txt");
		Scanner scanner;
		int numberofScores = 0;
		String line = "";
		StringTokenizer scoreData;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				users.add(line);
				line = scanner.nextLine();
				scoreData = new StringTokenizer(line);
				highScores.add(scoreData.nextElement().toString());
				times.add(scoreData.nextElement().toString());
				difficulties.add(scoreData.nextElement().toString());
				sizes.add(scoreData.nextElement().toString());
				numberofScores++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return numberofScores;
	}
    
    /**
     * Sorts the scores in order from highest to lowest
     */
	public void sortScoreInfo() {
		int i = 0, j = 0;
		String temp = "";
		for(i = 0; i < users.size(); i++) {
			for(j = i; j < users.size(); j++) {
				if(Double.parseDouble(highScores.get(i)) < Double.parseDouble(highScores.get(j)) ) {
					temp = users.get(i);
					users.set(i, users.get(j));
					users.set(j, temp);
					temp = highScores.get(i);
					highScores.set(i, highScores.get(j));
					highScores.set(j, temp);
					temp = times.get(i);
					times.set(i, times.get(j));
					times.set(j, temp);
					temp = difficulties.get(i);
					difficulties.set(i, difficulties.get(j));
					difficulties.set(j, temp);
					temp = sizes.get(i);
					sizes.set(i, sizes.get(j));
					sizes.set(j, temp);
				}
			}
		}
	}
}
