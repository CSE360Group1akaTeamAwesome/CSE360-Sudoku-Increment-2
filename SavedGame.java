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
public class SavedGame extends SudokuBoard
{
	// Private instance variables for features of the board
	private int currentTime=0, seconds = 0, minutes = 0, numberOfHints=0;
	private JTextField[][] entries;
	private JTextField timeDisplay; 
	private Timer timer;
	private User user;
	private String difficulty;
	// Constructor for the new Board
	public SavedGame(int width, int height, User u)
	{
		user = u;
		this.setBackground(Color.WHITE);
		
		this.setLayout(new BorderLayout());
		
		JTextPane titleMessage = new JTextPane();
    	
		JPanel title = new JPanel();
		title.setLayout(new FlowLayout());
		title.setBackground(Color.WHITE);
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
		JPanel mainBoard = new JPanel();
		this.add(mainBoard, BorderLayout.CENTER);
		mainBoard.setLayout(new GridLayout(3,3));
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
		
		
		entries = new JTextField[9][9];
		JTextFieldLimit[][] doc = new JTextFieldLimit[9][9];
		JPanel []regions = new JPanel[9];
		
		int i = 0, j = 0, k = 0, l = 0, counter = 0;
		// Initialize 3x3 regions
		for(i = 0; i < 9; i++)
		{
			regions[i] = new JPanel();
			regions[i].setLayout(new GridLayout(3,3));
			regions[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		//Initialize mainBoard
		for(i = 0; i < 3; i++)
		{
			for(j = 0; j < 3; j++)
			{
				mainBoard.add(regions[counter]);
				counter++;
			}
		}
		
		counter = 1;
		for(i = 0; i < 9; i=i+3)
		{
			for(j = 0; j < 9; j=j+3)
			{
				for( k = i; k < i + 3; k++)
				{
					for(l = j; l < j + 3; l++)
					{
						entries[k][l] = new JTextField(String.valueOf(counter));
						entries[k][l].setHorizontalAlignment(JTextField.CENTER);
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
		loadPuzzle();
		try {
			doc2.insertString(doc2.getLength(), "Solve this " + difficulty + " Puzzle, " + user.getUsername(), messageFont );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
						solution = new ShowSolution("easy9x9Solution.txt", "9x9");
						break;
					case "Medium":
						solution = new ShowSolution("medium9x9Solution.txt", "9x9");
						break;
					case "Hard":
						solution = new ShowSolution("hard9x9Solution.txt", "9x9");
						break;
					default:
						solution = new ShowSolution("evil9x9Solution.txt", "9x9");
						break;
					}
					solution.setSize(500,500);
					solution.setTitle("Solution");
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
	public boolean checkRow(int row)
	{
		int i = 0, j = 0, count = 0;
		for(i = 1; i < 10; i++)
		{	
			count = 0;
			for(j = 0; j < 9; j++)
			{
				if(i == Integer.parseInt(entries[row][j].getText()) )
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
	{
		int i = 0, j = 0, count = 0;
		for(j = 0; j < 10; j++)
		{
			count = 0;
			for(i = 0; i < 9; i++)
			{
				if(j == Integer.parseInt(entries[i][col].getText()) )
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
		int i = 0, j = 0, k = 0, count = 0;
		for(k = 1; k < 10; k++)
		{
			for(i = row; i < row+3; i++)
			{
				for(j = col; j < col+3; j++)
				{	
					count = 0;
					
						if(k == Integer.parseInt(entries[i][j].getText()) )
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
			for(j = 0; j < 9; j++)
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
	public boolean checkPuzzle()
	{
		int i = 0;
		if(!isEmptySpace())
		{
			return false;
		}
		if(!validateInput())
		{
			return false;
		}
		for(i = 0; i < 9; i++)
		{
			if(checkRow(i) == false || checkColumn(i) == false)
			{
				return false;
			}
		}
		
		if(!checkBox(0,0)||!checkBox(0,3)||!checkBox(0,6)||!checkBox(3,0)||!checkBox(3,3)||!checkBox(3,6)||!checkBox(6,0)||!checkBox(6,3)||!checkBox(6,6))
			return false;
		
		return true;
	}
	
	public boolean validateInput()
	{
		int i = 0, j = 0;
		for(i = 0; i < 9; i++)
		{
			for(j = 0; j < 9; j++)
			{
				    try 
				    {
				    	
				        if(Integer.parseInt(entries[i][j].getText()) < 1)
				        {
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
	// This should should contain a parameter based on the difficulty of the puzzle
	public void loadPuzzle()
	{
		File file;
		file = new File("Saved_Games.txt");
		int i = 0, j = 0, value = 0;
		Scanner scanner;
		String line;
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
			for(i = 0; i < 9; i++)
			{
				for(j = 0; j < 9; j++)
				{
					if(scanner.next().equals("E"))
						editable = true;
					else
						editable = false;
					
					if (scanner.hasNextInt())
					{
						value = scanner.nextInt();
						if(value != 0)
						{
							try
							{
								entries[i][j].setText(String.valueOf(value));
								if(editable == true)
								{
									entries[i][j].setForeground(Color.BLACK);
									entries[i][j].setEditable(editable);
								}
								else
								{
									entries[i][j].setForeground(Color.BLUE);
									entries[i][j].setEditable(editable);
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
	public String getCurrentPuzzle()
	{
		String currentPuzzle = "";
		int i = 0, j = 0;
		for(i = 0; i < 9; i++)
		{
			for(j = 0; j < 9; j++)
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
					iterator.set("9x9");
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
				data.add("9x9");
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
		
		setUserSavedGame();
		user.setHasSavedGame(true);
		
	}
	public void setUserSavedGame()
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
							if(line.equals("false"))
								iterator.set("true");
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
	
}
