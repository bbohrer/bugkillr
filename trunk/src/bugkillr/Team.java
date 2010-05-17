package bugkillr;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;


@PersistenceCapable
public class Team{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	//Team's name, chosen by creator when the team is made. Displayed on high scores page, etc.
	@Persistent
	@Unique
	private String name;
	
	//The user that created the team
	@Persistent
	private Key owner;
	
	//Total score of all users on the team.
	//This could be computed from the scores of
	//the users, but this way makes some of the queries much simpler.
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
	public Key getOwner(){
		return owner;
	}
	
	public String getName(){
		return name;
	}
	
	public Key getKey(){
		return key;
	}
	
	public Team(String Name, Key Owner){
		name = Name;
		owner = Owner;
		score =0;
	}
}
