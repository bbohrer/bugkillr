package bugkillr;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class Team{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	//Team's name, chosen by creator when the team is made. Displayed on high scores page, etc.
	@Persistent
	private String name;
	
	//The user that created the team
	@Persistent
	private Key owner;
	
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
	}
}
