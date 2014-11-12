import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SavedGame16x16 extends SudokuBoard16x16{

	private int currentTime=0, seconds = 0, minutes = 0, numberOfHints=0;
	private JTextField[][] entries,pencilEntries;
	private JTextField timeDisplay,pencilModeNotification; 
	private Timer timer;
	private User user;
	private String difficulty;
	private JPanel pencilPanel, mainBoard;
	private JPanel[] pencilRegions;
	private boolean pencilMode_ON_OFF;
	
	public SavedGame16x16(int width, int height, User u)
	{
		pencilMode_ON_OFF = false;
		user = u;
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
		sideBar.setLayout(new GridLayout(6,1));
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
		JButton save = new JButton("Save Puzzle");
		save.addActionListener(new ActionListener() 
	    {
		    public void actionPerformed(ActionEvent ae)
		    {
		    	saveGame();
				JOptionPane.showMessageDialog(null, "Puzzle Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
		    }
		});
		JButton check = new JButton("Check Puzzle");
		check.addActionListener(new ActionListener() 
	    {
		    public void actionPerformed(ActionEvent ae)
		    {
		    	if(checkPuzzle())
		    	{
		    		timer.stop();
		    		computeStats();	
		    		setUserSavedGame("false");
		    		user.setHasSavedGame(false);
		    		int reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
		    		if(reply == JOptionPane.YES_OPTION)
		    		{
			    		MainMenu menu = new MainMenu(1000,800, user);
						menu.setSize(1000,800);
						menu.setVisible(true);
						menu.setTitle("CSE360 Sudoku Main Menu");
					}
		    		dispose();
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(null, "Incorrect Answer. Victory has defeated you.", "Puzzle Incomplete", JOptionPane.ERROR_MESSAGE);
		    	}
		    }
		});
		JButton hint = new JButton("Hint");
		hint.addActionListener(new ActionListener() 
	    {
		    public void actionPerformed(ActionEvent ae)
		    {
				JOptionPane.showMessageDialog(null, "Hint: You just lost 5 points.", "Hint", JOptionPane.INFORMATION_MESSAGE);
		    	numberOfHints++;
		    }
		});
		JButton quit = new JButton("Quit Puzzle");
		quit.addActionListener(new ActionListener() 
	    {
		    public void actionPerformed(ActionEvent ae)
		    {
		    	backToMenu();
		    }
		});
		sideBar.add(save);
		sideBar.add(check);
		sideBar.add(hint);
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
		fontColorPanel.add(Box.createRigidArea(new Dimension(0,900)));
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
		// Initialize 4x4 regions on mainboard
		for(i = 0; i < 16; i++)
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
		for(i = 0; i < 16; i=i+4)
		{
			for(j = 0; j < 16; j=j+4)
			{
				for(k = i; k < i + 4; k++)
				{
					for(l = j; l < j + 4; l++)
					{
						pencilEntries[k][l] = new JTextField();
						pencilEntries[k][l].setHorizontalAlignment(JTextField.CENTER);
						pencilRegions[counter-1].add(pencilEntries[k][l]);
						
						entries[k][l] = new JTextField(String.valueOf(counter%9));
						entries[k][l].setHorizontalAlignment(JTextField.CENTER);
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
		loadPuzzle();
		try {
			doc2.insertString(doc2.getLength(), "Solve this " + difficulty + " Puzzle, " + user.getUsername(), messageFont );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkRow(int row)
	{
		String checker = "";
		int i = 0, j = 0, count = 0;
		for(i = 1; i < 17; i++)
		{	
			switch(i)
			{
				case 10:
					checker = "A";
					break;
				case 11:
					checker = "B";
					break;
				case 12:
					checker = "C";
					break;
				case 13:
					checker = "D";
					break;
				case 14:
					checker = "E";
					break;
				case 15:
					checker = "F";
					break;
				case 16:
					checker = "G";
					break;
				default:
					checker = String.valueOf(i);
					break;
			}
			count = 0;
			for(j = 0; j < 16; j++)
			{
				if(checker.equals(entries[row][j].getText().toUpperCase()) )
				{
					count++;
				}
				if( count >= 2)
				{
					System.out.println("checkRow() returned false at i: " + row + " j: "+j + " with " + entries[row][j].getText()); 
					return false;
				}
				
			}
		}
		
		return true;
	}
	public boolean checkColumn(int col)
	{	String checker = "";
		int i = 0, j = 0, count = 0;
		for(j = 1; j < 17; j++)
		{
			switch(j)
			{
				case 10:
					checker = "A";
					break;
				case 11:
					checker = "B";
					break;
				case 12:
					checker = "C";
					break;
				case 13:
					checker = "D";
					break;
				case 14:
					checker = "E";
					break;
				case 15:
					checker = "F";
					break;
				case 16:
					checker = "G";
					break;
				default:
					checker = String.valueOf(j);
					break;
			}
			count = 0;
			for(i = 0; i < 16; i++)
			{
				if(checker.equals(entries[i][col].getText().toUpperCase()) )
				{
					count++;
				}
				if(count >= 2)
				{
					System.out.println("checkColumn() returned false at i: " + i + " j: "+ col + " with " + entries[i][col].getText()); 
					return false;
				}
				
			}
		}
		
		return true;
	}
	public boolean checkBox(int row, int col)
	{
		String checker = "";
		int i = 0, j = 0, k = 0, count = 0;
		for(k = 1; k < 17; k++)
		{
			switch(k)
			{
				case 10:
					checker = "A";
					break;
				case 11:
					checker = "B";
					break;
				case 12:
					checker = "C";
					break;
				case 13:
					checker = "D";
					break;
				case 14:
					checker = "E";
					break;
				case 15:
					checker = "F";
					break;
				case 16:
					checker = "G";
					break;
				default:
					checker = String.valueOf(k);
					break;
			}
			for(i = row; i < row+4; i++)
			{
				for(j = col; j < col+4; j++)
				{	
					count = 0;
					
						if(checker.equals(entries[i][j].getText().toUpperCase()) )
						{
							count++;
						}
						if(count >= 2)
						{
							System.out.println("checkBox() returned false at i: " + i + " j: "+ j + " with " + entries[i][j].getText()); 
							return false;
						}
					
				}
			}
		}
		
		return true;
	}
	
	public boolean isEmptySpace()
	{
		int i = 0, j = 0;
		for(i = 0; i < 9; i++)
		{
			for(j = 0; j < 16; j++)
			{	
				if(entries[i][j].getText().equals(""))
				{
					System.out.println("isEmptySpace() returned false at i: " + i + " j: "+j); 
					return false;
				}
			}
		}
		return true;
	}
	public boolean validateInput()
	{
		int i = 0, j = 0;
		for(i = 0; i < 16; i++)
		{
			for(j = 0; j < 16; j++)
			{
				    try 
				    {
				    	
				    	if(!( entries[i][j].getText().equals("1")||entries[i][j].getText().equals("2")||entries[i][j].getText().equals("3")||entries[i][j].getText().equals("4")
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
	public boolean checkPuzzle()
	{
		int i = 0;
		if(!isEmptySpace())
		{
			return false;
		}
		if(!validateInput())
			return false;
		for(i = 0; i < 16; i++)
		{
			if(checkRow(i) == false || checkColumn(i) == false)
			{
				return false;
			}
		}
		
		if(!checkBox(0,0)||!checkBox(0,4)||!checkBox(0,8)||!checkBox(0,12)||!checkBox(4,0)||!checkBox(4,4)||!checkBox(4,8)||!checkBox(4,12)||!checkBox(8,0)||!checkBox(8,4)||!checkBox(8,8)||!checkBox(8,12)||!checkBox(12,0)||!checkBox(12,4)||!checkBox(12,8)||!checkBox(12,12))
			return false;
		
		return true;
	}
	
	// This should should contain a parameter based on the difficulty of the puzzle
	public void loadPuzzle()
	{
		File file;
		file = new File("Saved_Games.txt");
		int i = 0, j = 0;
		Scanner scanner;
		String line, value;
		boolean flag = false, editable = false;
		try 
		{
			scanner = new Scanner(file);
			while(scanner.hasNextLine() && flag == false)
			{
				line = scanner.nextLine();
				if(line.equals(user.getUsername()))
				{
					difficulty = scanner.nextLine();
					scanner.nextLine();
					currentTime = Integer.parseInt(scanner.nextLine());
					flag = true;
				}
			}
			for(i = 0; i < 16; i++)
			{
				for(j = 0; j < 16; j++)
				{
					if(scanner.next().equals("E"))
						editable = true;
					else
						editable = false;
					
					if (scanner.hasNext())
					{
						value = scanner.next();
						if(!value.equals("0"))
						{
							try
							{
								entries[i][j].setText(String.valueOf(value));
								if(editable == true)
								{
									entries[i][j].setForeground(Color.BLACK);
									entries[i][j].setEditable(editable);
									
									pencilEntries[i][j].setText(String.valueOf(value));
									pencilEntries[i][j].setForeground(Color.BLACK);
									pencilEntries[i][j].setEditable(editable);
								}
								else
								{
									entries[i][j].setForeground(Color.BLUE);
									entries[i][j].setEditable(editable);
									
									pencilEntries[i][j].setText(String.valueOf(value));
									pencilEntries[i][j].setForeground(Color.BLUE);
									pencilEntries[i][j].setEditable(editable);
								}
							}
							catch(Exception e)
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
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not load puzzle. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}		
	}
	
	public void computeStats()
	{
		user.getScore().setCurrentTime(currentTime);
		user.getScore().setNumberOfHints(numberOfHints);
		user.getScore().setLastDifficulty(difficulty);
		user.getScore().setLastSize("16x16");
		user.getScore().calculateScore();
		user.getScore().displayLatestStats();
		user.setSavedGameSize("16x16");
		saveScoreStats();
		
	}
	
	public void saveScoreStats()
	{
		File file = new File("Scores.txt");	
		boolean flag = false;
		FileWriter writer;
		ArrayList<String> scoreData = new ArrayList<String>();
		ListIterator<String> iterator;
		
		try
		{
			Scanner s = new Scanner(file);
			while(s.hasNextLine())
			{
				scoreData.add(s.nextLine());
			}
			s.close();
			
			iterator = scoreData.listIterator();
			while(iterator.hasNext())
			{
				if(iterator.next().equals(user.getUsername()))
				{
					iterator.next();
					iterator.set(String.valueOf(user.getScore().getHighScore())+ " " + String.valueOf(user.getScore().getBestTime()) + " " + user.getScore().getBestDifficulty() + " " + user.getScore().getBestSize()+ " "
							+ user.getScore().getCurrentScore()	+ " " + String.valueOf(user.getScore().getCurrentTime())
							+ " " + user.getScore().getLastDifficulty() + " " + user.getScore().getLastSize());
					flag = true;
					break;
				}
			}
			if(flag == false)
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
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not find Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Could not update Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
	public void backToMenu()
	{
		int reply = JOptionPane.showConfirmDialog(null, "Would you like to return to the Main Menu?");
		if(reply == JOptionPane.YES_OPTION)
		{
			timer.stop();
			reply = JOptionPane.showConfirmDialog(null, "Would you like to save your progress before quiting?");
			if(reply == JOptionPane.YES_OPTION)
			{
				saveGame();
				JOptionPane.showMessageDialog(null, "Puzzle Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
				MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");	
				dispose();		
			}
			else
			{
				reply = JOptionPane.showConfirmDialog(null, "Would you like to see the solution?");
				if(reply == JOptionPane.YES_OPTION)
				{
					MainMenu menu = new MainMenu(1000,800, user);
					menu.setSize(1000,800);
					menu.setVisible(true);
					menu.setTitle("CSE360 Sudoku Main Menu");
					ShowSolution solution;
					switch(difficulty)
					{
					case "Easy":
						solution = new ShowSolution("easy16x16Solution.txt", "16x16");
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
	}
	
	public void changeFontColor(String color)
	{
		int i = 0, j = 0;
		Color fontColor;
		if(color.equals("Black"))
			fontColor = Color.BLACK;
		else if(color.equals("Cyan"))
			fontColor = Color.CYAN;
		else if(color.equals("Green"))
			fontColor = Color.GREEN;
		else if(color.equals("Magenta"))
			fontColor = Color.MAGENTA;
		else if(color.equals("Orange"))
			fontColor = Color.ORANGE;
		else if(color.equals("Pink"))
			fontColor = Color.PINK;
		else if(color.equals("Red"))
			fontColor = Color.RED;
		else 
			fontColor = Color.YELLOW;
		
		for(i = 0; i < 16; i++)
		{
			for(j = 0; j < 16; j++)
			{
				if(entries[i][j].isEditable() && entries[i][j].getText().equals(""))
				{
					entries[i][j].setForeground(fontColor);
				}
				if(pencilEntries[i][j].isEditable() && pencilEntries[i][j].getText().equals(""))
				{
					pencilEntries[i][j].setForeground(fontColor);
				}
			}
		}
		
	}
			
	public void saveGame()
	{
		File file = new File("Saved_Games.txt");	
		FileWriter writer;
		boolean flag = false;
		ArrayList<String> data = new ArrayList<String>();
		ListIterator<String> iterator;
		String currentPuzzle = getCurrentPuzzle();
		try
		{
			Scanner s = new Scanner(file);
			while(s.hasNextLine())
			{
				data.add(s.nextLine());
			}
			s.close();
			
			iterator = data.listIterator();
			while(iterator.hasNext())
			{
				if(iterator.next().equals(user.getUsername()))
				{
					iterator.next();
					iterator.set(difficulty);
					iterator.next();
					iterator.set("16x16");
					iterator.next();
					iterator.set(String.valueOf(currentTime));
					if(iterator.hasNext())
					{
						iterator.next();
						iterator.set(currentPuzzle);
						flag = true;
						break;
					}
					else
					{
						data.add(currentPuzzle);
						break;
					}
				}
			}
			if(flag == false)
			{
				data.add(user.getUsername());
				data.add(difficulty);
				data.add("16x16");
				data.add(String.valueOf(currentTime));
				data.add(currentPuzzle);
			}
			writer = new FileWriter("Saved_Games.txt");
			iterator = data.listIterator();
			while(iterator.hasNext())
			{
				writer.write(iterator.next());
				writer.write("\n");
			}
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not find Saved_Games. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Could not update Saved_Games. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		setUserSavedGame("true");
		user.setHasSavedGame(true);
		
	}
	
	public String getCurrentPuzzle()
	{
		String currentPuzzle = "";
		int i = 0, j = 0;
		for(i = 0; i < 16; i++)
		{
			for(j = 0; j < 16; j++)
			{
				if(entries[i][j].isEditable())
				{
					currentPuzzle = currentPuzzle +"E ";
				}
				else
				{
					currentPuzzle = currentPuzzle +"N ";
				}
				if(entries[i][j].getText().equals(""))
				{
					currentPuzzle = currentPuzzle + "0 ";
				}
				else
				{
					currentPuzzle = currentPuzzle + entries[i][j].getText() + " ";
				}
			}
			
		}
		System.out.println("Current Puzzle is" + currentPuzzle);
		return currentPuzzle;
	}
	
	public void setUserSavedGame(String true_or_false)
	{
		File file = new File("Users.txt");	
		String line = "";
		FileWriter writer;
		ArrayList<String> userData= new ArrayList<String>();
		ListIterator<String> iterator;
		
		try
		{
			Scanner s = new Scanner(file);
			while(s.hasNextLine())
			{
				userData.add(s.nextLine());
			}
			s.close();
			
			iterator = userData.listIterator();
			while(iterator.hasNext())
			{
				if(iterator.next().equals(user.getUsername()))
				{
					if(iterator.hasNext())
					{
						iterator.next();
						if(iterator.hasNext())
						{
							line = iterator.next();
							iterator.set(true_or_false);
							break;
						}
					}
					
				}
			}
			writer = new FileWriter("Users.txt");
			iterator = userData.listIterator();
			while(iterator.hasNext())
			{
				writer.write(iterator.next());
				writer.write("\n");
			}
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not find User file. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Could not update Users. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void switchPencilMode()
	{
		// Turn On Pencil Mode
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
		// Turn Off Pencil Mode
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
	public void copyToPencilMode()
	{
		int i = 0, j = 0;
		
		for(i = 0; i < 16; i++)
		{
			for(j = 0; j < 16; j++)
			{
				if((pencilEntries[i][j].isEditable()) && (pencilEntries[i][j].getText().equals("")))
				{
					pencilEntries[i][j].setText(entries[i][j].getText());
				}
			}
		}
	}
}