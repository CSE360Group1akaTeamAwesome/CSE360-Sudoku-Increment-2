/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Random;

/*
 *
 * AIPlayer.java is a class that creates an AI player and gives 
 * it functionality to enter numbers.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class AIPlayer {
	private JTextField[][] entries;
	private String[][] solution;
	private String size, difficulty;
    
	/**
	 * Constructor
	 * @param diff  sets the difficulty of the game
     * @param bigness  sets the size of the game
     * @param tiles  sets the entries of the game
	 */
	public AIPlayer (String diff, String bigness, JTextField[][] tiles) {
		entries = tiles;
		difficulty = diff;
		size = bigness;
		if (size.equals("9x9")) {
			solution = new String[9][9];
		} else {
			solution = new String[16][16];
        }
		setSolution();
	}
    
    /**
     * This method has the AI player make a move 
     */
	public boolean makeMove () {
        /* Uses nested else and if statements to either fix a mistake or fill in an empty space */
		if (findMistake() == false) {
			if (addNumberToBoard()) {
				return true;
			} else {
				if (updateEmptySpace()) {
					return true;
				}
				return false;
			}
		} else {
			return true;
        }
	}
    
    /**
     * This method finds a mistake on the board
     * @return boolean is true when there is a mistake and false when there isn't
     */
	public boolean findMistake () {
		int i = 0, j = 0;
        /* Checks the size */
		if (size.equals("9x9")) {
            /* Uses nested for loops and if statements to check entered numbers */
			for (i = 0; i < 9; i++) {
				for (j = 0; j < 9; j++) {
					if ( !(entries[i][j].getText().equals("")) && (entries[i][j].isEditable()) ) {
						if (!entries[i][j].getText().equals(solution[i][j])) {
							entries[i][j].setText(solution[i][j]);
							entries[i][j].setForeground(Color.WHITE);
							entries[i][j].setEditable(false);
							entries[i][j].setBackground(Color.DARK_GRAY);
							return true;
						}
					}
				}
			}
		} else { /* Same as above for 16x16 */
			for (i = 0; i < 16; i++) {
				for (j = 0; j < 16; j++) {
					if ( !(entries[i][j].getText().equals("")) && (entries[i][j].isEditable()) ) {
						if (!entries[i][j].getText().equals(solution[i][j])){
							entries[i][j].setText(solution[i][j]);
							entries[i][j].setForeground(Color.WHITE);
							entries[i][j].setEditable(false);
							entries[i][j].setBackground(Color.DARK_GRAY);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
    
    /**
     * This method adds a random number to the board when there 
     * are no errors to fix 
     */
	public boolean addNumberToBoard () {
		int row = 0, col = 0, counter = 0;
		Random randomGenerator = new Random();
        /* Checks board size */
		if (size.equals("9x9")) {
            /* uses a while loop to generate a random number and then an if statement to place a solution */
			while (counter < 200) {
				row = randomGenerator.nextInt(9);
				col = randomGenerator.nextInt(9);
				if ( (entries[row][col].getText().equals("")) && (entries[row][col].isEditable())) {
					entries[row][col].setText(solution[row][col]);
					entries[row][col].setForeground(Color.WHITE);
					entries[row][col].setEditable(false);
					entries[row][col].setBackground(Color.DARK_GRAY);
					return true;
				}
				counter++;
			}
		} else { /* Same as above for 16x16 board */
			while (counter < 200) {
				row = randomGenerator.nextInt(16);
				col = randomGenerator.nextInt(16);
				if ((entries[row][col].getText().equals("")) && (entries[row][col].isEditable())) {
					entries[row][col].setText(solution[row][col]);
					entries[row][col].setForeground(Color.WHITE);
					entries[row][col].setEditable(false);
					entries[row][col].setBackground(Color.DARK_GRAY);
					return true;
				}
				counter++;
			}
		}
		return false;
	}
    
    /**
     * This method checks if a space is empty 
     */
	public boolean updateEmptySpace () {
		int i = 0, j = 0;
        /* Checks the size */
		if (size.equals("9x9")){
            /* Uses nested for loops to check if a space is empty */
			for (i = 0; i < 9; i++) {
				for (j = 0; j < 9; j++) {
					if (entries[i][j].getText().equals("")) {
						entries[i][j].setText(solution[i][j]);
					}
				}
			}
		} else { /* Same as above for 16x16 board */
			for (i = 0; i < 16; i++) {
				for (j = 0; j < 16; j++){
					if (entries[i][j].getText().equals("")) {
						entries[i][j].setText(solution[i][j]);
					}
				}
			}
		}
		return false;
	}
    
    /**
     * This method sets the solution to the puzzle 
     */
	public void setSolution () {
		File file = null;
		int i = 0, j = 0, v = 0;
		String value ="";
		Scanner scanner;
        /* Checks the size */
		if (size.equals("9x9")) {
            /* Uses a switch statement to find the appropriate solution */
			switch (difficulty){
			case "Easy":
				file = new File("easy9x9Solution.txt");
				break;
                    
			case "Medium":
				file = new File("medium9x9Solution.txt");
				break;
                    
			case "Hard":
				file = new File("hard9x9Solution.txt");
				break;
                    
			default:
				file = new File("evil9x9Solution.txt");
				break;
			}
		} else { /* Same as above for 16x16 board */
			switch (difficulty) {
			case "Easy":
				file = new File("easy16x16Solution.txt");
				break;
                    
			case "Medium":
				file = new File("medium16x16Solution.txt");
				break;
                    
			case "Hard":
				file = new File("hard16x16Solution.txt");
				break;
                    
			default:
				file = new File("evil16x16Solution.txt");
				break;
			}
		}
        /* Uses a try statement to catch any exceptions */
		try {
			scanner = new Scanner(file);
            /* Uses nested if and for loops to read in the solution */
			if (size.equals("9x9")) {
				for (i = 0; i < 9; i++) {
					for (j = 0; j < 9; j++) {
						if (scanner.hasNextInt()) {
							v= scanner.nextInt();
                            /* nested try statement to catch more exceptions */
							try {
								solution[i][j]=(String.valueOf(v));
							} catch (Exception e) {
								System.out.println("It broke at i: " + i + " j: " + j + " with " + String.valueOf(v));	
							}
						}
					}
				}
            } else { /* Same as above for 16x16 board */
				for (i = 0; i < 16; i++) {
					for (j = 0; j < 16; j++) {
						if (scanner.hasNext()) {
							value = scanner.next();						
							try {
								solution[i][j] = value;
							}
							catch (Exception e) {
								System.out.println("It broke at i: " + i + " j: " + j + " with " + value);	
							}
						}
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load solution. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
    
    /**
     * This method determines when the game is finished 
     */
	public boolean determineIfFinished () {
		int i = 0, j = 0;
        /* Checks the size */
		if (size.equals("9x9")) {
            /* Uses nested for loops and if statements to check if the game is finished based on all places being full or more errors remaining */
			for (i = 0; i < 9; i++) {
				for (j = 0; j < 9; j++) {
					System.out.println(entries[i][j].getText());
					if ((entries[i][j].getText().equals(solution[i][j])) == false) {
						return false;
					}
				}
			}
		} else { /* Same as above for 16x16 board */
			for (i = 0; i < 16; i++) {
				for (j = 0; j < 16; j++) {
					if ((entries[i][j].getText().equals(solution[i][j])) == false) {
						return false;
					}
				}
			}
		}
		return true;	
	}
}
