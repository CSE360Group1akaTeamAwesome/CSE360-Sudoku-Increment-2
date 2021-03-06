/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/*
 *
 * AIBoard.java is a child class of SudokuBoard. It opens
 * an AI game for the user when the user chooses that option.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class AIBoard extends SudokuBoard {
	/* Private instance variables for features of the board */
	private int currentTime=0, seconds = 0, minutes = 0;
	private JTextField[][] entries,pencilEntries;
	private JTextField timeDisplay,pencilModeNotification; 
	private Timer timer;
	private User user;
	private String difficulty;
	private JPanel pencilPanel, mainBoard;
	private JPanel[] pencilRegions;
	private boolean pencilMode_ON_OFF;
	private AIPlayer AI;
	
	/**
	 * Constructor
	 * @param width sets the width of the GUI
	 * @param height sets the height of the GUI
	 * @param u  sets the User of the game
     * @param diff sets the difficulty
	 */
	public AIBoard (int width, int height, String diff, User u) {
		pencilMode_ON_OFF = false;
		user = u;
		difficulty = diff;
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		JTextPane titleMessage = new JTextPane();
		JPanel title = new JPanel();
		title.setBackground(Color.WHITE);
		title.setLayout(new FlowLayout());
		this.add(title, BorderLayout.NORTH);
		title.add(titleMessage);
		titleMessage.setEditable(false);
		StyledDocument doc2 = (StyledDocument) titleMessage.getDocument();
		SimpleAttributeSet tileFont = new SimpleAttributeSet();
    	StyleConstants.setFontFamily(tileFont, "Serif");
    	StyleConstants.setFontSize(tileFont, 12);
    	StyleConstants.setForeground(tileFont, Color.GRAY);
		SimpleAttributeSet messageFont = new SimpleAttributeSet();
    	StyleConstants.setFontFamily(messageFont, "Serif");
    	StyleConstants.setFontSize(messageFont, 32);
    	StyleConstants.setForeground(messageFont, Color.darkGray);
    	StyleConstants.setAlignment(messageFont, StyleConstants.ALIGN_CENTER);
    	try {
			doc2.insertString(doc2.getLength(), "Solve this " + difficulty + " Puzzle, " + user.getUsername(), messageFont );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
		this.add(Box.createVerticalStrut(100), BorderLayout.SOUTH);
		mainBoard = new JPanel();
		this.add(mainBoard, BorderLayout.CENTER);
		mainBoard.setLayout(new GridLayout(3,3));
		mainBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pencilPanel = new JPanel();
		pencilPanel.setLayout(new GridLayout(3,3));
		pencilPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel sideBar = new JPanel();
		sideBar.setLayout(new GridLayout(3,1));
		JButton pencilMode = new JButton("Pencil Mode");
		pencilMode.addActionListener(new ActionListener() {
            
            /**
			 * Action Event after pencilMode button is pressed
			 * It switches the mode to PencilMode
			 * @param ae begins Action Event
			 */
		    public void actionPerformed(ActionEvent ae) {
		    	switchPencilMode();
		    }
		});
		pencilModeNotification = new JTextField("  Pencil Mode: OFF");
		pencilModeNotification.setForeground(Color.RED);
		pencilModeNotification.setBackground(Color.WHITE);
		pencilModeNotification.setEditable(false);
		sideBar.add(pencilModeNotification);
		sideBar.add(pencilMode);
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
            
            /**
			 * Action Event after quit button is pressed
			 * It brings the user back to the main menu
			 * @param ae begins Action Event
			 */
		    public void actionPerformed(ActionEvent ae) {
		    	backToMenu();
		    }
		});
		sideBar.add(quit);
		this.add(sideBar,BorderLayout.EAST);
		JPanel fontColorPanel = new JPanel();
		String[] colors = {"Black","Cyan","Green","Magenta", "Orange", "Pink","Red", "Yellow" };
		final JComboBox colorList = new JComboBox(colors);
		colorList.addActionListener(new ActionListener() {
            
            /**
			 * Action event after a combobox option is selected
			 * It changes the color of the numbers being placed
			 * in the Sudoku puzzle boxes.
			 * @param ae begins Action Event
			 */
			public void actionPerformed(ActionEvent e) {
				changeFontColor((String)colorList.getSelectedItem());
			}
		});
		JTextField colorSelectPrompt = new JTextField("Choose a color");
		colorSelectPrompt.setEditable(false);
		fontColorPanel.setLayout(new BoxLayout(fontColorPanel, BoxLayout.Y_AXIS));
		fontColorPanel.add(colorSelectPrompt);
		fontColorPanel.add(colorList);
		fontColorPanel.add(Box.createRigidArea(new Dimension(0,700)));
		fontColorPanel.setBackground(Color.WHITE);
		this.add(fontColorPanel, BorderLayout.WEST);
		JPanel timerPanel = new JPanel();
		timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.X_AXIS));
		timerPanel.setBackground(Color.WHITE);
		timeDisplay = new JTextField();
		timeDisplay.setEditable(false);
		timerPanel.add(Box.createRigidArea(new Dimension(340,50)));
		timerPanel.add(timeDisplay);
		timerPanel.add(Box.createRigidArea(new Dimension(340,50)));
		
        /**
		 * Action Listener Class that implements the timer
		 * for the Sudoku puzzle.
		 *
		 * @version 0
		 * @author Garrett Gutierrez
		 */
		class CountdownTimerListener implements ActionListener {
            /**
			 * Implements a timer and updates it as the User
			 * continues to solve the puzzle.
			 * @param e begins Action Event
			 */
		      public void actionPerformed(ActionEvent e) {
		    	currentTime++;  
		        minutes = currentTime/60;
		        seconds = currentTime%60;
		        timeDisplay.setText("Current Time on Puzzle: " + String.valueOf(minutes) + " minutes and " + String.valueOf(seconds) + " seconds");
		      }
		}
		timer = new Timer(1000,new CountdownTimerListener());
		timer.start();	
		this.add(timerPanel, BorderLayout.SOUTH);
		entries = new JTextField[9][9];
		pencilEntries = new JTextField[9][9];
		JTextFieldLimit [][]doc = new JTextFieldLimit[9][9];
		JPanel []regions = new JPanel[9];
		pencilRegions = new JPanel[9];
		int i = 0, j = 0, counter = 0, k = 0, l = 0;
		/* Initialize 3x3 regions */
        for (i = 0; i < 9; i++) {
			regions[i] = new JPanel();
			regions[i].setLayout(new GridLayout(3,3));
			regions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			mainBoard.add(regions[counter]);
			pencilRegions[i] = new JPanel();
			pencilRegions[i].setLayout(new GridLayout(3,3));
			pencilRegions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			pencilPanel.add(pencilRegions[counter]);			
			counter++;
		}
		counter = 1;
		for (i = 0; i < 9; i=i+3) {
			for (j = 0; j < 9; j=j+3) {
				for (k = i; k < i + 3; k++) {
					for (l = j; l < j + 3; l++) {
						pencilEntries[k][l] = new JTextField();
						pencilEntries[k][l].setHorizontalAlignment(JTextField.CENTER);
						pencilRegions[counter-1].add(pencilEntries[k][l]);
						entries[k][l] = new JTextField(String.valueOf(counter));
						entries[k][l].setHorizontalAlignment(JTextField.CENTER);
						entries[k][l].addKeyListener(new MoveListener());
						doc[k][l] = new JTextFieldLimit(1);
						try {
							doc[k][l].insertString(doc[k][l].getLength(), String.valueOf(counter), tileFont);
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						entries[k][l].setDocument(doc[k][l]);
						regions[counter-1].add(entries[k][l]);
					}
				}
				counter++;
			}
		}
		loadPuzzle(difficulty);
		AI = new AIPlayer(difficulty, "9x9",entries);
	}
	
    /**
     * Key Listener Class that implements the moves the user makes
     * for the Sudoku puzzle.
     *
     * @version 0
     * @author Garrett Gutierrez
     */
	public class MoveListener implements KeyListener {
		private char input;
        
        /**
         * Implements a listener and updates it as the User
         * continues to solve the puzzle and press and release keys
         * as type keys as well.
         * @param a begins Key Event
         */
		@Override
		public void keyPressed (KeyEvent a) {
		}

		@Override
		public void keyReleased (KeyEvent a) {
			input = a.getKeyChar();
			if (!(a.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
				if (checkInput (input)) {
					int reply = JOptionPane.showConfirmDialog(null, "Confirm input of " + input + " ?");
					if (reply == JOptionPane.YES_OPTION) {
						if ((AI.determineIfFinished()) == false) {
				    		switchTurns();
				    		if ((AI.determineIfFinished()) == true) {
				    			timer.stop();
					    		computeStats();	
					    		reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
					    		if (reply == JOptionPane.YES_OPTION) {
						    		MainMenu menu = new MainMenu(1000,800, user);
									menu.setSize(1000,800);
									menu.setVisible(true);
									menu.setTitle("CSE360 Sudoku Main Menu");
								}
					    		dispose();
					    	}
				    	} else {
				    		timer.stop();
				    		computeStats();	
				    		reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
				    		if (reply == JOptionPane.YES_OPTION) {
					    		MainMenu menu = new MainMenu(1000,800, user);
								menu.setSize(1000,800);
								menu.setVisible(true);
								menu.setTitle("CSE360 Sudoku Main Menu");
							}
				    		dispose();
				    	}
					} else {
						JTextField temp = (JTextField) a.getSource();
						temp.setText("");
					}
				} else {
					JTextField temp = (JTextField) a.getSource();
					temp.setText("");
					JOptionPane.showMessageDialog(null,"Please enter a digit from 1 to 9", "Invalid Move", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
        
		@Override
		public void keyTyped (KeyEvent a) {
		}
        
        /**
         * Used to debug and determine whether user inputted
         * valid numbers for each row of the puzzle.
         * @param input used for debugging purposes
         * @return boolean to determine whether inputted
         * numbers are valid
         */
		public boolean checkInput (char input) {
			if ((input == '1')||(input == '2')||(input == '3')||(input == '4')||(input == '5')||(input == '6')
					||(input == '7')||(input == '8')||(input == '9')) {
				return true;		
			} else {
				return false;
			}
		}
	}
    
    /**
	 * Used to debug and determine whether user inputted
	 * correct numbers for each row of the puzzle.
	 * @param col used for debugging purposes
	 * @return boolean to determine whether inputted numbers
	 * are correct.
	 */
	public boolean checkRow (int row) {
		int i = 0, j = 0, count = 0;
        /* Uses nested for loops to check the entries in the row */
		for(i = 1; i < 10; i++) {
			count = 0;
			for (j = 0; j < 9; j++) {
				if (i == Integer.parseInt(entries[row][j].getText()) ){
					count++;
				}
				if ( count >= 2) {
					System.out.println("checkRow() returned false at i: " + row + " j: "+j + " with " + entries[row][j].getText()); 
					return false;
				}
			}
		}
		return true;
	}
    
    /**
	 * Used to debug and determine whether user inputted
	 * correct numbers for each column of the puzzle.
	 * @param col used for debugging purposes
	 * @return boolean to determine whether inputted numbers
	 * are correct.
	 */
	public boolean checkColumn (int col) {
		int i = 0, j = 0, count = 0;
        /* Uses nested for loops to check the entries in the column */
		for (j = 0; j < 10; j++) {
			count = 0;
			for (i = 0; i < 9; i++) {
				if (j == Integer.parseInt(entries[i][col].getText())) {
					count++;
				}
				if (count >= 2) {
					System.out.println("checkColumn() returned false at i: " + i + " j: "+ col + " with " + entries[i][col].getText()); 
					return false;
				}
			}
		}
		return true;
	}
    
    /**
	 * Used to debug and determine whether user inputted
	 * correct numbers into each puzzle box
	 * @param row used for debugging purposes
	 * @param col used for debugging purposes
	 * @return boolean to determine if inputted numbers
	 * are correct.
	 */
	public boolean checkBox (int row, int col) {
		int i = 0, j = 0, k = 0, count = 0;
        /* Uses nested for loops to check the entries */
		for (k = 1; k < 10; k++) {
			for (i = row; i < row+3; i++) {
				for (j = col; j < col+3; j++) {
					count = 0;
						if (k == Integer.parseInt(entries[i][j].getText())) {
							count++;
						}
						if (count >= 2) {
							System.out.println("checkBox() returned false at i: " + i + " j: "+ j + " with " + entries[i][j].getText()); 
							return false;
						}
				}
			}
		}
		return true;
	}
	
    /**
	 * Used to determine if the user did not input a number
	 * into a box
	 * @return boolean to determine whether the whole puzzle
	 * is full.
	 */
	public boolean isEmptySpace () {
		int i = 0, j = 0;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				if (entries[i][j].getText().equals("")) {
					System.out.println("isEmptySpace() returned false at i: " + i + " j: "+j); 
					return false;
				}
			}
		}
		return true;
	}
    
    /**
	 * Makes sure that the user inputs only correct input (i.e
	 * numbers 1-9 and letters A-G).
	 * @return boolean to determine whether the user inputted valid
	 * values.
	 */
	public boolean checkPuzzle () {
		int i = 0;
        /* checks for empty space */
		if (!isEmptySpace()) {
			return false;
		}
        /* checks for valid input */
		if (!validateInput()) {
			return false;
		}
		for (i = 0; i < 9; i++) {
			if (checkRow(i) == false || checkColumn(i) == false) {
				return false;
			}
		}
		if (!checkBox(0,0)||!checkBox(0,3)||!checkBox(0,6)||!checkBox(3,0)||!checkBox(3,3)||!checkBox(3,6)||!checkBox(6,0)||!checkBox(6,3)||!checkBox(6,6)) {
			return false;
		}
		return true;
	}
	
    /**
	 * Makes sure that the user inputs only correct input (i.e
	 * numbers 1-9 and letters A-G).
	 * @return boolean to determine whether the user inputted valid
	 * values.
	 */
	public boolean validateInput () {
		int i = 0, j = 0;
        /* Uses nested for loops to check that all input is valid */
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				    try {
				        if (Integer.parseInt(entries[i][j].getText()) < 1) {
				        	return false;
				        }
				    } catch (NumberFormatException e) {
				        JOptionPane.showMessageDialog(null, "Invalid input. Enter an integer at row " + (i+1) + " column " + (j+1), "Error", JOptionPane.ERROR_MESSAGE);
				        return false;
				    }
			}
		}
		return true;
	}
	
	/**
	 * Loads a new puzzle for the User 
	 */
	public void loadPuzzle (String difficulty) {
		File file;
        /* Uses if statements to check the difficulty level passed */
		if (difficulty.equals("Easy")) {
			file = new File("easy9x9.txt");
		} else if (difficulty.equals("Medium")) {
			file = new File("medium9x9.txt");
		} else if (difficulty.equals("Hard")) {
			file = new File("hard9x9.txt");
		} else {
			file = new File("evil9x9.txt");
		}
		int i = 0, j = 0, value = 0;
		Scanner scanner;
        /* this uses a try to make sure that it handles exceptions */
		try {
			scanner = new Scanner(file);
			for (i = 0; i < 9; i++) {
				for (j = 0; j < 9; j++) {
					if (scanner.hasNextInt()) {
						value = scanner.nextInt();
						if (value != 0) {
							try {
								entries[i][j].setText(String.valueOf(value));
								entries[i][j].setForeground(Color.BLUE);
								entries[i][j].setEditable(false);
								
								pencilEntries[i][j].setText(String.valueOf(value));
								pencilEntries[i][j].setForeground(Color.BLUE);
								pencilEntries[i][j].setEditable(false);
							} catch (Exception e) {
								System.out.println("It broke at i: " + i + " j: " + j + " with " + value);	
							}
						} else {
							entries[i][j].setText("");
							pencilEntries[i][j].setText("");
						}
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load puzzle. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
    
    /**
	 * Computes the user's score and saves it.
	 */
	public void computeStats ()
	{
		user.getScore().setCurrentTime(currentTime);
		user.getScore().setLastDifficulty(difficulty);
		user.getScore().setLastSize("9x9");
		user.getScore().calculateScore();
		user.getScore().displayLatestStats();
		user.setSavedGameSize("9x9");
		saveScoreStats();
		
	}
    
	/**
	 * Saves the user's score to their account
	 */
	public void saveScoreStats () {
		File file = new File("Scores.txt");	
		boolean flag = false;
		FileWriter writer;
		ArrayList<String> scoreData = new ArrayList<String>();
		ListIterator<String> iterator;
        /* Uses a try statement to catch any exceptions */
		try {
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				scoreData.add(s.nextLine());
			}
			s.close();
			
			iterator = scoreData.listIterator();
			while (iterator.hasNext()) {
				if (iterator.next().equals(user.getUsername())){
					iterator.next();
					iterator.set(String.valueOf(user.getScore().getHighScore())+ " " + String.valueOf(user.getScore().getBestTime()) + " " + user.getScore().getBestDifficulty() + " " + user.getScore().getBestSize()+ " "
							+ user.getScore().getCurrentScore()	+ " " + String.valueOf(user.getScore().getCurrentTime())
							+ " " + user.getScore().getLastDifficulty() + " " + user.getScore().getLastSize());
					flag = true;
					break;
				}
			}
			if (flag == false) {
				scoreData.add(user.getUsername());
				scoreData.add(String.valueOf(user.getScore().getHighScore())+ " " + String.valueOf(user.getScore().getBestTime()) + " " + user.getScore().getBestDifficulty() + " " + user.getScore().getBestSize());
			}
			writer = new FileWriter("Scores.txt");
			iterator = scoreData.listIterator();
			while (iterator.hasNext()) {
				writer.write(iterator.next());
				writer.write("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not find Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not update Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
    /**
	 * Returns the user to the main menu
	 */
	public void backToMenu () {
        /* uses if and else statements to check what the user would like to do */
		int reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
		if(reply == JOptionPane.YES_OPTION) {
			timer.stop();
			reply = JOptionPane.showConfirmDialog(null, "Would you like to see the solution?");
			if(reply == JOptionPane.YES_OPTION) {
				MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");
				ShowSolution solution;
				switch(difficulty) {
                /* Uses case statements to load the solution */
				case "Easy":
					solution = new ShowSolution("easy9x9Solution.txt","9x9");
					solution.setTitle("Easy 9x9 Solution");
					break;
                        
				case "Medium":
					solution = new ShowSolution("medium9x9Solution.txt", "9x9");
					solution.setTitle("Medium 9x9 Solution");
					break;
                        
				case "Hard":
					solution = new ShowSolution("hard9x9Solution.txt", "9x9");
					solution.setTitle("Hard 9x9 Solution");
					break;
                        
				default:
					solution = new ShowSolution("evil9x9Solution.txt", "9x9");
					solution.setTitle("Evil 9x9 Solution");
					break;
				}
				solution.setSize(500,500);
				solution.setVisible(true);
				solution.setResizable(false);
				dispose();
			} else {
				MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");
				dispose();					
			}
		}		
	}
    
    /**
	 * Changes the font color of the numbers being put into
	 * the box.
	 * @param color changes the font color to a new one
	 */
	public void changeFontColor (String color) {
		int i = 0, j = 0;
		Color fontColor;
        /* uses if and else if statements to set the color */
		if (color.equals("Black")) {
			fontColor = Color.BLACK;
		} else if (color.equals("Cyan")) {
			fontColor = Color.CYAN;
		} else if (color.equals("Green")) {
			fontColor = Color.GREEN;
		} else if (color.equals("Magenta")) {
			fontColor = Color.MAGENTA;
		} else if (color.equals("Orange")) {
			fontColor = Color.ORANGE;
		} else if (color.equals("Pink")) {
			fontColor = Color.PINK;
		} else if (color.equals("Red")) {
			fontColor = Color.RED;
		} else {
			fontColor = Color.YELLOW;
		}
        /* uses nested for loops and if statements to actually set the color */
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				if (entries[i][j].isEditable() && entries[i][j].getText().equals("")) {
					entries[i][j].setForeground(fontColor);
				}
				if (pencilEntries[i][j].isEditable() && pencilEntries[i][j].getText().equals("")) {
					pencilEntries[i][j].setForeground(fontColor);
				}
			}
		}
	}
	
    /**
	 * Changes the mode of the game to Pencil Mode
	 */
	public void switchPencilMode () {
		/* Turn On Pencil Mode */
		if (pencilMode_ON_OFF == false) {
    		pencilMode_ON_OFF = true;
    		copyToPencilMode();
    		pencilModeNotification.setText("  Pencil Mode: ON");
    		this.remove(mainBoard);
    		this.add(pencilPanel, BorderLayout.CENTER);
    		this.revalidate();
    		this.repaint();
    	} else { /* Turn Off Pencil Mode */
			pencilMode_ON_OFF = false;
			pencilModeNotification.setText("  Pencil Mode: OFF");
    		this.remove(pencilPanel);
    		this.add(mainBoard, BorderLayout.CENTER);
    		this.revalidate();
    		this.repaint();
		}
	}
    
    /**
	 * Copies the entries made by the user in pencil mode so that
	 * when they return to pencil mode the entries will still be
	 * there.
	 */
	public void copyToPencilMode () {
		int i = 0, j = 0;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				if ((pencilEntries[i][j].isEditable()) && (pencilEntries[i][j].getText().equals(""))) {
					pencilEntries[i][j].setText(entries[i][j].getText());
				}
			}
		}
	}
    
    /**
     * Changes turns between the user and the AI
     */
	public void switchTurns () {
		if (!AI.makeMove()) {
			JOptionPane.showMessageDialog(null, "Error with AI. Could not make move.", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null,"AI has finished move.", "Player's Turn",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}