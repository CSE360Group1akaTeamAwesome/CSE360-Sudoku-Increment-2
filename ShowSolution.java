import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShowSolution extends JFrame
{
	private JPanel mainPanel;
	private JPanel[] regions;
	private JTextField[][] entries;
	
	public ShowSolution(String gameName)
	{
		int counter = 0, i = 0, j = 0, k = 0, l = 0;
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,3));
		entries = new JTextField[9][9];
		regions = new JPanel[9];
		this.add(mainPanel);
		
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
				mainPanel.add(regions[counter]);
				counter++;
			}
		}
		counter = 1;
		for(i = 0; i < 9; i=i+3)
		{
			for(j = 0; j < 9; j=j+3)
			{
				for(k = i; k < i + 3; k++)
				{
					for(l = j; l < j + 3; l++)
					{
						entries[k][l] = new JTextField(String.valueOf(counter));
						entries[k][l].setEditable(false);
						entries[k][l].setHorizontalAlignment(JTextField.CENTER);
						regions[counter-1].add(entries[k][l]);
					}
				}
				counter++;
			}
		}
		loadSolution(gameName);
	}
	void loadSolution(String nameOfFile)
	{
		File file = new File(nameOfFile);
		int i = 0, j = 0, value = 0;
		Scanner scanner;
		try 
		{
			scanner = new Scanner(file);
			
			for(i = 0; i < 9; i++)
			{
				for(j = 0; j < 9; j++)
				{
					if (scanner.hasNextInt())
					{
						value = scanner.nextInt();						
						try
						{
							entries[i][j].setText(String.valueOf(value));
						}
						catch(Exception e)
						{
							System.out.println("It broke at i: " + i + " j: " + j + " with " + value);	
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
}
