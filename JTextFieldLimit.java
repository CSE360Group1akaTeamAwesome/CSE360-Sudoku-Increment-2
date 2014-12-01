/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import java.awt.*;
import java.awt.event.*;

import javax.print.attribute.AttributeSet;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/*
 * JTextFieldLimit.java is used to set the limit on the size of 
 * the text fields and provide an insertString method.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class JTextFieldLimit extends PlainDocument {
	private int limit;
    
      /**
	   * Constructor
	   * @param limit sets the limit on the length
       * @param upper sets whether the upper limit has been met
       *
       * This class has overloaded constructors that take either an int or an int and a boolean as parameters
       * 
       */
     
	  JTextFieldLimit (int limit){
	    super();
	    this.limit = limit;
	  }

	  JTextFieldLimit (int limit, boolean upper){
	    super();
	    this.limit = limit;
	  }
    /**
	 * InsertString
     * Inserts the text into the box
	 * @param offset is the offset for where it is placed
     * @param str is the string to be inserted
     * @param attr is the attribute set for the text
     */
	  public void insertString (int offset, String str, javax.swing.text.AttributeSet attr) throws BadLocationException {
	    if (str == null)
	      return;

	    if ((getLength() + str.length()) <= limit){
	      super.insertString(offset, str, attr);
	    }
	  }
	}
