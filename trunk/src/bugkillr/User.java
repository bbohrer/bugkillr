package bugkillr;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
public class User{
	//Primary key for the user
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	//User's email address
	private String emailAddr;
	
	//String for the user's team
	private String teamId;
	
	public Key getKey(){
		return key;
	}
	public String getAccountId(){
		return emailAddr;
	}
	public String getTeamId(){
		return teamId;
	}
	public User(String TeamId, String EmailAddr){
		teamId = TeamId;
		emailAddr = EmailAddr;
	}
	public void setTeam(String TeamID) {
		teamId = TeamID;
	}
}
