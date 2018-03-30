package sw4.team2.common;

import java.io.Serializable;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String engName;
	private int beverageSection;
	private String beverageType;
	private int type;
	
	// =================== 전체타입 ===================
	public static final int TYPE_GLASS = 0;
	public static final int TYPE_BEVERAGE = 1;
	public static final int TYPE_GARNISHES = 2;
	public static final int TYPE_ADDITIONAL = 3;
	public static final int TYPE_BARSPOON = 4;
	public static final int TYPE_COCKTAILPICK = 5;
	public static final int TYPE_SHAKER = 6;
	public static final int TYPE_MIXINGGLASS_AND_STRAINER= 7;
	public static final int TYPE_BLENDER = 8;
	//==============Beverage관련=========================
	public static final int SECTION_ALCOHOLIC = 0;
	public static final int SECTION_ADD_ALCOHOILC = 1;
	public static final int SECTION_NON_ALCOHOLIC = 2;
	public static final int SECTION_NONE = -1;
	//=============Garnishe타입===========================혹시나 쓸까봐 다 만들어놨음..====
	public static final int GARNISHE_REDCHERRY=0;
	public static final int GARNISHE_GREENOLIVE=1;
	public static final int GARNISHE_ORANGE=2;
	public static final int GARNISHE_LEMON=3;
	public static final int GARNISHE_CELERYSTICK=4;
	public static final int GARNISHE_NUTMEG=5;
	public static final int GARNISHE_PINEAPPLE=6;
	public static final int GARNISHE_APPLE=7;
	//=============Additional타입=========================
	public static final int ADDITIONAL_CUBEDSUGAR=0;
	public static final int ADDITIONAL_CUBEDICE=1;
	public static final int ADDITIONAL_SALT=2;
	public static final int ADDITIONAL_PEPPER=3;
	public static final int ADDITIONAL_POWDEREDSUGAR=4;
	public static final int ADDITIONAL_LEMONPIECE=5;
	public static final int ADDITIONAL_CRUSHEDICE=6;
	//=============Tool타입=================================
	
	public Item(String name, String engName, int beverageSection, String beverageType, int type) {
		this.name = name;
		this.engName = engName;
		this.beverageSection = beverageSection;
		this.beverageType = beverageType;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getEngName() {
		return engName;
	}
	
	public int getBeverageSection() {
		return beverageSection;
	}

	public String getBeverageType() {
		return beverageType;
	}

	public int getType() {
		return type;
	}
}
