import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

public class Sign_Up extends JFrame
{
	private JPanel mainPanel;
	private JLabel promptUsername, promptPassword, promptConfirmPassword;
	private JTextField usernameField;
	private JPasswordField passwordField, confirmPasswordField;
	private JButton submit;

	public Sign_Up(int width, int height)
	{
		this.setSize(width, height);
		this.setVisible(true);
		this.setTitle("Create an Account");
		this.setLayout(new FlowLayout());
		
		mainPanel = new JPanel();
		mainPanel.setSize(width,height);
		mainPanel.setLayout(new GridLayout(3,2));
		this.add(mainPanel);
		
		promptUsername = new JLabel("Enter username between 4-10 characters");
		mainPanel.add(promptUsername);
		
		usernameField = new JTextField();
		mainPanel.add(usernameField);
		
		promptPassword = new JLabel("Enter password between 8-16 characters");
		mainPanel.add(promptPassword);
		
		passwordField = new JPasswordField(16);
		mainPanel.add(passwordField);
		
		promptConfirmPassword = new JLabel("Confirm password");
		mainPanel.add(promptConfirmPassword);
		
		confirmPasswordField = new JPasswordField(16);
		mainPanel.add(confirmPasswordField);
		
		submit = new JButton("Submit");
		this.add(submit);
		submit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
			   if(checkUsername(usernameField.getText()) && checkPassword(new String(passwordField.getPassword()),new String(confirmPasswordField.getPassword())) )
			   {
				   File file = new File("Users.txt");
				   try {
						if (!file.exists()) {
							file.createNewFile();
						}
			 
						FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(usernameField.getText());
						bw.write("\n");
						bw.write(new String(passwordField.getPassword()));
						bw.write("\n\n");
						bw.close();
			 
					} 
				   catch (IOException e)
				   {
					   System.out.println("Could not find user file");
				   }
				   
				   
				 int option = JOptionPane.showConfirmDialog(null, "Would you like to return to main menu?","User Successfully Created", JOptionPane.YES_NO_OPTION);
				 if(option == JOptionPane.YES_OPTION)
				 {
					User user = new User(usernameField.getText(),null,new Score());
					user.setHasSavedGame(false);
					MainMenu menu = new MainMenu(1000,800, user);				
					menu.setSize(1000,800);
					menu.setVisible(true);
				    menu.setTitle("CSE360 Sudoku Main Menu");
					dispose();
				 }
				 else
				 {
					dispose(); 
				 }
			   }
			}
		});
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
			JOptionPane.showMessageDialog(null, "Password must be between 8 to 16 characters long", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(!password.equals(confirmPassword))
		{
			JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
				
		return true;
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
}
	

