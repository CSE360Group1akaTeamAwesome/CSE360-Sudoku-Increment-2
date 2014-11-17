import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.*;

public class LogIn extends JFrame
{
	private JLabel userPrompt, passwordPrompt;
	private JTextField username;
	private JPasswordField password;
	private JButton signUp, submit;
	private JPanel prompts, buttons;
	
	public LogIn(int width, int height)
	{
		this.setSize(width,height);
		this.setLayout(new GridLayout(2,1));
		
		prompts = new JPanel();
		prompts.setLayout(new GridLayout(2,4));
		this.add(prompts);
		
		userPrompt = new JLabel("Enter username: ");
		passwordPrompt = new JLabel("Enter password: ");
		username = new JTextField();
		password = new JPasswordField(16);
		prompts.add(userPrompt);
		prompts.add(username);
		prompts.add(passwordPrompt);
		prompts.add(password);
		
		buttons = new JPanel();
		this.add(buttons);		buttons.setLayout(new GridLayout(1,2));
		signUp = new JButton("Create Profile");
		buttons.add(signUp);
		signUp.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae)
	    {
	    	Sign_Up signUp = new Sign_Up(500,150);
	    	signUp.setVisible(true);
	    	dispose();
	    }
	     });
		submit = new JButton("Submit");
		buttons.add(submit);
		submit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae)
		{
		   if(loadUser(username.getText(), new String(password.getPassword()) ) )
		   { 
				User user = new User(username.getText(),null,findScore(username.getText()));
				user.setHasSavedGame(userHasGamedSaved(user));
			    MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
				menu.setTitle("CSE360 Sudoku Main Menu");
				dispose();
		   }
		   else
		   {
			   JOptionPane.showMessageDialog(null, "Username or Password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);   
		   }
			
		}});
		initializeFiles();
	}
	public boolean loadUser(String username, String password)
	{
		File file = new File("Users.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
			   String lineFromFile = scanner.nextLine();
			   if(lineFromFile.contains(username))
			   { 
			      lineFromFile = scanner.nextLine();
			      if(password.equals(lineFromFile))
			      {
			    	  scanner.close();
			    	  return true;
			      }
			   }
			}
			scanner.close();
			return false;
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "System Error: Can't find User file.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch(Exception a)
		{
			JOptionPane.showMessageDialog(null, "System Error: Can't find User data.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	public Score findScore(String name)
	{
		File file = new File("Scores.txt");
		double highScore, lastScore;
		int bestTime,lastTime;
		String lineFromFile, bestDifficulty, bestSize,lastDifficulty,lastSize;
		StringTokenizer scoreData;
		Score userScore = new Score();
		Scanner scanner;
		try
		{
			scanner = new Scanner(file);
			while(scanner.hasNextLine())
			{
				lineFromFile = scanner.nextLine();
				System.out.println("Username Search Result: " + lineFromFile + "\n");
				if(name.equals(lineFromFile) && scanner.hasNextLine())
				{
					lineFromFile = scanner.nextLine();
					System.out.println("Data Search Result: " + lineFromFile+"\n");
					scoreData = new StringTokenizer(lineFromFile);
					highScore = Double.parseDouble(scoreData.nextElement().toString());
					bestTime = Integer.parseInt(scoreData.nextElement().toString());
					bestDifficulty = scoreData.nextElement().toString();
					bestSize = scoreData.nextElement().toString();
					lastScore = Double.parseDouble(scoreData.nextElement().toString());
					lastTime = Integer.parseInt(scoreData.nextElement().toString());
					lastDifficulty = scoreData.nextElement().toString();
					lastSize = scoreData.nextElement().toString();
					userScore.loadScore(highScore,bestTime,bestDifficulty,bestSize,lastScore,lastTime,lastDifficulty,lastSize);
					System.out.println("Score data:\n\tHigh Score: " + highScore + "\n\tTime: " + bestTime + "\n\tDifficulty: " + bestDifficulty + "\n\tSize: " + bestSize + "\n\tLast Score: " + lastScore + "\n\tTime: " + lastTime + "\n\tDifficulty: " + lastDifficulty + "\n\tSize: " + lastSize);
					break;
				
				}
			}
			scanner.close();
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "System Error: Can't find Score data.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch(NumberFormatException n)
		{
			JOptionPane.showMessageDialog(null, "System Error: Can't process Score data.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return userScore;
	}
	public boolean userHasGamedSaved(User user)
	{
		File file = new File("Users.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
			   String lineFromFile = scanner.nextLine();
			   if(lineFromFile.contains(user.getUsername()))
			   { 
			      lineFromFile = scanner.nextLine();
			      if(scanner.hasNextLine())
			      {
			    	  lineFromFile = scanner.nextLine();
			    	  if(lineFromFile.equals("true"))
			    	  {
			    		  scanner.close();
				    	  return true;
			    	  }
			    	  else
			    	  {
				    	  scanner.close();
				    	  return false;
			    	  }
			    	  
			      }
			   }
			}
			scanner.close();
			return false;
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "System Error: Can't find User File.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch(Exception a)
		{
			JOptionPane.showMessageDialog(null, "System Error: Can't find User Saved Game Info.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	public void initializeFiles()
	{
		File Users, Scores, Saved_Games;
		Users = new File("Users.txt");
		Scores = new File("Scores.txt");
		Saved_Games = new File("Saved_Games.txt");
		try{
			if (!Users.exists()) {
				Users.createNewFile();
			}
			if (!Scores.exists()) {
				Scores.createNewFile();
			}
			if (!Saved_Games.exists()) {
				Saved_Games.createNewFile();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "System Error. Could not create necessary system files.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
}
