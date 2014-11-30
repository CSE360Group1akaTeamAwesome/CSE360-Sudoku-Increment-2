/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 *
 * ShowSolution.java reveals the solution of the puzzle
 * to the user when they ask for the solution.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class ShowSolution extends JFrame {
	private JPanel mainPanel;
	private JPanel[] regions;
	private JTextField[][] entries;

	/**
	 * Constructor
	 * @param gameName sets the name of the puzzle
	 * @param size sets the size of the puzzle
	 */
	public ShowSolution(String gameName, String size) {
		int counter = 0;
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3, 3));
		this.add(mainPanel);

		if(size.equals("9x9")) {
			mainPanel.setLayout(new GridLayout(3, 3));
			entries = new JTextField[9][9];
			regions = new JPanel[9];
			for(int i = 0; i < 9; i++) {
				regions[i] = new JPanel();
				regions[i].setLayout(new GridLayout(3, 3));
				regions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			}
			//Initialize mainBoard
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					mainPanel.add(regions[counter]);
					counter++;
				}
			}
			counter = 1;
			for(int i = 0; i < 9; i = i + 3) {
				for(int j = 0; j < 9; j = j  +3) {
					for(int k = i; k < i + 3; k++) {
						for(int l = j; l < j + 3; l++) {
							entries[k][l] = new JTextField(String.valueOf(counter));
							entries[k][l].setEditable(false);
							entries[k][l].setHorizontalAlignment(JTextField.CENTER);
							regions[counter-1].add(entries[k][l]);
						}
					}
					counter++;
				}
			}
		}
		else {
			mainPanel.setLayout(new GridLayout(4, 4));
			entries = new JTextField[16][16];
			regions = new JPanel[16];
			for(int i = 0; i < 16; i++) {
				regions[i] = new JPanel();
				regions[i].setLayout(new GridLayout(4, 4));
				regions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			}
			//Initialize mainBoard
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					mainPanel.add(regions[counter]);
					counter++;
				}
			}
			counter = 1;
			for(int i = 0; i < 16; i = i + 4) {
				for(int j = 0; j < 16; j = j + 4) {
					for(int k = i; k < i + 4; k++) {
						for(int l = j; l < j + 4; l++) {
							entries[k][l] = new JTextField(String.valueOf(counter));
							entries[k][l].setEditable(false);
							entries[k][l].setHorizontalAlignment(JTextField.CENTER);
							regions[counter - 1].add(entries[k][l]);
						}
					}
					counter++;
				}
			}
		}
		loadSolution(gameName, size);
	}

	/**
	 * Loads the solution of the puzzle for the user to see.
	 * @param nameOfFile retrieves the name of the solution file
	 * @param size loads the correct solution puzzle
	 */
	void loadSolution(String nameOfFile, String size) {
		File file = new File(nameOfFile);
		int v = 0;
		String value ="";
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			if(size.equals("9x9")) {
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 9; j++) {
						if (scanner.hasNextInt()) {
							v= scanner.nextInt();
							try {
								entries[i][j].setText(String.valueOf(v));
							}
							catch(Exception e) {
								System.out.println("It broke at i: " + i + " j: " + j + " with " + String.valueOf(v));
							}
						}
					}
				}
			}
			else {
				for(int i = 0; i < 16; i++) {
					for(int j = 0; j < 16; j++) {
						if (scanner.hasNext()) {
							value = scanner.next();
							try {
								entries[i][j].setText(value);
							}
							catch(Exception e) {
								System.out.println("It broke at i: " + i + " j: " + j + " with " + value);
							}
						}
					}
				}
			}
			scanner.close();
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load solution. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
