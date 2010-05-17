package bugkillr;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable(detachable="true")
public class User{
	private static final long serialVersionUID = 1L;
	//Primary key for the user
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	//User's email address
	@Unique
	@Persistent
	private String emailAddr;
	
	//String for the user's team
	@Persistent
	private String teamId; 
	
	//User's current score
	@Persistent
	private int score;
	
	public int getScore()
	{
		return score;
	}
	public void setScore(int Score)
	{
		score=Score;
	}
	public Key getKey(){
		return key;
	}
	public String getAccountId(){
		return emailAddr;
	}
	public String getTeamName(){
		return teamId;
	}
	public User(String TeamName, String EmailAddr){
		teamId = TeamName;
		emailAddr = EmailAddr;
		score = 0;
	}
	public void setTeam( String TeamName) {
		teamId = TeamName;
	}
}
