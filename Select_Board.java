/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
 *
 * Select_Board.java allows the user to choose a type
 * of Sudoku board, as they can choose between easy,
 * medium, hard, and evil difficulties and a 9x9 or
 * 16x16 size. They can also choose whether or not
 * they want to play with an AI.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class Select_Board extends JFrame {
	private int difficulty = 0;
	private int size = 0;
	private int AI_yes_or_no= 0;
	private User user;
	private RadioListener list0;
	private RadioListener list1;
	private RadioListener list2;

	/**
	 * Action Listener Class that implements the radio listener
	 * for the buttons that choose the type of puzzle for the
	 * user.
	 *
	 * @version 0
	 * @author Garrett Gutierrez
	 */
	class RadioListener implements ActionListener {

		/**
		 * Implements a radio listener for all of the
		 * buttons to choose the puzzle type
		 * @param e begins Action Event
		 */
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
				case "Yes":
					AI_yes_or_no = 1;
					break;
				case "No":
					AI_yes_or_no = 2;
					break;
				case "Easy":
					difficulty = 1;
					break;
				case "Medium":
					difficulty = 2;
					break;
				case "Hard":
					difficulty = 3;
					break;
				case "Evil":
					difficulty = 4;
					break;
				case "9x9":
					size = 1;
					break;
				case "16x16":
					size = 2;
					break;
				default:
					break;
			}
			if((difficulty != 0) && (size != 0) && (AI_yes_or_no != 0)) {
				if(AI_yes_or_no == 2) {
					if(size == 1) {
						SudokuBoard board;
						switch(difficulty) {
							case 1:
								board = new SudokuBoard(1000, 850, "Easy", user);
								board.setTitle("Easy Sudoku Puzzle");
								break;
							case 2:
								board = new SudokuBoard(1000, 850, "Medium", user);
								board.setTitle("Medium Sudoku Puzzle");
								break;
							case 3:
								board = new SudokuBoard(1000, 850, "Hard", user);
								board.setTitle("Hard Sudoku Puzzle");
								break;
							default:
								board = new SudokuBoard(1000, 850, "Evil", user);
								board.setTitle("Evil Sudoku Puzzle");
								break;
						}
						board.setVisible(true);
						board.setTitle("Sudoku Puzzle");
						board.setSize(1000, 850);
						board.setResizable(false);
						diposeFrame();
					} else {
						SudokuBoard16x16 board;
						switch(difficulty) {
							case 1:
								board = new SudokuBoard16x16(1000, 850, "Easy", user);
								board.setTitle("Easy Sudoku Puzzle");
								break;
							case 2:
								board = new SudokuBoard16x16(1000, 850, "Medium", user);
								board.setTitle("Medium Sudoku Puzzle");
								break;
							case 3:
								board = new SudokuBoard16x16(1000, 850, "Hard", user);
								board.setTitle("Hard Sudoku Puzzle");
								break;
							default:
								board = new SudokuBoard16x16(1000, 850, "Evil", user);
								board.setTitle("Evil Sudoku Puzzle");
								break;
						}
						board.setVisible(true);
						board.setTitle("Sudoku Puzzle");
						board.setSize(1100, 950);
						board.setResizable(false);
						diposeFrame();
					}
				} else {
					if(size == 1) {
						SudokuBoard board;
						switch(difficulty) {
							case 1:
								board = new AIBoard(1000, 850, "Easy", user);
								board.setTitle("Easy Sudoku Puzzle");
								break;
							case 2:
								board = new AIBoard(1000, 850, "Medium", user);
								board.setTitle("Medium Sudoku Puzzle");
								break;
							case 3:
								board = new AIBoard(1000, 850, "Hard", user);
								board.setTitle("Hard Sudoku Puzzle");
								break;
							default:
								board = new AIBoard(1000, 850, "Evil", user);
								board.setTitle("Evil Sudoku Puzzle");
								break;
						}
						board.setVisible(true);
						board.setTitle("Sudoku Puzzle");
						board.setSize(1000, 850);
						board.setResizable(false);
						diposeFrame();
					} else {
						SudokuBoard16x16 board;
						switch(difficulty) {
							case 1:
								board = new AIBoard16x16(1000, 850, "Easy", user);
								board.setTitle("Easy Sudoku Puzzle");
								break;
							case 2:
								board = new AIBoard16x16(1000, 850, "Medium", user);
								board.setTitle("Medium Sudoku Puzzle");
								break;
							case 3:
								board = new AIBoard16x16(1000, 850, "Hard", user);
								board.setTitle("Hard Sudoku Puzzle");
								break;
							default:
								board = new AIBoard16x16(1000, 850, "Evil", user);
								board.setTitle("Evil Sudoku Puzzle");
								break;
						}
						board.setVisible(true);
						board.setTitle("Sudoku Puzzle");
						board.setSize(1100, 950);
						board.setResizable(false);
						diposeFrame();
					}
				}
			}
		}
    }

	/**
	 * Constructor
	 * @param width sets the width of the GUI
	 * @param height sets the height of the GUI
	 * @param u  sets the User of the game
	 */
	public Select_Board(int width, int height, User u)
	{
		user = u;
		JPanel mainPanel = new JPanel();
		JPanel AIChoicePanel = new JPanel();
		JPanel selection1 = new JPanel();
		JPanel selection2 = new JPanel();
		list0 = new RadioListener();
		list1 = new RadioListener();
		list2 = new RadioListener();
		ButtonGroup AIChoice = new ButtonGroup();
		JRadioButton AIYes = new JRadioButton("Yes");
		AIYes.setActionCommand("Yes");
		AIYes.addActionListener(list0);
		JRadioButton AINo = new JRadioButton("No");
		AINo.setActionCommand("No");
		AINo.addActionListener(list0);
		AIChoice.add(AIYes);
		AIChoice.add(AINo);
		ButtonGroup difficulties = new ButtonGroup();
		JRadioButton easy = new JRadioButton("Easy");
		easy.setActionCommand("Easy");
		easy.addActionListener(list1);
		JRadioButton medium = new JRadioButton("Medium");
		medium.setActionCommand("Medium");
		medium.addActionListener(list1);
		JRadioButton hard = new JRadioButton("Hard");
		hard.setActionCommand("Hard");
		hard.addActionListener(list1);
		JRadioButton evil = new JRadioButton("Evil");
		evil.setActionCommand("Evil");
		evil.addActionListener(list1);
		difficulties.add(easy);
		difficulties.add(medium);
		difficulties.add(hard);
		difficulties.add(evil);
		ButtonGroup sizes = new ButtonGroup();
		JRadioButton ninebynine = new JRadioButton("9x9");
		ninebynine.setActionCommand("9x9");
		ninebynine.addActionListener(list2);
		JRadioButton sixteenbysixteen= new JRadioButton("16x16");
		sixteenbysixteen.setActionCommand("16x16");
		sixteenbysixteen.addActionListener(list2);
		sizes.add(ninebynine);
		sizes.add(sixteenbysixteen);
		JLabel pickAI = new JLabel("Play with AI?");
		JLabel pickDifficulty = new JLabel("Choose difficulty of puzzle");
		JLabel pickSize = new JLabel("Choose size of the puzzle");
		JButton mainMenu = new JButton("Back to Main Menu");
		mainMenu.addActionListener(new ActionListener() {

			/**
			 * Action Event after mainMenu button is pressed,
			 * where it will send the user back to the main menu
			 * @param ae begins Action Event
			 */
			public void actionPerformed(ActionEvent ae) {
					MainMenu menu = new MainMenu(1000, 800, user);
					menu.setSize(1000, 800);
					menu.setVisible(true);
					menu.setTitle("CSE360 Sudoku Main Menu");
					dispose();
			}
		});
		mainPanel.setLayout(new GridLayout(6, 1));
		this.setTitle("Choose Type of Puzzle");
		this.setVisible(true);
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		mainPanel.add(pickAI);
		mainPanel.add(AIChoicePanel);
		mainPanel.add(pickDifficulty);
		mainPanel.add(selection1);
		mainPanel.add(pickSize);
		mainPanel.add(selection2);
		AIChoicePanel.add(AIYes);
		AIChoicePanel.add(AINo);
		selection1.add(easy);
		selection1.add(medium);
		selection1.add(hard);
		selection1.add(evil);
		selection2.add(ninebynine);
		selection2.add(sixteenbysixteen);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(mainMenu, BorderLayout.SOUTH);
	}

	/**
	 * Disposes the frame of the board
	 */
	void diposeFrame() {
		dispose();
	}
}
