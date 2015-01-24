//Pokemon.java
//Alex Popovic
//The class for pokemon, keeps track of all things that define a pokemon.

import java.util.*;
import java.math.*;

public class Pokemon{
	
	private int hp,energy = 50;
	private String name, type, resist, weak;
	private final int maxen = 50;
	private final int maxHP;
	private boolean stun = false, disable = false;
	
	private int numMoves;
	private String[] movesList;
	private PokemonAttack[] attacks;
	
	public Pokemon(String name, String hp, String ty, String res, String wk, String[] moves){
		this.name = name;
		this.hp = Integer.parseInt(hp);
		this.maxHP = Integer.parseInt(hp);  
		this.type = ty;
		this.resist = res;
		this.weak = wk;
		
		this.numMoves = Integer.parseInt(moves[0]);
		this.attacks = new PokemonAttack[numMoves];
		this.movesList = new String [numMoves];
		
		for(int i=0 ;i<numMoves;i+=1){ //make an arraylist for the attacks to make it easy to use
			this.movesList[i] = moves[4*i+1];
			attacks[i] = new PokemonAttack(moves[4*i+1],moves[4*i+2],moves[4*i+3],moves[4*i+4]);
		}
	}
	
	public double checkType(PokemonAttack attack, Pokemon opp){ //checks type of pokemon for super or not very effective
		double multiply = 1; 
			
		if(this.type.equals(opp.getResist())){
			multiply = 1/2;
			System.out.println("It wasn't very effective!");
		}
		else if(this.type.equals(opp.getWeak())){
			multiply =  2;
			System.out.println("It was super effective!");
		}
		return multiply;
	} 
	
	public int getDamage(PokemonAttack attack, Pokemon opp){ //finds damage of attack taking into account effects
		int damage = attack.getDamage();
		
		if(this.disable){
			damage-=10;
		}
		return damage;
	} 
	
	public int doDamage(PokemonAttack attack, Pokemon opp){ //combines all the damage into one
		return (int)(getDamage(attack,opp)*checkType(attack,opp));
	} 
	
	public void fisticuffs(PokemonAttack attack, Pokemon opp){ //continue looking at damage looking at the specials of the attack
		int damage = doDamage(attack, opp);
		String special = attack.getSpecial();
		
		if(special.equals("stun")){
			if((int)(Math.random()*2)==0){
				opp.setStun(true);
				System.out.printf("%s was stunned!\n", opp.getName());	
			}	
		}
		
		else if(special.equals("disable")){	
			if(opp.getDisable()==false){
				opp.setDisable(true);
				System.out.printf("%s is now disabled!\n", opp.getName());
			}
			else{
				System.out.printf("%s is already disabled.\n", opp.getName());
			}
		}
		
		else if(special.equals("wild card")){
			System.out.println("Wild card activated!");
			if((int)(Math.random()*2)==0){
				damage = 0;
				System.out.println("Wild card failed.");
			}
			else{
				System.out.println("Wild card succeeded!.");
			}
		}
		
		else if(special.equals("wild storm")){
			if((int)(Math.random()*2)==0){
				if(opp.getHp()>attack.getDamage()){
					System.out.printf("\n%s is on a wild storm frenzy!\n", this.name);
					fisticuffs(attack, opp);
				}
				else{
					System.out.println("Wild storm succeeded\n.");
				}
			}
			else{
				damage = 0;
				System.out.println("Wild storm failed.\n");
			}
		}
		
		else if(special.equals("recharge")){
			this.setEnergy(20);
			System.out.printf("Recharge activated. %s's energy is now at %d.\n", this.name,this.energy);
		}
		
		if(damage<=0){
			damage = 0;
		}
		
		opp.setHp(-damage);
		this.setEnergy(-attack.getCost());
		
		System.out.printf("%s did %d damage to %s.\n\n", name, damage,opp.getName());
	} 
	
	public boolean canAttack(){ // can the pokemon do any attack based on energy
		for(int i =0;i<movesList.length;i++){
			if(this.energy>=attacks[i].getCost()){
				return true;
			}
		}
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public int getHp(){
		return hp;
	}
	
	public void setHp(int i){
		hp += i;
		
		if(hp>maxHP){
			hp = maxHP;
		}
		else if(hp<0){
			hp=0;
		}
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public void setEnergy(int i){
		energy += i;
		
		if(energy>maxen){
			energy = maxen;
		}
		else if(energy<0){
			energy = 0;
		}	
	}
	
	public String getType(){
		return type;
	}
	
	public String getResist(){
		return resist;
	}
	
	public String getWeak(){
		return weak;
	}
	
	public Boolean getStun(){
		return stun;
	}
	
	public void setStun(boolean status){
		stun = status;
	}
	
	public Boolean getDisable(){
		return disable;
	}
	
	public void setDisable(boolean status){
		disable = status;
	}
	
	public String[] getAttackNames(){
		return movesList;
	}
	
	public PokemonAttack[] getAttacks(){
		return attacks;
	}
	
	public String toString(){
		return String.format("Name: %10s.  HP: %3d. Type: %10s. Resistance: %10s. Weakness: %10s. Attacks: %s ",this.name,this.hp,this.type,this.resist,this.weak,Arrays.toString(this.attacks));
	}
}