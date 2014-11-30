/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */

 /*
  *
  * User.java is used to keep track of a user's information.
  * This includes their saved games, their username, and their
  * scores.
  *
  * @version 0 2014
  * @author Garrett Gutierrez
  *
 */
public class User {
	private String username;
	private Score userScore;
	private SudokuBoard savedGame;
	private boolean hasSavedGame;
	private String savedGameSize;

	/**
	 * Constructor
	 * @param name sets the name of the user
	 * @param game sets up the puzzle for the user
	 * @param score sets the score of the user
	 */
	public User(String name, SudokuBoard game, Score score) {
		username = name;
		savedGame = game;
		userScore = score;
		hasSavedGame = false;
		savedGameSize = "";
	}

	/**
	 * Mutator method sets the username of the user to a value
	 * @param newName sets class variable username
	 */
	public void setUsername(String newName) {
		// Need to change username in file to update
		username = newName;
	}
	//public void setScore(Score newScore);

	/**
	 * Mutator method sets the class variable savedGame to a value
	 * @param game sets class variable savedGame
	 */
	public void setSavedGame(SudokuBoard game) {
		savedGame = game;
	}

	/**
	 * Mutator method sets the class variable userScore to a value
	 * @param score sets class variable userScore
	 */
	public void setScore(Score score) {
		userScore = score;
	}

	/**
	 * Mutator method sets the class variable hasSavedGame to a value
	 * @param bool sets class variable hasSavedGame
	 */
	public void setHasSavedGame(boolean bool) {
		hasSavedGame = bool;
	}
	/**
	 * Mutator method sets the class variable savedGameSize to a value
	 * @param size sets class variable savedGameSize
	 */
	public void setSavedGameSize(String size) {
		savedGameSize = size;
	}

	/**
	 * Accessor method returns class variable username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Accessor method returns class variable savedGame
	 * @return savedGame
	 */
	public SudokuBoard getSavedGame() {
		return savedGame;
	}

	/**
	 * Accessor method returns class variable userScore
	 * @return userScore
	 */
	public Score getScore() {
		return userScore;
	}

	/**
	 * Accessor method returns class variable savedGameSize
	 * @return savedGameSize
	 */
	public String getSavedGameSize() {
		return savedGameSize;
	}

	/**
	 * Accessor method returns class variable hasSavedGame
	 * @return hasSavedGame
	 */
	public boolean isGameSaved() {
		return hasSavedGame;
	}
	//public void display_highscore()
	// Should show time, score, size, and difficulty
}
