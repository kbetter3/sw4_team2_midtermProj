package sw4.team2.common;

import java.io.Serializable;
import java.util.List;

public class Cocktail implements Serializable {
	String name;
	List<Recipe> recipe;
	
	public Cocktail(String name, List<Recipe> recipe) {
		this.name = name;
		this.recipe = recipe;
	}
	
	
	public String getName() {
		return name;
	}
	public List<Recipe> getRecipe() {
		return recipe;
	}
}
