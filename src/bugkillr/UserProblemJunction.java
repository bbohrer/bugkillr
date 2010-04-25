package bugkillr;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserProblemJunction{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Key userId;
	
	@Persistent
	private long problemId;
	
	
	public Key getKey(){
		return key;
	}
	public long getProblemId(){
		return problemId;
	}
	public Key getUserId(){
		return userId;
	}
	public UserProblemJunction(Key UserId, long ProblemId){
		userId = UserId;
		problemId = ProblemId;
	}
}
