//PokemonAttack.java
//Alex Popovic
//The class keeping track of all the details of each attack the pokemon has.

import java.util.*;
import java.math.*;

public class PokemonAttack{
	private String name, special;
	private int cost, damage;
	
	public PokemonAttack(String n, String c, String d, String s){
		this.name = n;
		this.cost = Integer.parseInt(c);
		this.damage = Integer.parseInt(d);  
		this.special = s;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
	public String getSpecial(){
		return this.special;
	}
	
	public void setDamage(int i){
		damage += i;
		
		if(damage<0){ //min damage is 0
			damage = 0;
		}
	}
	
	public String toString(){
		return String.format("Attack: %18s. Damage: %3d. Cost: %3d. Special: %10s.",this.name,this.damage,this.cost,this.special);
	}
}