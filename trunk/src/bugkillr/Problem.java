package bugkillr;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Problem{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	//Name for the problem, used, for example, when listing the problems a certain user has solved.
	@Persistent
	private String name;
	
	//URL of the web page containing a description of the problem. This is used when building lists of problems, to create links to the problems.
	@Persistent
	private String descriptionURL;
	/*Path to the program used to solve the problem. This is used to verify a problem has been done correctly (When a problem is submitted,
	  the server will run it and then run the program at solverFileName on the solution's output, and this program will determine whether it is correct.
	  This isn't complete, because there are several different ways to verify that a program is correct; for example on some problems it would be better
	  to have an instructor check that a solution is correct.
	*/
	
	@Persistent
	private String solverFileName;
	
	public Key getKey(){
		return key;
	}
	public String getDescriptionURL(){
		return descriptionURL;
	}
	public String getSolverFileName(){
		return solverFileName;
	}
	
	public String getName(){
		return name;
	}
	public Problem(String Name, String DescriptionURL, String SolverFileName){
		name = Name;
		descriptionURL = DescriptionURL;
		solverFileName = SolverFileName;
	}
}
