/*
 * AIBoard16x16
 *
 * Version 1:
 * Copyright Info:
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

public class AIBoard16x16 extends SudokuBoard16x16
{
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
	
	/* Constructor for the new Board */
	public AIBoard16x16 (int width, int height, String diff, User u)
	{
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
		mainBoard.setLayout(new GridLayout(4,4));
		mainBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		pencilPanel = new JPanel();
		pencilPanel.setLayout(new GridLayout(4,4));
		pencilPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
		JPanel sideBar = new JPanel();
		sideBar.setLayout(new GridLayout(3,1));
		JButton pencilMode = new JButton("Pencil Mode");
		pencilMode.addActionListener(new ActionListener() 
	    {
		    public void actionPerformed(ActionEvent ae)
		    {
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
		quit.addActionListener(new ActionListener() 
	    {
		    public void actionPerformed(ActionEvent ae)
		    {
		    	backToMenu();
		    }
		});
		sideBar.add(quit);
		this.add(sideBar,BorderLayout.EAST);
		
		JPanel fontColorPanel = new JPanel();
		String[] colors = {"Black","Cyan","Green","Magenta", "Orange", "Pink","Red", "Yellow" };
		final JComboBox colorList = new JComboBox(colors);
		colorList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
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
		
		class CountdownTimerListener implements ActionListener
		{
		      public void actionPerformed(ActionEvent e) 
		      {
		    	currentTime++;  
		        minutes = currentTime/60;
		        seconds = currentTime%60;
		        timeDisplay.setText("Current Time on Puzzle: " + String.valueOf(minutes) + " minutes and " + String.valueOf(seconds) + " seconds");
	
		      }
		}
		timer = new Timer(1000,new CountdownTimerListener());
		timer.start();	
		this.add(timerPanel, BorderLayout.SOUTH);
		
		
		entries = new JTextField[16][16];
		pencilEntries = new JTextField[16][16];
		JTextFieldLimit [][] doc = new JTextFieldLimit[16][16];
		JPanel [] regions = new JPanel[16];
		pencilRegions = new JPanel[16];
		
		int i = 0, j = 0, k = 0, l = 0, counter = 0;
		/* Initialize 4x4 regions on mainboard */
		for (i = 0; i < 16; i++)
		{
			regions[i] = new JPanel();
			regions[i].setLayout(new GridLayout(4,4));
			regions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			mainBoard.add(regions[counter]);
			
			pencilRegions[i] = new JPanel();
			pencilRegions[i].setLayout(new GridLayout(4,4));
			pencilRegions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			pencilPanel.add(pencilRegions[counter]);
			counter++;
		}
		counter = 1;
		for (i = 0; i < 16; i=i+4)
		{
			for (j = 0; j < 16; j=j+4)
			{
				for (k = i; k < i + 4; k++)
				{
					for (l = j; l < j + 4; l++)
					{
						pencilEntries[k][l] = new JTextField();
						pencilEntries[k][l].setHorizontalAlignment(JTextField.CENTER);
						pencilRegions[counter-1].add(pencilEntries[k][l]);
						
						entries[k][l] = new JTextField(String.valueOf(counter%16));
						entries[k][l].setHorizontalAlignment(JTextField.CENTER);
						entries[k][l].addKeyListener(new MoveListener());
						doc[k][l] = new JTextFieldLimit(1);
						try {
							doc[k][l].insertString(doc[k][l].getLength(), String.valueOf(counter%9), tileFont);
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
		AI = new AIPlayer(difficulty,"16x16", entries);
	}
	
    /* This class listens to moves that the user makes */
	public class MoveListener implements KeyListener
	{
		private char input;
		@Override
		public void keyPressed (KeyEvent a) {
		}

		@Override
		public void keyReleased (KeyEvent a) {
			input = a.getKeyChar();
			if (!(a.getKeyCode() == KeyEvent.VK_BACK_SPACE))
			{
				if(checkInput(input))
				{
					int reply = JOptionPane.showConfirmDialog(null, "Confirm input of " + input + " ?");
					if (reply == JOptionPane.YES_OPTION)
					{
						if ((AI.determineIfFinished()) == false)
				    	{
				    		switchTurns();
				    		if ((AI.determineIfFinished()) == true)
					    	{
				    			timer.stop();
					    		computeStats();	
					    		reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
					    		if (reply == JOptionPane.YES_OPTION)
					    		{
						    		MainMenu menu = new MainMenu(1000,800, user);
									menu.setSize(1000,800);
									menu.setVisible(true);
									menu.setTitle("CSE360 Sudoku Main Menu");
									
								}
					    		dispose();
					    	}	    		
					    	
				    	}
				    	else
				    	{
				    		timer.stop();
				    		computeStats();	
				    		reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
				    		if (reply == JOptionPane.YES_OPTION)
				    		{
					    		MainMenu menu = new MainMenu(1000,800, user);
								menu.setSize(1000,800);
								menu.setVisible(true);
								menu.setTitle("CSE360 Sudoku Main Menu");
								
							}
				    		dispose();
				    	}
					}
					else
					{
						JTextField temp = (JTextField) a.getSource();
						temp.setText("");
					}
				}
				else
				{
					JTextField temp = (JTextField) a.getSource();
					temp.setText("");
					JOptionPane.showMessageDialog(null,"Please enter a digit from 1 to 9 or a character from A to G. ", "Invalid Move", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		@Override
		public void keyTyped (KeyEvent a) {
			
		}
        
		public boolean checkInput (char input){
            if((input == '1') || (input == '2')|| (input == '3')|| (input == '4')|| (input == '5')|| (input == '6')|| (input == '7')
					 || (input == '8')|| (input == '9')|| (input == 'a')|| (input == 'A')|| (input == 'b')|| (input == 'B')|| (input == 'c')
					 || (input == 'C')|| (input == 'd')|| (input == 'D')|| (input == 'e')|| (input == 'E')|| (input == 'f')|| (input == 'F')
					 || (input == 'e')|| (input == 'E')|| (input == 'f')|| (input == 'F')|| (input == 'g')|| (input == 'G'))
		        {
				return true;		
			}
			else
			{
				return false;
			}
				
		}
	}
	
    /* This method checks the row */
	public boolean checkRow (int row)
	{
		int i = 0, j = 0, count = 0;
        /* Uses nested for loops to check the entries in the row */
		for (i = 1; i < 10; i++)
		{	
			count = 0;
			for (j = 0; j < 16; j++)
			{
				if (i == Integer.parseInt(entries[row][j].getText()) )
				{
					count++;
				}
                
				if ( count >= 2)
				{
					System.out.println("checkRow() returned false at i: " + row + " j: "+j + " with " + entries[row][j].getText()); 
					return false;
				}
				
			}
		}
		
		return true;
	}
    
    /* This method checks the column */
	public boolean checkColumn (int col)
	{
		int i = 0, j = 0, count = 0;
        /* Uses nested for loops to check the entries in the column */
		for (j = 0; j < 10; j++)
		{
			count = 0;
			for (i = 0; i < 16; i++)
			{
				if (j == Integer.parseInt(entries[i][col].getText()) )
				{
					count++;
				}
                
				if (count >= 2)
				{
					System.out.println("checkColumn() returned false at i: " + i + " j: "+ col + " with " + entries[i][col].getText()); 
					return false;
				}
				
			}
		}
		
		return true;
	}
    
    /* This method checks a box on the board */
	public boolean checkBox (int row, int col)
	{
		int i = 0, j = 0, k = 0, count = 0;
        /* Uses nested for loops to check the entries */
		for (k = 1; k < 10; k++)
		{
			for (i = row; i < row+4; i++)
			{
				for (j = col; j < col+4; j++)
				{	
					count = 0;
					
						if (k == Integer.parseInt(entries[i][j].getText()) )
						{
							count++;
						}
                    
						if (count >= 2)
						{
							System.out.println("checkBox() returned false at i: " + i + " j: "+ j + " with " + entries[i][j].getText()); 
							return false;
						}
					
				}
			}
		}
		
		return true;
	}
	
    /* checks if a space is empty */
	public boolean isEmptySpace ()
	{
		int i = 0, j = 0;
		for (i = 0; i < 16; i++)
		{
			for (j = 0; j < 16; j++)
			{	
				if (entries[i][j].getText().equals(""))
				{
					System.out.println("isEmptySpace() returned false at i: " + i + " j: "+j); 
					return false;
				}
			}
		}
		return true;
	}
    
    /* This method checks the puzzle */
	public boolean checkPuzzle ()
	{
		int i = 0;
        /* checks for empty space */
		if (!isEmptySpace())
		{
			return false;
		}
        
        /* checks for valid input */
		if (!validateInput())
		{
			return false;
		}
        
		for (i = 0; i < 16; i++)
		{
			if (checkRow(i) == false || checkColumn(i) == false)
			{
				return false;
			}
		}
		
		if (!checkBox(0,0)||!checkBox(0,4)||!checkBox(0,8)||!checkBox(0,12)||!checkBox(4,0)||!checkBox(4,4)||!checkBox(4,8)||!checkBox(4,12)||!checkBox(8,0)||!checkBox(8,4)||!checkBox(8,8)||!checkBox(8,12)||!checkBox(12,0)||!checkBox(12,4)||!checkBox(12,8)||!checkBox(12,12))
			return false;
		
		return true;
	}
	
    /* This method validates user input */
	public boolean validateInput ()
	{
		int i = 0, j = 0;
        /* Uses nested for loops to check that all input is valid */
		for (i = 0; i < 16; i++)
		{
			for (j = 0; j < 16; j++)
			{
                /* Uses a try/ catch to validate since there are both letters and numbers used as entries in this board size */
				    try 
				    {
				    	
				        if (!( entries[i][j].getText().equals("1")||entries[i][j].getText().equals("2")||entries[i][j].getText().equals("3")||entries[i][j].getText().equals("4")
				        		||entries[i][j].getText().equals("5")||entries[i][j].getText().equals("6")||entries[i][j].getText().equals("7")||entries[i][j].getText().equals("8")
				        		||entries[i][j].getText().equals("9")||entries[i][j].getText().toUpperCase().equals("A")||entries[i][j].getText().toUpperCase().equals("B")
				        		||entries[i][j].getText().toUpperCase().equals("C")||entries[i][j].getText().toUpperCase().equals("D")||entries[i][j].getText().toUpperCase().equals("E")
				        		||entries[i][j].getText().toUpperCase().equals("F")||entries[i][j].getText().toUpperCase().equals("G") ))
				        {
				        	JOptionPane.showMessageDialog(null, "Invalid input. Enter an integer at row " + (i+1) + " column " + (j+1), "Error", JOptionPane.ERROR_MESSAGE);
				        	return false;
				        }
				    }
				    catch (NumberFormatException e) 
				    {
				        JOptionPane.showMessageDialog(null, "Invalid input. Enter an integer at row " + (i+1) + " column " + (j+1), "Error", JOptionPane.ERROR_MESSAGE);
				        return false;
				    }
			}
		}
		return true;
	}
	
    /* This method loads a new puzzle */
	public void loadPuzzle (String difficulty)
	{
		File file;
        /* Uses if statements to check the difficulty level passed */
		if (difficulty.equals("Easy"))
		{
			file = new File("easy16x16.txt");
		}
		else if (difficulty.equals("Medium"))
		{
			file = new File("medium16x16.txt");
		}
		else if (difficulty.equals("Hard"))
		{
			file = new File("hard16x16.txt");
		}
		else
		{
			file = new File("evil16x16.txt");
		}
		int i = 0, j = 0;
		String value = "";
		Scanner scanner;
        /* this uses a try to make sure that it handles exceptions */
		try 
		{
			scanner = new Scanner(file);
			
			for (i = 0; i < 16; i++)
			{
				for (j = 0; j < 16; j++)
				{
					if (scanner.hasNext())
					{
						value = scanner.next();
						if (!value.equals("0"))
						{
							try
							{
								entries[i][j].setText(value);
								entries[i][j].setForeground(Color.BLUE);
								entries[i][j].setEditable(false);
								
								pencilEntries[i][j].setText(String.valueOf(value));
								pencilEntries[i][j].setForeground(Color.BLUE);
								pencilEntries[i][j].setEditable(false);
							}
							catch (Exception e)
							{
								System.out.println("It broke at i: " + i + " j: " + j + " with " + value);	
							}
						}
						else
						{
							entries[i][j].setText("");
							pencilEntries[i][j].setText("");
						}
    
					}
				}
			}
			scanner.close();
		}
        /* Catches any potential exceptions and throws an error message */
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not load puzzle. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
    
    /* This method computes a users stats */
	public void computeStats()
	{
		user.getScore().setCurrentTime(currentTime);
		user.getScore().setLastDifficulty(difficulty);
		user.getScore().setLastSize("16x16");
		user.getScore().calculateScore();
		user.getScore().displayLatestStats();
		user.setSavedGameSize("16x16");
		saveScoreStats();
		
	}
	
    /* This method gives user's the ability to save their stats */
	public void saveScoreStats ()
	{
		File file = new File("Scores.txt");	
		boolean flag = false;
		FileWriter writer;
		ArrayList<String> scoreData = new ArrayList<String>();
		ListIterator<String> iterator;
		
        /* Uses a try statement to catch any exceptions */
		try
		{
			Scanner s = new Scanner(file);
			while (s.hasNextLine())
			{
				scoreData.add(s.nextLine());
			}
			s.close();
			
			iterator = scoreData.listIterator();
			while (iterator.hasNext())
			{
				if (iterator.next().equals(user.getUsername()))
				{
					iterator.next();
					iterator.set(String.valueOf(user.getScore().getHighScore())+ " " + String.valueOf(user.getScore().getBestTime()) + " " + user.getScore().getBestDifficulty() + " " + user.getScore().getBestSize()+ " "
							+ user.getScore().getCurrentScore()	+ " " + String.valueOf(user.getScore().getCurrentTime())
							+ " " + user.getScore().getLastDifficulty() + " " + user.getScore().getLastSize());
					flag = true;
					break;
				}
			}
			if (flag == false)
			{
				scoreData.add(user.getUsername());
				scoreData.add(String.valueOf(user.getScore().getHighScore())+ " " + String.valueOf(user.getScore().getBestTime()) + " " + user.getScore().getBestDifficulty() + " " + user.getScore().getBestSize());
			}
			writer = new FileWriter("Scores.txt");
			iterator = scoreData.listIterator();
			while(iterator.hasNext())
			{
				writer.write(iterator.next());
				writer.write("\n");
			}
			writer.close();
		}
        /* Catches either of the potential errors that could be thrown and handles them with a message */
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not find Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Could not update Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
    /* This method gives the user the option to return to the main menu at any time */
	public void backToMenu ()
	{
        /* uses if and else statements to check what the user would like to do */
		int reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
		if (reply == JOptionPane.YES_OPTION)
		{	
			timer.stop();
			
			reply = JOptionPane.showConfirmDialog(null, "Would you like to see the solution?");
			if (reply == JOptionPane.YES_OPTION)
			{
				MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");
				ShowSolution solution;
				switch (difficulty)
				{
                /* Uses case statements to load the solution */
				case "Easy":
					solution = new ShowSolution("easy16x16Solution.txt","16x16");
					solution.setTitle("Easy 16x16 Solution");
					break;
                        
				case "Medium":
					solution = new ShowSolution("medium16x16Solution.txt", "16x16");
					solution.setTitle("Medium 16x16 Solution");
					break;
                        
				case "Hard":
					solution = new ShowSolution("hard16x16Solution.txt", "16x16");
					solution.setTitle("Hard 16x16 Solution");
					break;
                        
				default:
					solution = new ShowSolution("evil16x16Solution.txt", "16x16");
					solution.setTitle("Evil 16x16 Solution");
					break;
				}
				solution.setSize(700,700);
				solution.setVisible(true);
				solution.setResizable(false);				
				dispose();
			}
			else
			{
				MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");
				dispose();					
			}
		}
	}
    
    /* This method allows users to change the font color to illustrate different paths they are taking */
	public void changeFontColor (String color)
	{
		int i = 0, j = 0;
		Color fontColor;
        /* uses if and else if statements to set the color */
		if (color.equals("Black"))
			fontColor = Color.BLACK;
		else if (color.equals("Cyan"))
			fontColor = Color.CYAN;
		else if (color.equals("Green"))
			fontColor = Color.GREEN;
		else if (color.equals("Magenta"))
			fontColor = Color.MAGENTA;
		else if (color.equals("Orange"))
			fontColor = Color.ORANGE;
		else if (color.equals("Pink"))
			fontColor = Color.PINK;
		else if (color.equals("Red"))
			fontColor = Color.RED;
		else 
			fontColor = Color.YELLOW;
		
        /* uses nested for loops and if statements to actually set the color */
		for (i = 0; i < 16; i++)
		{
			for (j = 0; j < 16; j++)
			{
				if (entries[i][j].isEditable() && entries[i][j].getText().equals(""))
				{
					entries[i][j].setForeground(fontColor);
				}
                
				if (pencilEntries[i][j].isEditable() && pencilEntries[i][j].getText().equals(""))
				{
					pencilEntries[i][j].setForeground(fontColor);
				}
			}
		}
		
	}
	
    /* This method gives the functionality for a user to input guesses and take notes */
	public void switchPencilMode()
	{
		/* Turn On Pencil Mode */
		if(pencilMode_ON_OFF == false)
    	{
    		pencilMode_ON_OFF = true;
    		copyToPencilMode();
    		pencilModeNotification.setText("  Pencil Mode: ON");
    		this.remove(mainBoard);
    		this.add(pencilPanel, BorderLayout.CENTER);
    		this.revalidate();
    		this.repaint();
    	}
		/* Turn Off Pencil Mode */
		else
		{
			pencilMode_ON_OFF = false;
			pencilModeNotification.setText("  Pencil Mode: OFF");
    		this.remove(pencilPanel);
    		this.add(mainBoard, BorderLayout.CENTER);
    		this.revalidate();
    		this.repaint();
		}
	}
    
    /* transfers data already placed to pencil mode */
	public void copyToPencilMode ()
	{
		int i = 0, j = 0;
		
		for (i = 0; i < 16; i++)
		{
			for (j = 0; j < 16; j++)
			{
				if ((pencilEntries[i][j].isEditable()) && (pencilEntries[i][j].getText().equals("")))
				{
					pencilEntries[i][j].setText(entries[i][j].getText());
				}
			}
		}
	}
    
    /* This method gives the game to functionality so the AI and the user can switch turns back and forth */
	public void switchTurns ()
    {
		if (!AI.makeMove())
		{
			JOptionPane.showMessageDialog(null, "Error with AI. Could not make move.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(null,"AI has finished move.", "Player's Turn",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}