import javax.swing.JOptionPane;


public class Score
{
	private int best_time;
	private int number_of_hints;
	private double high_score, current_score;
	private int current_time;
	private String best_difficulty, best_size, last_difficulty, last_size;
	
	public Score()
	{
		best_time = 0;
		number_of_hints= 0;
		high_score = 0;
		current_score = 0;
		current_time = 0;
		best_difficulty = "";
		best_size = "";
		last_difficulty = "";
		last_size = "";
	}
	
	public void setNumberOfHints(int hints)
	{
		number_of_hints = hints;
	}
	public void setLastDifficulty(String difficulty)
	{
		last_difficulty = difficulty;
	}
	public void setLastSize(String size)
	{
		last_size = size;
	}
	public  double getHighScore()
	{
		return high_score;
	}
	public int getBestTime()
	{
		return best_time;
	}
	public String getBestDifficulty()
	{
		return best_difficulty;
	}
	public String getBestSize()
	{
		return best_size;
	}
	public double getCurrentScore()
	{
		return current_score;
	}
	public int getCurrentTime()
	{
		return current_time;
	}
	public String getLastDifficulty()
	{
		return last_difficulty;
	}
	public String getLastSize()
	{
		return last_size;
	}
	public void calculateScore()
	{
		double basePoints = 750.0, multiplier = 1.0;
		if(last_difficulty.equals("Easy"))
			multiplier = 1.0;
		else if(last_difficulty.equals("Medium"))
			multiplier = 1.5;
		else if(last_difficulty.equals("Hard"))
			multiplier = 2.0;
		else if(last_difficulty.equals("Evil"))
			multiplier = 4.0;
		
		if(last_size.equals("9x9"))
			basePoints = 750.0;
		else if(last_size.equals("16x16"))
			basePoints = 1000.0;
		
		current_score = (float) ((( (current_time/480.0) * basePoints ) * multiplier ) - ( number_of_hints * 5.0 ) );
		updateHighScore();
	}
	
	public void setCurrentTime(int time)
	{
		current_time = time;
	}
	
	public void updateHighScore()
	{
		if(high_score < current_score)
		{
			high_score = current_score;
			best_time = current_time;
			best_difficulty = last_difficulty;
			best_size = last_size;
			JOptionPane.showMessageDialog(null,"New High Score","High Score Updated",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void loadScore(double highScore, int bestTime, String bestDifficulty, String bestSize)
	{
		high_score = highScore;
		best_time = bestTime;
		best_difficulty = bestDifficulty;
		best_size = bestSize;
	}
	public void displayLatestStats()
	{
		JOptionPane.showMessageDialog(null,"Difficulty: " + last_difficulty + "\nSize: " + last_size + "\nScore: " + current_score + "\nTime: " + current_time,"Last Puzzle Stats",JOptionPane.INFORMATION_MESSAGE);
	}
	public void displayBestStats()
	{
		JOptionPane.showMessageDialog(null,"Difficulty: " + best_difficulty + "\nSize: " + best_size + "\nHigh Score: " + high_score + "\nTime: " + best_time,"Best Score Stats",JOptionPane.INFORMATION_MESSAGE);
	}
	//public void showLeaderBoard();
}