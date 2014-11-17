import javax.swing.*;

import java.awt.*;
import java.util.*;

public class CSE360Sudoku extends JApplet
{
	// The init() method is the starting point of the JApplet. Here is where the log-in screen is created.
	public void init()
	{
		LogIn log_in = new LogIn(400,150);
		log_in.setVisible(true);
		log_in.setTitle("CSE360 Sudoku Login");
		log_in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane();
		this.setLayout(new GridLayout(1,1));

	}
}
