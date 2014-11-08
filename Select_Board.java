import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class Select_Board extends JFrame
{
	private int difficulty = 0, size = 0;
	private JPanel mainPanel,selection1, selection2;
	private JRadioButton easy, medium, hard, evil,ninebynine,sixteenbysixteen;
	private ButtonGroup difficulties, sizes;
	private JLabel pickDifficulty, pickSize;
	private JButton mainMenu;
	private User user;
	
	class RadioListener implements ActionListener 
    { 
		public void actionPerformed(ActionEvent e)
		{
			
			switch (e.getActionCommand())
			{
				case "Easy": difficulty = 1; break;
				case "Medium": difficulty = 2; break;
				case "Hard": difficulty = 3; break;
				case "Evil": difficulty = 4; break;
				case "9x9": size = 1; break;
				case "16x16": size = 2; break;
				default: break;
			
			}
			if(difficulty != 0 && size != 0)
			{
				if(size == 1)
				{
					SudokuBoard board;
					switch(difficulty)
					{
						case 1:
							board = new SudokuBoard(1000,850, "Easy", user);
							board.setTitle("Easy Sudoku Puzzle");
							break;	
						case 2:
							board = new SudokuBoard(1000,850, "Medium", user);
							board.setTitle("Medium Sudoku Puzzle");
							break;
						case 3:
							board = new SudokuBoard(1000,850, "Hard",user);
							board.setTitle("Hard Sudoku Puzzle");
							break;
						default:
							board = new SudokuBoard(1000,850, "Evil",user);
							board.setTitle("Evil Sudoku Puzzle");
							break;
					}
					board.setVisible(true);
					board.setTitle("Sudoku Puzzle");
					board.setSize(1000,850);
					board.setResizable(false);
					diposeFrame();
				}
				else
				{
					SudokuBoard16x16 board;
					switch(difficulty)
					{
						case 1:
							board = new SudokuBoard16x16(1000,850, "Easy", user);
							board.setTitle("Easy Sudoku Puzzle");
							break;	
						case 2:
							board = new SudokuBoard16x16(1000,850, "Medium", user);
							board.setTitle("Medium Sudoku Puzzle");
							break;
						case 3:
							board = new SudokuBoard16x16(1000,850, "Hard",user);
							board.setTitle("Hard Sudoku Puzzle");
							break;
						default:
							board = new SudokuBoard16x16(1000,850, "Evil",user);
							board.setTitle("Evil Sudoku Puzzle");
							break;
					}	
					board.setVisible(true);
					board.setTitle("Sudoku Puzzle");
					board.setSize(1100,950);
					board.setResizable(false);
					diposeFrame();
				}
			}
	
		}
    }
	private RadioListener list1, list2;
	public Select_Board(int width, int height, User u)
	{
		user = u;
		mainPanel = new JPanel();
		selection1 = new JPanel();
		selection2 = new JPanel();
		list1 = new RadioListener();
		list2 = new RadioListener();
		
		difficulties = new ButtonGroup();
		easy = new JRadioButton("Easy");
		easy.setActionCommand("Easy");
		easy.addActionListener(list1);
		medium = new JRadioButton("Medium");
		medium.setActionCommand("Medium");
		medium.addActionListener(list1);
		hard = new JRadioButton("Hard"); 
		hard.setActionCommand("Hard");
		hard.addActionListener(list1);
		evil = new JRadioButton("Evil");
		evil.setActionCommand("Evil");
		evil.addActionListener(list1);
		difficulties.add(easy);
		difficulties.add(medium);
		difficulties.add(hard);
		difficulties.add(evil);
		
		sizes = new ButtonGroup();
		ninebynine = new JRadioButton("9x9");
		ninebynine.setActionCommand("9x9");
		ninebynine.addActionListener(list2);
		sixteenbysixteen= new JRadioButton("16x16");
		sixteenbysixteen.setActionCommand("16x16");
		sixteenbysixteen.addActionListener(list2);
		sizes.add(ninebynine);
		sizes.add(sixteenbysixteen);
		
		pickDifficulty = new JLabel("Choose difficulty of puzzle");
		pickSize = new JLabel("Choose size of the puzzle");
		mainMenu = new JButton("Back to Main Menu");
		mainMenu.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae)
		{
				MainMenu menu = new MainMenu(1000,800, user);
				menu.setSize(1000,800);
				menu.setVisible(true);
			    menu.setTitle("CSE360 Sudoku Main Menu");
				dispose();
		}});
		mainPanel.setLayout(new GridLayout(4,1));
		mainPanel.setSize(500,100);
		this.setTitle("Choose Type of Puzzle");
		this.setVisible(true);
		this.setSize(500,200);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		mainPanel.add(pickDifficulty);
		mainPanel.add(selection1);
		mainPanel.add(pickSize);
		mainPanel.add(selection2);
		selection1.add(easy);
		selection1.add(medium);
		selection1.add(hard);
		selection1.add(evil);
		selection2.add(ninebynine);
		selection2.add(sixteenbysixteen);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(mainMenu, BorderLayout.SOUTH);
	
	}
	void diposeFrame()
	{
		dispose();		
	}
}
