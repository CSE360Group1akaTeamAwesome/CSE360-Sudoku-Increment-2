import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import javax.swing.*;

public class Profile extends JFrame
{
	private JPanel mainPanel, userPanel, buttonPanel;
	private JButton viewLastScore, viewHighScore, editUsername, editPassword;
	private User user;
	private JLabel userName;
	private JTextField username, confirmUsername;
	private JPasswordField oldPassword, password, confirmPassword;
	private JFrame profileFrame;
	public Profile(User u)
	{
		user = u;
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		viewLastScore = new JButton("View Latest Game Data");
		viewLastScore.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(user.getScore().getLastDifficulty().equals("") ||user.getScore().getLastSize().equals("")||user.getScore().getCurrentScore()== 0.0 ||user.getScore().getCurrentTime() == 0)
				{
					JOptionPane.showMessageDialog(null,"Solve a puzzle, then return here before exiting out to see the results.","No Game Played For This Program Instance",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"User: " + user.getUsername() + "\n" + user.getScore().getLastDifficulty() +" " + user.getScore().getLastSize()+ " Puzzle solved in " 
												+ user.getScore().getCurrentTime()+" seconds"+ "\nScore: " + user.getScore().getCurrentScore(),"Latest Game Results",JOptionPane.INFORMATION_MESSAGE);
				}
			}
					
		});
		viewHighScore = new JButton("View High Score Game Data");
		viewHighScore.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(user.getScore().getBestDifficulty().equals("") ||user.getScore().getBestSize().equals("")||user.getScore().getHighScore()== 0.0 ||user.getScore().getBestTime() == 0)
				{
					JOptionPane.showMessageDialog(null,"No puzzle solved yet.","No Game Played Yet",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"User: " + user.getUsername() + "\n" + user.getScore().getBestDifficulty() +" " + user.getScore().getBestSize()+ " Puzzle solved in " + user.getScore().getBestTime()+" seconds\nScore: " + user.getScore().getHighScore(),"High Score Game Results",JOptionPane.INFORMATION_MESSAGE);
				}
			}
					
		});
		editUsername = new JButton("Change Username");
		editPassword = new JButton("Change Password");
		this.add(mainPanel);
		userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(1,1));
		userName  = new JLabel("Username: " + user.getUsername());
		userPanel.add(userName);
		mainPanel.add(userPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		mainPanel.add(buttonPanel);
		
		buttonPanel.add(viewLastScore);
		buttonPanel.add(viewHighScore);
		buttonPanel.add(editUsername);
		buttonPanel.add(editPassword);
		editUsername.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(2,1));
				JPanel changeProf = new JPanel();
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());
			    changeProf.setLayout(new GridLayout(3,2));
			    JLabel promptUsername = new JLabel("Enter current username: ");
			    JLabel promptNewUsername= new JLabel("Enter new username: ");
			    JLabel promptPassword= new JLabel("Enter password: ");
			    username = new JTextField();
			    confirmUsername = new JTextField();
			    password = new JPasswordField(16);
			    changeProf.add(promptUsername);
			    changeProf.add(username);
			    changeProf.add(promptNewUsername);
			    changeProf.add(confirmUsername);
			    changeProf.add(promptPassword);
			    changeProf.add(password);
			    panel.add(changeProf);
			    JButton submit = new JButton("Submit");
			    buttonPanel.add(submit);
			    panel.add(buttonPanel);
			    profileFrame = new JFrame();
			    profileFrame.setVisible(true);
			    profileFrame.setTitle("Change Username");
				profileFrame.add(panel);
				profileFrame.setSize(500,150);
			    submit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(checkPassword(new String(password.getPassword())))
						{
							if(username.getText().equals(user.getUsername()))
							{
								if(checkUsername(confirmUsername.getText()))
								{
									changeUsername(confirmUsername.getText());
									user.setUsername(confirmUsername.getText());
									userName.setText("Username: " + user.getUsername());
									changeTitle();
									JOptionPane.showMessageDialog(null, "Username changed to " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);
									profileFrame.dispose();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Incorrect Current Username", "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				);
			    
			}
		});
		editPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(2,1));
				JPanel changeProf = new JPanel();
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());
			    changeProf.setLayout(new GridLayout(3,2));
			    JLabel promptOldPassword = new JLabel("Enter old password");
			    JLabel promptPassword= new JLabel("Enter new password:");
			    JLabel promptConfirmPassword= new JLabel("Confirm password:");
			    oldPassword = new JPasswordField(16);
			    password = new JPasswordField(16);
			    confirmPassword = new JPasswordField(16);
			    changeProf.add(promptOldPassword);
			    changeProf.add(oldPassword);
			    changeProf.add(promptPassword);
			    changeProf.add(password);
			    changeProf.add(promptConfirmPassword);
			    changeProf.add(confirmPassword);
			    panel.add(changeProf);
			    JButton submit = new JButton("Submit");
			    buttonPanel.add(submit);
			    panel.add(buttonPanel);
			    profileFrame = new JFrame();
			    profileFrame.setVisible(true);
			    profileFrame.setTitle("Change Password");
				profileFrame.add(panel);
				profileFrame.setSize(500,150);
			    submit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(checkPassword(new String(oldPassword.getPassword())))
						{
							if(checkPassword(new String(confirmPassword.getPassword()),new String(password.getPassword())))
							{									
									changePassword(new String(password.getPassword()));
									JOptionPane.showMessageDialog(null, "Password changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
									profileFrame.dispose();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				);
			    
			}
			
		});
		
	}
	public void changeTitle()
	{
		this.setTitle(user.getUsername()+ "'s Profile");
	}
	public void changeUsername(String newName)
	{
		File file = new File("Users.txt");	
		File scoreFile = new File("Scores.txt");
		FileWriter writer;
		FileWriter scoreWriter;
		ArrayList<String> userData = new ArrayList<String>();
		ArrayList<String> scoreData = new ArrayList<String>();
		ListIterator<String> iterator;
		ListIterator<String> stringIterator;
		
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
					iterator.set(newName);
					break;
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
			JOptionPane.showMessageDialog(null, "Could not find Users. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Could not update Username. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}	
		
		try
		{
			Scanner s = new Scanner(scoreFile);
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
					iterator.set(newName);
					break;
				}
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
			JOptionPane.showMessageDialog(null, "Could not update Username in Scores. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void changePassword(String newName)
	{
		File file = new File("Users.txt");	
		FileWriter writer;
		ArrayList<String> userData = new ArrayList<String>();
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
					iterator.next();
					iterator.set(newName);
					break;
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
			JOptionPane.showMessageDialog(null, "Could not find Users. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Could not update Password. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
	public boolean checkPassword(String password)
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
			JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	boolean alreadyInFile(String name)
	{
		File file = new File("Users.txt");
		Scanner scanner;
		try 
		{
			scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
			   String lineFromFile = scanner.nextLine();
			   if(lineFromFile.contains(name))
			   { 
				  JOptionPane.showMessageDialog(null, "Username has already been chosen. Pick another.", "Error", JOptionPane.ERROR_MESSAGE);
				  scanner.close();
				  return true;
			   }
			   
			}
			scanner.close();
			return false;
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "System Error: Contact administrator.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	boolean checkUsername(String username)
	{
		if(username.length() < 4 || username.length() > 10)
		{
			JOptionPane.showMessageDialog(null, "Username must be between 4 to 10 characters long", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
		{
			if(alreadyInFile(username))
				return false;
		}
				
		return true;
	}
	boolean checkPassword(String password, String confirmPassword)
	{
		if(password.length() < 8 || password.length() > 16)
		{
			JOptionPane.showMessageDialog(null, "New password must be between 8 to 16 characters long", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(!password.equals(confirmPassword))
		{
			JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
				
		return true;
	}
}
