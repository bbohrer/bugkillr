package bugkillr;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Problem{
	/* This goes against the convention of using the Key type,
	 * because this key will have to be passed to another program.
	 * Using an integer key means the data can be understood by
	 * programs of all languages with little fuss.
	 */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	//Name for the problem, used, for example, when listing the problems a certain user has solved.
	@Persistent
	private String name;
	
	//URL of the web page containing a description of the problem. This is used when building lists of problems, to create links to the problems.
	@Persistent
	private String descriptionURL;
	
	//URL to the web page containing the help for the program.
	@Persistent
	private String helpURL;
	
	/*URL for the program used to solve the problem. This is used to verify a problem has been done correctly (When a problem is submitted,
	  the server will run it and then run the program at solverURL on the solution's output, and this program will determine whether it is correct.
	  This isn't complete, because there are several different ways to verify that a program is correct; for example on some problems it would be better
	  to have an instructor check that a solution is correct.
	*/
	@Persistent
	private String solverURL;
	
	/*Score required before problem can be played*/
	private int minscore;
	public long getKey(){
		return key;
	}
	public String getDescriptionURL(){
		return descriptionURL;
	}
	public String getSolverURL(){
		return solverURL;
	}
	public String getHelpURL()
	{
		return helpURL;
	}
	public String getName(){
		return name;
	}
	public int getMinscore(){
		return minscore;
	}
	public Problem(String Name, String DescriptionURL, String HelpURL, String SolverURL, int Minscore){
		name = Name;
		descriptionURL = DescriptionURL;
		helpURL = HelpURL;
		solverURL = SolverURL;
		minscore = Minscore;
	}
}
