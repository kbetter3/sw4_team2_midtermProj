package sw4.team2.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RequestMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private int requestType;
	private int requestMode;
	private String cocktailName;
	private String userId;
	private Set<String> cocktail;
	
	public static final int TYPE_REQUEST = 0;
	public static final int TYPE_WAN = 1;
	
	public static final int MODE_PRACTICE = 1;
	public static final int MODE_EXAM = 3;
	
	public RequestMessage(int rType, int rMode, String cName, String uId, Set set) {
		this.requestType = rType;
		this.requestMode = rMode;
		this.cocktailName = cName;
		this.userId = uId;
		if (set == null) {
			cocktail = new HashSet<>();
		} else {
			cocktail = set;
		}
	}
}
