/*
 *
 * %W% %E% Garrett Gutierrez
 * Copyright (c) 2014.
 *
 */
import javax.swing.JOptionPane;

/*
 *
 * Score.java is used to calculate a User's score when they
 * have successfully completed a Sudoku puzzle. This class
 * uses User time, number of hints, puzzle difficulty, and
 * puzzle size to calculate the score.
 *
 * @version 0 2014
 * @author Garrett Gutierrez
 *
 */
public class Score {
	private int 	best_time;
	private int 	number_of_hints;
	private double 	high_score;
	private double 	current_score;
	private int 	current_time;
	private String 	best_difficulty;
	private String 	best_size;
	private String 	last_difficulty;
	private String 	last_size;

	/**
	 * Constructor
	 */
	public Score() {
		best_time = 0;
		number_of_hints = 0;
		high_score = 0;
		current_score = 0;
		current_time = 0;
		best_difficulty = "";
		best_size = "";
		last_difficulty = "";
		last_size = "";
	}

	/**
	 * Mutator method sets the class variable number_of_hints to a value
	 * @param hints sets class variable number_of_hints
	 */
	public void setNumberOfHints(int hints) {
		number_of_hints = hints;
	}

	/**
	 * Mutator method sets the class variable last_difficulty to a value
	 * @param difficulty sets class variable last_difficulty
	 */
	public void setLastDifficulty(String difficulty) {
		last_difficulty = difficulty;
	}

	/**
	 * Mutator method sets the class variable last_size to a value
	 * @param size sets class variable last_size
	 */
	public void setLastSize(String size) {
		last_size = size;
	}

	/**
	 * Mutator method sets the class variable current_time to a value
	 * @param time sets class variable current_time
	 */
	public void setCurrentTime(int time) {
		current_time = time;
	}

	/**
	 * Accessor method returns class variable number_of_hints
	 * @return number_of_hints
	 */
	public int getNumberOfHints() {
		return number_of_hints;
	}

	/**
	 * Accessor method returns class variable high_score
	 * @return high_score
	 */
	public  double getHighScore() {
		return high_score;
	}

	/**
	 * Accessor method returns class variable best_time
	 * @return best_time
	 */
	public int getBestTime() {
		return best_time;
	}

	/**
	 * Accessor method returns class variable best_difficulty
	 * @return best_difficulty
	 */
	public String getBestDifficulty() {
		return best_difficulty;
	}

	/**
	 * Accessor method returns class variable best_size
	 * @return best_size
	 */
	public String getBestSize() {
		return best_size;
	}

	/**
	 * Accessor method returns class variable current_score
	 * @return current_score
	 */
	public double getCurrentScore() {
		return current_score;
	}

	/**
	 * Accessor method returns class variable current_time
	 * @return current_size
	 */
	public int getCurrentTime() {
		return current_time;
	}

	/**
	 * Accessor method returns class variable last_difficulty
	 * @return last_difficulty
	 */
	public String getLastDifficulty() {
		return last_difficulty;
	}

	/**
	 * Accessor method returns class variable last_size
	 * @return last_size
	 */
	public String getLastSize() {
		return last_size;
	}

	/**
	 * Uses arithmetic and class variables to determine the score
	 * of the Sudoku puzzle using a scoring algorithm. The algorithm
	 * is based off of puzzle difficulty, puzzle size, number of
	 * hints, and time to complete the puzzle.
	 */
	public void calculateScore() {
		double basePoints = 750.0;
		double multiplier = 1.0;

		if(last_difficulty.equals("Easy")) {
			multiplier = 1.0;
		} else if(last_difficulty.equals("Medium")) {
			multiplier = 1.5;
		} else if(last_difficulty.equals("Hard")) {
			multiplier = 2.0;
		} else if(last_difficulty.equals("Evil")) {
			multiplier = 4.0;
		}

		if(last_size.equals("9x9")) {
			basePoints = 750.0;
		} else if(last_size.equals("16x16")) {
			basePoints = 1000.0;
		}

		current_score = (float) ((((480 / current_time) * basePoints) * multiplier) - (number_of_hints * 5.0));
		updateHighScore();
	}

	/**
	 * Updates the highscore of the User if their new score
	 * after completing a puzzle is higher than old scores
	 */
	public void updateHighScore() {
		if(high_score < current_score) {
			high_score = current_score;
			best_time = current_time;
			best_difficulty = last_difficulty;
			best_size = last_size;
			JOptionPane.showMessageDialog(null, "New High Score", "High Score Updated", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Loads the score of the User by setting class variables to values
	 * @param highScore sets class variable high_score
	 * @param bestTime sets class variable best_time
	 * @param bestDifficulty sets class variable best_difficulty
	 * @param bestSize sets class variable best_size
	 * @param lastScore sets class variable current_score
	 * @param lastTime sets class variable current_time
	 * @param lastDifficulty sets class variable last_difficulty
	 * @param lastSize sets class variable last_size
	 */
	public void loadScore(double highScore, int bestTime, String bestDifficulty, String bestSize,
							double lastScore, int lastTime, String lastDifficulty, String lastSize) {
		high_score = highScore;
		best_time = bestTime;
		best_difficulty = bestDifficulty;
		best_size = bestSize;
		current_score = lastScore;
		current_time = lastTime;
		last_difficulty = lastDifficulty;
		last_size = lastSize;
	}

	/**
	 * Displays latest stats to the User, including difficulty,
	 * size, score, time, and last puzzle stats
	 */
	public void displayLatestStats() {
		JOptionPane.showMessageDialog(null, "Difficulty: " + last_difficulty + "\nSize: "
										+ last_size + "\nScore: " + current_score + "\nTime: "
										+ current_time, "Last Puzzle Stats", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays the best stats to the User, including difficulty,
	 * sie, high score, time, and best score stats
	 */
	public void displayBestStats() {
		JOptionPane.showMessageDialog(null, "Difficulty: " + best_difficulty + "\nSize: "
										+ best_size + "\nHigh Score: " + high_score + "\nTime: "
										+ best_time, "Best Score Stats", JOptionPane.INFORMATION_MESSAGE);
	}
	//public void showLeaderBoard();
}
