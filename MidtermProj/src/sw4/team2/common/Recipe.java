package sw4.team2.common;

import java.io.Serializable;

public class Recipe implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String engName;
	private int beverageType;
	private boolean beverageGroup;
	private int type;
	private String answer;
	
	public static final int TYPE_GLASS = 0;
	public static final int TYPE_BEVERAGE = 1;
	public static final int TYPE_GARNISHES = 2;
	public static final int TYPE_ADDITIONAL = 3;
	public static final int TYPE_BARSOPPN = 4;
	public static final int TYPE_COCKTAILPICK = 5;
	public static final int TYPE_SHAKER = 6;
	public static final int TYPE_MIXINGGLASS = 7;
	public static final int TYPE_STRAINER = 8;
	public static final int TYPE_BLENDER = 9;
	
	public static final int BEVERAGE_GIN = 0;
}
