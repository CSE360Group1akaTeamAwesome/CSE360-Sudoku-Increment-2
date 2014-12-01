/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

/*
 *
 * CSE360Sudoku.java is a class that initializes the JApplet.
 * It also creates the login screen.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class CSE360Sudoku extends JApplet {
	/**
     * The init() method is the starting point of the JApplet. 
     * Here is where the log-in screen is created. 
     */
	public void init () {
		LogIn log_in = new LogIn(400,150);
		log_in.setVisible(true);
		log_in.setTitle("CSE360 Sudoku Login");
		log_in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane();
		this.setLayout(new GridLayout(1,1));

	}
}
