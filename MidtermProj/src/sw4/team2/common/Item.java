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
	public static final int TYPE_BARSOPPN = 4;
	public static final int TYPE_COCKTAILPICK = 5;
	public static final int TYPE_SHAKER = 6;
	public static final int TYPE_MIXINGGLASS_AND_STRAINER= 7;
	public static final int TYPE_BLENDER = 8;
	//==============Beverage관련=========================
	public static final int SECTION_ALCOHOLIC = 0;
	public static final int SECTION_ADD_ALCOHOILC = 1;
	public static final int SECTION_NON_ALCOHOLIC = 2;
	public static final int SECTION_NONE = -1;
	//==============Beverage관련=========================
	public static final int BEVERAGE_BACARDIWHITERUM=0;
	public static final int BEVERAGE_BOURBONWHISKEY=1;
	public static final int BEVERAGE_BRANDY=2;
	public static final int BEVERAGE_DRYGIN=3;
	public static final int BEVERAGE_SCOTCHWHISKY=4;
	public static final int BEVERAGE_SLOEGIN=5;
	public static final int BEVERAGE_TEQUILA=6;
	public static final int BEVERAGE_VODKA=7;
	public static final int BEVERAGE_WHITERUM=8;
	public static final int BEVERAGE_WHITEWINE=9;
	public static final int BEVERAGE_TRADITIONAL=10;
	public static final int BEVERAGE_GALLIANO=11;
	public static final int BEVERAGE_GRANDMARNIER=12;
	public static final int BEVERAGE_GREENCREEMDEMENTHE=13;
	public static final int BEVERAGE_DRYVERMOUH=14;
	public static final int BEVERAGE_DRAMBUIE=15;
	public static final int BEVERAGE_MELONLIQUEUR=16;
	public static final int BEVERAGE_BANANALIQUEUR=17;
	public static final int BEVERAGE_BENEDICTINEDOM=18;
	public static final int BEVERAGE_BAILEYSIRISHCREAM=19;
	public static final int BEVERAGE_BLUECURACAO=20;
	public static final int BEVERAGE_SWEETVERMOUTH=21;
	public static final int BEVERAGE_APPLEBRANDY=22;
	public static final int BEVERAGE_APPLERPUCKER=23;
	public static final int BEVERAGE_APRICOTBRANDY=24;
	public static final int BEVERAGE_CHERRYBRANDY=25;
	public static final int BEVERAGE_CAMPARI=26;
	public static final int BEVERAGE_COFFELIQUEUR=27;
	public static final int BEVERAGE_COINTREAU=28;
	public static final int BEVERAGE_COCONUTRUM=29;
	public static final int BEVERAGE_CRENEDECASSIS=30;
	public static final int BEVERAGE_TRIPLESEC=31;
	public static final int BEVERAGE_WHITECREMEDEMENTHE=32;
	public static final int BEVERAGE_WHITECREMEDECACAO=33;
	public static final int BEVERAGE_GRENADINESYRUP=34;
	public static final int BEVERAGE_GRAPEFRUITJUICE=35;
	public static final int BEVERAGE_LIMEJUICE=36;
	public static final int BEVERAGE_RASPBERRYSYRUP=37;
	public static final int BEVERAGE_LEMONJUICE=38;
	public static final int BEVERAGE_CIDER=39;
	public static final int BEVERAGE_SODAWATHER=40;
	public static final int BEVERAGE_SWEETANDSOURMIX=41;
	public static final int BEVERAGE_ANGOSTURABITTERS=42;
	public static final int BEVERAGE_ORANGEJUICE=43;
	public static final int BEVERAGE_WORCESTERSHIRESAUCE=44;
	public static final int BEVERAGE_MILK=45;
	public static final int BEVERAGE_GINGERALE=46;
	public static final int BEVERAGE_WHITEGRAPEJUICE=47;
	public static final int BEVERAGE_COLA=48;
	public static final int BEVERAGE_CRANBERRYJUICE=49;
	public static final int BEVERAGE_TABASCOSAUCE=50;
	public static final int BEVERAGE_TOMATOJUICE=51;
	public static final int BEVERAGE_PINEAPPLEJUICE=52;
	public static final int BEVERAGE_PINACOLADAMIX=53;
	public static final int BEVERAGE_NONE = -1;
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
