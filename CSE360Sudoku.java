import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CSE360Sudoku extends JApplet
{
	private final int length = 400, width = 150;
	private LogIn log_in;
	
	public void init()
	{
		log_in = new LogIn(length,width);
		log_in.setVisible(true);
		log_in.setTitle("CSE360 Sudoku Login");
		log_in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane();
		this.setLayout(new GridLayout(1,1));
	}
}
