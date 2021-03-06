/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/*
 *
 * MainMenu.java is a class that updates, maintains, and
 * displays a main menu. This class gives most of the 
 * functionality for the user to enter the game, view
 * the leader board, etc.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class MainMenu extends JFrame {
	private int difficulty = 0, size = 0;
	private User user;
	
    /**
	 * Constructor
	 * @param u  sets the User of the game
     * @param width is the width of the panel
     * @param height is the height of the panel
	 */
	public MainMenu(int width, int height, User u) {
		user = u;
	    this.setSize(width, height);
	    this.setLayout(new GridLayout(1,1));
		JButton instructions =new JButton("Instructions");
		JButton playGame =new JButton("Play New Game");
		JButton savedGame =new JButton("Play Saved Game");
		JButton viewLeaderBoard = new JButton("View Leader Board");
		JButton viewProfile = new JButton("View Profile");
		JButton logout = new JButton("Log Out");
	    JPanel buttonLayout = new JPanel();
	    buttonLayout.setLayout(new GridLayout(7,1));
	    JPanel layout = new JPanel();
	    layout.setSize(width,height);
	    layout.setLayout(new BorderLayout());
	    JTextPane welcome = new JTextPane();
	    welcome.setEditable(false);
	    StyledDocument doc1 = (StyledDocument) welcome.getDocument();
    	SimpleAttributeSet titleFont = new SimpleAttributeSet();
    	StyleConstants.setFontFamily(titleFont, "Serif");
    	StyleConstants.setFontSize(titleFont, 42);
    	StyleConstants.setForeground(titleFont, Color.blue);
    	StyleConstants.setAlignment(titleFont,StyleConstants.ALIGN_CENTER);
    	try {
			doc1.insertString(doc1.getLength(), "\t\t\t  CSE360 Sudoku Main Menu", titleFont);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    this.add(layout);
	    layout.add(buttonLayout, BorderLayout.CENTER);
	    buttonLayout.add(welcome);
	    buttonLayout.add(instructions);
	    buttonLayout.add(playGame);
	    buttonLayout.add(savedGame);
	    buttonLayout.add(viewLeaderBoard);
	    buttonLayout.add(viewProfile);
	    buttonLayout.add(logout);
	    instructions.addActionListener(new ActionListener() {
            
            /**
			 * Action event after instructions button is pressed,
			 * where the instructions menu will display for the user.
			 * @param ae begins Action Event
			 */
		    public void actionPerformed(ActionEvent ae) {
		    	// Add instruction page here
		    	String s = "Sudoku is played over a 9x9 grid, divided to 3x3 sub grids called regions. Sudoku begins with some of the grid cells already filled with numbers. The object of Sudoku is to fill the other empty cells with numbers between 1 and 9 (1 number only in each cell) according the following guidelines:\n\n1.)\tA number can only appear once in each row.\n2.)\tA number can only appear once in each column.\n3.)\tA number can only appear once in each 3 x 3 region.\n\nTo win the game, select the blank entries of the sudoku puzzle, type in the number that must be placed in that area to avoid violating the rules. Once all values are placed and none of the rules are violated, then the game is over.";
		    	JTextPane instructs = new JTextPane();
		    	instructs.setEditable(false);
		    	StyledDocument doc = (StyledDocument) instructs.getDocument();
		    	SimpleAttributeSet normal = new SimpleAttributeSet();
		    	StyleConstants.setFontFamily(normal, "SansSerif");
		    	StyleConstants.setFontSize(normal, 16);
		    	SimpleAttributeSet boldBlue = new SimpleAttributeSet(normal);
		    	StyleConstants.setBold(boldBlue,true);
		    	StyleConstants.setForeground(boldBlue,Color.blue);
		     	try {
					doc.insertString(doc.getLength(), s, normal);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     	JFrame instructionFrame = new JFrame();
		     	instructionFrame.setTitle("Sudoku Instructions");
			    instructionFrame.setVisible(true);
			    instructionFrame.add(instructs);
			    instructionFrame.setSize(500,500);
	    	}
	    });
	    playGame.addActionListener(new ActionListener() {
            
            /**
			 * Action listener after playGame button is pressed,
             * where a game will be started with the necessary parameter.
			 * @param ae begins Action Event
			 */
	    	public void actionPerformed(ActionEvent ae) {
	    		selectBoard();
	    	}
	    });
	    savedGame.addActionListener(new ActionListener() {
            
            /**
			 * Action listener after savedGame button is pressed,
			 * where the user's last saved game will be pulled up for
			 * the user to play. If the user doesn't have a saved game,
			 * it will print an error message.
			 * @param ae begins Action Event
			 */
	    	public void actionPerformed(ActionEvent ae) {
	    		if(user.isGameSaved()) {
	    			if(findSavedGameSize().equals("9x9")) {
			    		SavedGame board = new SavedGame(1000,850, user);
			    		board.setVisible(true);
			    		board.setTitle("Saved Sudoku Puzzle");
			    		board.setSize(1000,850);
			    		board.setResizable(false);
			    		dispose();
	    			}
	    			else {
	    				SavedGame16x16 board = new SavedGame16x16(1000,850, user);
			    		board.setVisible(true);
			    		board.setTitle("Saved Sudoku Puzzle");
			    		board.setSize(1100,950);
			    		board.setResizable(false);
			    		dispose();
	    			}
	    		}
	    		else {
	    			 JOptionPane.showMessageDialog(null, user.getUsername()+" has no saved game.","Error", JOptionPane.ERROR_MESSAGE);
                }
	    	}
	    });
	    viewLeaderBoard.addActionListener(new ActionListener() {
            
            /**
             * Action listener after viewLeaderBoard button is pressed,
             * where the systems leader board will be pulled up for
             * the user to see.
             * @param ae begins Action Event
             */
	    	public void actionPerformed(ActionEvent ae) {
	    		LeaderBoard scoreBoard = new LeaderBoard(user);
	    		scoreBoard.setSize(800,600);
	    		scoreBoard.setVisible(true);
	    		scoreBoard.setTitle("CSE 360 Sudoku Leader Board");
	    		dispose();
	    	}
	    });
	    viewProfile.addActionListener(new ActionListener() {
            
            /**
             * Action listener after viewProfile button is pressed,
             * where the user's profile will be pulled up for
             * the user to see.
             * @param ae begins Action Event
             */
	    	public void actionPerformed(ActionEvent ae){
	    		// Add code to launch window with the user profile info and options
			    Profile profile = new Profile(user);
			    profile.setVisible(true);
			    profile.setTitle(user.getUsername() +"'s Profile");
	    		profile.setSize(700,150);	
	    		dispose();
	    	}
	    });
	    logout.addActionListener(new ActionListener() {
            
            /**
			 * Action listener after logout button is pressed,
			 * where a logout prompt is displayed and the user 
             * logged out.
			 * @param ae begins Action Event
			 */
	    	public void actionPerformed(ActionEvent ae){
	    		int reply = JOptionPane.showConfirmDialog(null, "Are you sure that want to quit?");
	    		LogIn log_in;
	    		if(reply == JOptionPane.YES_OPTION) {
		    		log_in = new LogIn(400,150);
		    		log_in.setVisible(true);
		    		log_in.setTitle("CSE360 Sudoku Login");
		    		log_in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    		dispose();
	    		}
	    	}
	    });
	    //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
    
    /**
     * This method selects the board that the user picks.
     */
	public void selectBoard() {
		Select_Board selectBoard = new Select_Board(900,300, user);
		dispose();
	}
    
    /**
     * This method finds the size of a saved game for the user.
     * It returns an error if the puzzle could not be loaded.
     * @return String that is either "9x9" or "16x16"
     */
    public String findSavedGameSize() {
		File file;
		file = new File("Saved_Games.txt");
		Scanner scanner;
		String line;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				if(line.equals(user.getUsername())) {
					scanner.nextLine();
					return scanner.nextLine();
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load puzzle. Contact system administrator.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return "";
	}
}