//PokemonArena.java
//Alex Popovic
//Main class for the Grand Stage, keeps track of the universe and all inside it

import java.util.*;
import java.math.*;
import java.io.*;

public class PokemonArena{
	static ArrayList<Pokemon> pokes = new ArrayList<Pokemon>(); 		//all pokemon
	static ArrayList<Pokemon> playerPokes = new ArrayList<Pokemon>(); 	//player pokemon
	static ArrayList<Pokemon> oppPokes = new ArrayList<Pokemon>(); 		//opp pokes
	static ArrayList<String> trainerNames = new ArrayList<String>(); 	//uselsess now dont worry bout it :P
	public static Pokemon currentPoke,currentOppPoke; 					//will always be up to date with current pokemon
	static int oppNum = 0; 												//amount of enemy pokemon
	
	static boolean pokemonAlive = true; //team is still alive
	static boolean trainerLeft = true;  //more fights remaining
	static boolean goAhead = false;		//will give you a chance to repick
	static boolean retry = true;		//will give you a chance to replay
	
	
	public static void main(String[]args){ //this is the main loop
			loadAll(); 								//load all the pokemon from file
			while(retry){ 							//will allow user to play again
				while(!goAhead){					
					pickPoke();						//pick pokemon
					checkSatisfaction();			//either move on or repick
				}
				oppPickPoke();						//take into account all enemies
				//pickNames();
				oppNum = oppPokes.size();			//amount of enemy pokemon, keep track of which enemy you are battling
				while(pokemonAlive && trainerLeft){ //one needs to fail to get out of battle
					System.out.printf("\nYou are up against: %s\n",oppPokes.get(oppNum-1).getName());
					battle(chooseCurrent(),oppPokes.get(oppNum-1)); //BATTLE IT OUT
					recharge();						//health potions for everyone!
					clearEffects();					//reset all effects
					pokemonAlive = checkUser();		//check if you are alive
					trainerLeft = checkOpp(); 		//check if anyone left
				}
				if(pokemonAlive){ 		//you win
					System.out.println("Congradulations! You have beaten the Pokemon Arena!");
					System.out.println("You are now the NEW POKEMON LEAGUE CHAMPION");
				}
				else{					//you lose
					System.out.println("Ha-ha, you lose. Alex remains the POKEMON LEAGUE CHAMPION!");
					System.out.println("Better luck next time.");
				}
				checkRetry();
			}
			System.out.println("Thank you for playing.");
	}
	
	public static void loadAll(){ //load all pokemon
		try{
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
			int num = Integer.parseInt(inFile.nextLine());
			String[] allPoke;
			
			for(int i=0;i<num;i++){
				String[] eachPoke = new String[5];
				allPoke = inFile.nextLine().split(",");
				
				for(int j= 0; j<5;j++){
					eachPoke[j] = allPoke[j];
				}
				
				String[] moves = Arrays.copyOfRange(allPoke,5,allPoke.length);
				pokes.add(new Pokemon(eachPoke[0],eachPoke[1],eachPoke[2],eachPoke[3],eachPoke[4],moves));	
			}
		}
		catch(Exception except){
			System.out.print("Whyyyyyyyyyy " + except);
		}
	}
	
	public static void pickPoke(){ //Pick your pokemon
		Scanner kb = new Scanner(System.in);
		
		for(int i = 0; i<pokes.size();i++){
			System.out.printf("%2d. %s\n",i+1,pokes.get(i).getName());
		}
		
		while(playerPokes.size()<4){
			System.out.println("\nPlease pick your pokemon team: (" + (4-playerPokes.size()) + " left)");
			int pick =  kb.nextInt();
		
			if(pick>pokes.size() || pick<1){
				System.out.println("\nBro, come on. You see that list. You wanna pick something INSIDE. -_-\n");
			}
			else if(playerPokes.contains(pokes.get(pick-1))){
				System.out.println("\nHey, I love that pokemon too, but that just can't work and you know it...\n");				
			}
			else{
				playerPokes.add(pokes.get(pick-1));
				System.out.println("\nYay! " + pokes.get(pick-1).getName() + " has been added to your team! :D\n");
				
			}
		}
		System.out.println("Your team: ");
		for(int i= 0;i<playerPokes.size();i++){
			System.out.printf("#%d: %s\n",i+1,playerPokes.get(i).getName());
		}
	}
	
	public static void checkSatisfaction(){
		Scanner kb = new Scanner(System.in);
		System.out.println("\nIs this your final choice? (y/n)");
		String response =  kb.next();
		if(response.equals("y")){
			System.out.println("\nGreat! It's time to duel!");
			goAhead = true;
		}
		else{
			System.out.println("Let's try this again then...");
			playerPokes.clear(); //clear your selections
		}
	}
	
	public static void checkRetry(){
		Scanner kb = new Scanner(System.in);
		System.out.println("Would you like to go again? (y/n)");
		String response =  kb.next();
		if(response.equals("y")){
			System.out.println("Let's try this again then...");
			playerPokes.clear(); //clear teams
			oppPokes.clear();
			retry = true;
		}
		else{
			retry = false;
		}
	}
	
	public static void oppPickPoke(){ //Picks random order pokemon for opponents
		Collections.shuffle(pokes);
		for(int i = 0; i<pokes.size();i++){
			if(playerPokes.contains(pokes.get(i))){
				;
			}
			else{
				oppPokes.add(pokes.get(i));
			}
		}
	}
	
	public static void pickNames(){ //
		//ussless dont worry about :)
		String[] nick = {"Wierd","Typical Jock","Nerd","Stinky","Geek","Sketch","LOSER","Kewl Kat","Brat","Hater","Amateur","COCKy","Girly","Masculine","Genius","Nigga","Smart-ass","Cutie","Feminine","Femme Fatale"};
		String[] name = {"Alex","Ahmed","Wendy","Marko","Luka","Rafay","Sir","Stefan","Luka","Jovan","Ivana","Sara","Selena","Lana","Dragan","Nikola","Borislav","Komsija","Tina","Tamara"};
		for(int i=0;i<oppPokes.size();i++){
			Collections.shuffle(Arrays.asList(nick));
			Collections.shuffle(Arrays.asList(name));
			trainerNames.add(nick[0]+" "+ name[0]);
		}
	}
	
	public static boolean checkUser(){ //any pokes alive?
		for(Pokemon p:playerPokes){
			if(p.getHp()>0){
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkOpp(){
		for(Pokemon p:oppPokes){
			if(p.getHp()>0){
				return true;
			}
		}
		return false;
	}
	
	public static void startBattle(){ //usseless cause yeah.... too cool for school
		System.out.printf("%-15s                                 %15s\n","Your Team",trainerNames.get(oppNum)+"'s");
		System.out.printf("%-15s -------------      ------------- \n",playerPokes.get(0).getName());
		System.out.printf("%-15s -------------      ------------- \n",playerPokes.get(1).getName());
		System.out.printf("                             VERSUS             %15s\n",oppPokes.get(oppNum).getName());
		System.out.printf("%-15s -------------      ------------- \n",playerPokes.get(2).getName());
		System.out.printf("%-15s -------------      ------------- \n",playerPokes.get(3).getName());
	}
	
	public static Pokemon chooseCurrent(){ //display team stats and choose pokemon
		Scanner kb = new Scanner(System.in);
		System.out.printf("\nPokemon:   1: %10s   2: %10s   3: %10s   4: %10s\n",playerPokes.get(0).getName(),playerPokes.get(1).getName(),playerPokes.get(2).getName(),playerPokes.get(3).getName());
		System.out.printf("HP:           %10d      %10d      %10d      %10d\n",playerPokes.get(0).getHp(),playerPokes.get(1).getHp(),playerPokes.get(2).getHp(),playerPokes.get(3).getHp());
		System.out.printf("Energy:       %10d      %10d      %10d      %10d\n",playerPokes.get(0).getEnergy(),playerPokes.get(1).getEnergy(),playerPokes.get(2).getEnergy(),playerPokes.get(3).getEnergy());
		System.out.println("\nWhich pokemon do you select?");
		boolean done = false;
		int choice = 0;
		while(!done){
			choice = kb.nextInt();
			if(choice<=0 || choice>4){
				System.out.println("You only have the four pokemon...");
			}
			else if(playerPokes.get(choice-1).getHp()==0){
				System.out.println("I'm afraid that one isn't possible. :( He dead bro. ");				
			}
			else{
				done = true;
			}
		}
		System.out.printf("\nI choose you %s!\n",playerPokes.get(choice-1).getName());
		return playerPokes.get(choice-1);
	}
	
	public static PokemonAttack chooseAttack(Pokemon poke){ //display attacks and choose one
		Scanner kb = new Scanner(System.in);
		System.out.println();
		for(int i=0; i<poke.getAttackNames().length;i++){
			System.out.printf("Attack #%d: %10s   Cost: %3d   Damage: %3d   Special: %10s\n",i+1,poke.getAttacks()[i].getName(),poke.getAttacks()[i].getCost(),poke.getAttacks()[i].getDamage(),poke.getAttacks()[i].getSpecial());
		}
		System.out.println("\nWhich attack do you select?");
		boolean done = false;
		int choice = 0;
		while(!done){
			choice = kb.nextInt();
			if(choice<0 || choice>4){
				System.out.println("Invalid attack.");
			}
			else if(poke.getAttacks()[choice-1].getCost()>poke.getEnergy()){
				System.out.println("Not enough energy.");				
			}
			else{
				done = true;
			}
		}
		System.out.printf("%s attacked using %s!\n",poke.getName(),poke.getAttacks()[choice-1].getName());
		return poke.getAttacks()[choice-1];
	}
	
	public static PokemonAttack chooseEnemyAttack(Pokemon poke){ //choose random attack for enemy
		boolean done = false;
		int choice = (int)(Math.random()*poke.getAttackNames().length); //random from available
		int ogchoice = choice;
		while(!done){
			if(poke.getAttacks()[choice].getCost()>poke.getEnergy()){				
			}
			else{
				done = true;
			}
			choice+=1;
			if(choice==poke.getAttackNames().length){ 	//cycles through all attacks starting randomly
				choice=1;								//alraedy checked, one must be possible
			}
		}
		System.out.printf("%s attacked using %s!\n",poke.getName(),poke.getAttacks()[choice-1].getName());
		return poke.getAttacks()[choice-1];
	}
	
	public static void doSomething(boolean enemy){ //asks the user to do something, moves the battle along
		Scanner kb = new Scanner(System.in);
		int decision = 0;
		if(enemy){ //whos doing something, user or enemy, choose for enemie
			if(!currentOppPoke.getStun()){	
				if(currentOppPoke.canAttack()){
					currentOppPoke.fisticuffs(chooseEnemyAttack(currentOppPoke),currentPoke); //choose attack and move on with it
				}
				else{
					System.out.printf("%s passed the turn.\n", currentOppPoke.getName());
				}
			}
			else{
				System.out.printf("%s is stunned and cannot move.\n", currentOppPoke.getName());
			}	
		}
		else{ //give user options
			System.out.printf("%s%15s%15s\n\n", "1. Attack", "2. Retreat", "3. Pass");

			while(decision<1 || decision>3){
				System.out.println("What will you do? \n");
				decision = kb.nextInt();
				
				if(!currentPoke.getStun()){
					
					if(decision==1){
						if(currentPoke.canAttack()){
							currentPoke.fisticuffs(chooseAttack(currentPoke),currentOppPoke);
						}
						else{
							System.out.printf("%s does not have enough energy to attack.\n\n", currentPoke.getName());
							decision = 0;
						}
					}
					else if(decision==2){
						System.out.printf("%s retreated.\n\n", currentPoke.getName());
						currentPoke = chooseCurrent();	
					}
					else if(decision==3){
						System.out.printf("%s passed the turn.\n\n", currentPoke.getName());
					}
				}
				else{
					System.out.printf("%s is stunned and cannot move.\n\n", currentPoke.getName());
				}
			}	
		}
	}
	
	public static void battle(Pokemon player,Pokemon opp){ //actual battle
		currentPoke = player;
		currentOppPoke = opp;

		int first = (int)(Math.random()*2); //random who attacks first
		boolean dead = false;
		boolean deadOpp = false;
		
		while(!dead && !deadOpp){ //till someones dead!
			
			//battle interface showing stats to let you make an informed decision.
			System.out.printf("Pokemon: %10s    %10s\n",currentPoke.getName(),currentOppPoke.getName());
			System.out.printf("HP:      %10d    %10d\n",currentPoke.getHp(),currentOppPoke.getHp());
			System.out.printf("Energy:  %10d    %10d\n\n",currentPoke.getEnergy(),currentOppPoke.getEnergy());
			
			if(first==0){ //first you
				doSomething(false);
				
				if(currentOppPoke.getHp()<=0){
					deadOpp = true;
					oppNum-=1;
					rechargeEnergy();
					break;
				}
	
				if(currentPoke.getStun()){
					System.out.printf("%s is no longer stunned.\n", currentPoke.getName());
					currentPoke.setStun(false);
				}
				
				doSomething(true);
				
				if(currentPoke.getHp()<=0){
					dead = true;
					rechargeEnergy(); //recharge energy, then break before dead pokemon attacks.
					break;
				}
				
				if(currentOppPoke.getStun()){
					System.out.printf("%s is no longer stunned.\n\n", currentOppPoke.getName());
					currentOppPoke.setStun(false);
				}
				
			}
			else{ //first enemie
				doSomething(true);
				
				if(currentPoke.getHp()<=0){
					dead = true;
					rechargeEnergy();
					break;
				}
				
				if(currentOppPoke.getStun()){
					System.out.printf("%s is no longer stunned.\n\n", currentOppPoke.getName());
					currentOppPoke.setStun(false);
				}
				
				doSomething(false);
				
				if(currentOppPoke.getHp()<=0){
					deadOpp = true;
					oppNum-=1;
					rechargeEnergy();
					break;
				}
				
				if(currentPoke.getStun()){
					System.out.printf("%s is no longer stunned.\n\n", currentPoke.getName());
					currentPoke.setStun(false);
				}
			}
			
			rechargeEnergy();
		}
		
		if(dead){
			System.out.printf("%s is unable to battle, %s is the winner!\n", currentPoke.getName(), currentOppPoke.getName());
			
		}
		else if(deadOpp){
			System.out.printf("%s is unable to battle, %s is the winner!\n", currentOppPoke.getName(), currentPoke.getName());
			
		}		
	}
	
	public static void recharge(){
		for(Pokemon poke:playerPokes){
			if (poke.getHp()>0){
				poke.setHp(20);
			}
		}
	}
	
	public static void rechargeEnergy(){
		currentOppPoke.setEnergy(10);
		for(int i=0;i<4;i++){
			playerPokes.get(i).setEnergy(10); //recharge energy for all your pokemon, doesnt matter if dead since you cant use anyway.
		}
	}
	
	public static void clearEffects(){
		for(Pokemon poke:playerPokes){
			poke.setDisable(false);
			poke.setStun(false);
		}
	}
}