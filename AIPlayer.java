import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import java.util.Random;

public class AIPlayer {
	private JTextField[][] entries;
	private String[][] solution;
	private String size, difficulty;
	
	public AIPlayer(String diff, String bigness, JTextField[][] tiles){
		entries = tiles;
		difficulty = diff;
		size = bigness;
		if(size.equals("9x9"))
			solution = new String[9][9];
		else
			solution = new String[16][16];
		setSolution();
	}
	public boolean makeMove(){
		if(findMistake() == false)
		{
			if(addNumberToBoard())
			{
				return true;
			}
			else
			{
				if(updateEmptySpace())
				{
					return true;
				}
				return false;
			}
		}
		else
			return true;
		
	}
	public boolean findMistake(){
		int i = 0, j = 0;
		if(size.equals("9x9"))
		{
			for(i = 0; i < 9; i++)
			{
				for(j = 0; j < 9; j++)
				{
					if( !(entries[i][j].getText().equals("")) && (entries[i][j].isEditable()) )
					{
						if(!entries[i][j].getText().equals(solution[i][j]))
						{
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
		else
		{
			for(i = 0; i < 16; i++)
			{
				for(j = 0; j < 16; j++)
				{
					if( !(entries[i][j].getText().equals("")) && (entries[i][j].isEditable()) )
					{
						if(!entries[i][j].getText().equals(solution[i][j]))
						{
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
	public boolean addNumberToBoard(){
		int row = 0, col = 0, counter = 0;
		Random randomGenerator = new Random();
		if(size.equals("9x9"))
		{
			while(counter < 200)
			{
				row = randomGenerator.nextInt(9);
				col = randomGenerator.nextInt(9);
				if( (entries[row][col].getText().equals("")) && (entries[row][col].isEditable()) )
				{
					entries[row][col].setText(solution[row][col]);
					entries[row][col].setForeground(Color.WHITE);
					entries[row][col].setEditable(false);
					entries[row][col].setBackground(Color.DARK_GRAY);
					return true;
				}
				counter++;
			}
		}
		else
		{
			while(counter < 200)
			{
				row = randomGenerator.nextInt(16);
				col = randomGenerator.nextInt(16);
				if( (entries[row][col].getText().equals("")) && (entries[row][col].isEditable()) )
				{
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
	public boolean updateEmptySpace()
	{
		int i = 0, j = 0;
		if(size.equals("9x9"))
		{
			for(i = 0; i < 9; i++)
			{
				for(j = 0; j < 9; j++)
				{
					if(entries[i][j].getText().equals(""))
					{
						entries[i][j].setText(solution[i][j]);
					}
				}
			}
		}
		else
		{
			for(i = 0; i < 16; i++)
			{
				for(j = 0; j < 16; j++)
				{
					if(entries[i][j].getText().equals(""))
					{
						entries[i][j].setText(solution[i][j]);
					}
				}
			}
		}
		return false;
	}
	public void setSolution(){
		File file = null;
		int i = 0, j = 0, v = 0;
		String value ="";
		Scanner scanner;
		if(size.equals("9x9"))
		{
			switch(difficulty)
			{
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
		}
		else
		{
			switch(difficulty)
			{
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
		try 
		{
			scanner = new Scanner(file);
			if(size.equals("9x9"))
			{
				for(i = 0; i < 9; i++)
				{
					for(j = 0; j < 9; j++)
					{
						if (scanner.hasNextInt())
						{
							v= scanner.nextInt();						
							try
							{
								solution[i][j]=(String.valueOf(v));
							}
							catch(Exception e)
							{
								System.out.println("It broke at i: " + i + " j: " + j + " with " + String.valueOf(v));	
							}
		
						}
					}
				}
			}
			else
			{
				for(i = 0; i < 16; i++)
				{
					for(j = 0; j < 16; j++)
					{
						if (scanner.hasNext())
						{
							value = scanner.next();						
							try
							{
								solution[i][j] = value;
							}
							catch(Exception e)
							{
								System.out.println("It broke at i: " + i + " j: " + j + " with " + value);	
							}
		
						}
					}
				}
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not load solution. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	public boolean determineIfFinished()
	{
		int i = 0, j = 0;
		if(size.equals("9x9"))
		{
			for(i = 0; i < 9; i++)
			{
				for(j = 0; j < 9; j++)
				{
					System.out.println(entries[i][j].getText());
					if((entries[i][j].getText().equals(solution[i][j])) == false)
					{
						return false;
					}
				}
			}
		}
		else
		{
			for(i = 0; i < 16; i++)
			{
				for(j = 0; j < 16; j++)
				{
					if((entries[i][j].getText().equals(solution[i][j])) == false)
					{
						return false;
					}
				}
			}
		}
		return true;	
	}
}
