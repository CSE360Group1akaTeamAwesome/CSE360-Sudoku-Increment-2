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

	private JTextField[][] entries;
	private int  currentTime=0, seconds = 0, minutes = 0, numberOfHints=0;
	private User user;
	private String difficulty;
	private JTextField timeDisplay;
	private Timer timer;
	public SavedGame16x16(int width, int height, User u)
	{
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
						solution = new ShowSolution("easy16x16Solution.txt", "16x16");
						break;
					case "Medium":
						solution = new ShowSolution("medium16x16Solution.txt", "16x16");
						break;
					case "Hard":
						solution = new ShowSolution("hard16x16Solution.txt", "16x16");
						break;
					default:
						solution = new ShowSolution("evil16x16Solution.txt", "16x16");
						break;
					}
					solution.setSize(700,700);
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
		setUserSavedGame();
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