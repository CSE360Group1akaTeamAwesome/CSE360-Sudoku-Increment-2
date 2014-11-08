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

public class SudokuBoard16x16 extends JFrame{

	private JTextField[][] entries;
	private int  currentTime=0, seconds = 0, minutes = 0, numberOfHints=0;
	private User user;
	private String difficulty;
	private JTextField timeDisplay;
	private Timer timer;
	
	public SudokuBoard16x16(int width, int height, String diff, User u)
	{
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
		JPanel mainBoard = new JPanel();
		this.add(mainBoard, BorderLayout.CENTER);
		mainBoard.setLayout(new GridLayout(4,4));
		mainBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
		JPanel sideBar = new JPanel();
		sideBar.setLayout(new GridLayout(5,1));
		JTextPane possibleValues = new JTextPane();
		possibleValues.setText("Enter possible Values Below");
		possibleValues.setEditable(true);
		sideBar.add(possibleValues);
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
		JTextFieldLimit [][] doc = new JTextFieldLimit[16][16];
		JPanel [] regions = new JPanel[16];
		
		int i = 0, j = 0, k = 0, l = 0, counter = 0;
		// Initialize 4x4 regions on mainboard
		for(i = 0; i < 16; i++)
		{
			regions[i] = new JPanel();
			regions[i].setLayout(new GridLayout(4,4));
			regions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			mainBoard.add(regions[counter]);
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
		loadPuzzle(difficulty);
	}
	boolean checkRow(int row)
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
				if(checker.equals(entries[row][j].getText()) )
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
	boolean checkColumn(int col)
	{	String checker = "";
		int i = 0, j = 0, count = 0;
		for(j = 0; j < 17; j++)
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
				if(checker.equals(entries[i][col].getText()) )
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
	boolean checkBox(int row, int col)
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
					
						if(checker.equals(entries[i][j].getText()) )
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
	
	boolean isEmptySpace()
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
	boolean checkPuzzle()
	{
		int i = 0;
		if(!isEmptySpace())
		{
			return false;
		}
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
	void loadPuzzle(String difficulty)
	{
		File file;
		if(difficulty.equals("Easy"))
		{
			file = new File("easy16x16Solution.txt");
		}
		else if(difficulty.equals("Medium"))
		{
			file = new File("medium16x16.txt");
		}
		else if(difficulty.equals("Hard"))
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
		try 
		{
			scanner = new Scanner(file);
			
			for(i = 0; i < 16; i++)
			{
				for(j = 0; j < 16; j++)
				{
					if (scanner.hasNext())
					{
						value = scanner.next();
						if(!value.equals("0"))
						{
							try
							{
								entries[i][j].setText(value);
								entries[i][j].setForeground(Color.BLUE);
								entries[i][j].setEditable(false);
							}
							catch(Exception e)
							{
								System.out.println("It broke at i: " + i + " j: " + j + " with " + value);	
							}
						}
						else
						{
							entries[i][j].setText("");
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
							+ " " + user.getScore().getLastDifficulty() + " " + user.getScore().getLastSize() + String.valueOf(user.isGameSaved()));
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
			reply = JOptionPane.showConfirmDialog(null, "Would you like to save your progress before quiting?");
			if(reply == JOptionPane.YES_OPTION)
			{
				saveGame();
				JOptionPane.showMessageDialog(null, "Puzzle Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
				reply = JOptionPane.showConfirmDialog(null, "Would you like to see the solution?");
				if(reply == JOptionPane.YES_OPTION)
				{
					ShowSolution solution = new ShowSolution("easy9x9Solution.txt");
					solution.setSize(500,500);
					solution.setTitle("Solution");
					solution.setVisible(true);
					solution.setResizable(false);
				}
				
			}
			MainMenu menu = new MainMenu(1000,800, user);
			menu.setSize(1000,800);
			menu.setVisible(true);
			menu.setTitle("CSE360 Sudoku Main Menu");
			dispose();
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
}
