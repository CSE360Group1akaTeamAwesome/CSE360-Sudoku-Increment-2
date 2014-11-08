
public class User
{
	private String username;
	private Score userScore;
	private SudokuBoard savedGame;
	private boolean hasSavedGame;
	private String savedGameSize;
	
	public User(String name, SudokuBoard game, Score score)
	{
		username = name;
		savedGame = game;
		userScore = score;
		hasSavedGame = false;
		savedGameSize = "";
	}
	
	public void setUsername(String newName)
	{
		// Need to change username in file to update
		username = newName;
	}
	//public void setScore(Score newScore);
	public void setSavedGame(SudokuBoard game)
	{
		savedGame = game;
	}
	public void setScore(Score score)
	{
		userScore = score;
	}
	public void setHasSavedGame(boolean bool)
	{
		hasSavedGame = bool;
	}
	public void setSavedGameSize(String size)
	{
		savedGameSize = size;
	}
	public String getUsername()
	{
		return username;
	}
	public SudokuBoard getSavedGame()
	{
		return savedGame;
	}
	public Score getScore()
	{
		return userScore;
	}
	public boolean isGameSaved()
	{
		return hasSavedGame;
	}
	public String getSavedGameSize()
	{
		return savedGameSize;
	}
	//public void display_highscore()
	// Should show time, score, size, and difficulty
	
}
