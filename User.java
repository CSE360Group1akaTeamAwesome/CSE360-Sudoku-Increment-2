
public class User
{
	private String username;
	Score userScore;
	private SudokuBoard savedGame;
	
	public User(String name, SudokuBoard game, Score score)
	{
		username = name;
		savedGame = game;
		userScore = score;
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
	
	//public void display_highscore()
	// Should show time, score, size, and difficulty
	
}
