package sw4.team2.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequestMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private int requestType;
	private int requestMode;
	private String userId;
	private Map<String, Cocktail> cocktailMap;
	
	public static final int TYPE_REQUEST = 0;
	public static final int TYPE_WAN = 1;
	
	public static final int MODE_PRACTICE = 1;
	public static final int MODE_EXAM = 3;
	
	public RequestMessage(int rType, int rMode, String uId, Map<String, Cocktail> cocktailMap) {
		this.requestType = rType;
		this.requestMode = rMode;
		this.userId = uId;
		if (cocktailMap == null) {
			cocktailMap = new HashMap<>();
		} else {
			this.cocktailMap = cocktailMap;
		}
	}

	public int getRequestType() {
		return requestType;
	}

	public int getRequestMode() {
		return requestMode;
	}

	public String getUserId() {
		return userId;
	}

	public Map<String, Cocktail> getCocktailMap() {
		return cocktailMap;
	}
}
