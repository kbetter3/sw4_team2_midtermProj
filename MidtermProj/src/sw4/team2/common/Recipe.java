package sw4.team2.common;

import java.io.Serializable;

public class Recipe implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Item item;
	private boolean beverageGroup;
	private String answer;
	
	public Recipe(Item item, boolean beverageGroup, String answer) {
		this.item = item;
		this.beverageGroup = beverageGroup;
		this.answer = answer;
	}

	public Item getItem() {
		return item;
	}

	public boolean isBeverageGroup() {
		return beverageGroup;
	}

	public String getAnswer() {
		return answer;
	}
}
