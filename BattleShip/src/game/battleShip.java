package game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class battleShip {

	private static int shipIterator = 1; //global value used to cycle between types of ships
										 //in the PLACESHIPS and ENEMYPLACESHIPS methods
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		char[][] grid = new char[14][14];		//create Players grid
		char[][] eGrid = new char[14][14];		//create Enemy grid
		Grid Player = new Grid(grid);
		Grid Enemy = new Grid(eGrid);

		Scanner in = new Scanner(System.in);
		
		System.out.println("Welcome to BalleShip\n");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("type 'I' for instructions");
		System.out.println("           or           ");
		System.out.println("type any key to start the game\n");
		String select = in.next();
		
		//accept request to view instructions
		if(select.equals("I")) {
			displayInstructions();
		}
		
		//set both grids to empty
		Player.setGrid('0');
		Enemy.setGrid('0');
		
		//timed text showing what size ships and how many they have
		System.out.println();
		System.out.println("Prepare to set your board, you have...");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("(x3) 3x1");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("(x2) 4x1");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("(x1) 5x1");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("(x1) 6x1");
		TimeUnit.SECONDS.sleep(1);
		
		Player.printGrid();		//show empty grid
		
		System.out.println();
		TimeUnit.SECONDS.sleep(1);
		
		placeShips(Player, in);		//method for player to place their ships
		EnemyPlaceShips(Enemy);		//method to automatically place enemy ships
		
		System.out.println("PREPARE FOR BATTLE!");
		
		//this grid is the enemy grid that the player sees, they will 
		//see an empty grid but it will fill in as they fire on cells
		char[][] InGameEnemyGrid = new char[14][14]; 	 
		Grid InGame = new Grid (InGameEnemyGrid);	//creation of said grid
		
		//this grid is a duplicate of the grid the player created with
		//their ships. this is so the algorithm can determine if the 
		//enemy hit or miss
		char[][] dupeGrid = new char[14][14];
		Grid PlayerPlacedShips = new Grid(dupeGrid);	//creation of said grid
		
		//this loop copies the players grid to the duplicate grid.
		for(int i=0; i<14; i++) {
			for( int k=0; k<14; k++) {
				PlayerPlacedShips.getGrid()[i][k] = Player.getGrid()[i][k];
				}
			}
		
		createGameBoard(Player, InGame );	//remove all '0' in the players grid, more visually appealing
		InGame.printGrid();
		Player.printGrid();
		
		//this buffered reader is to take in an input with spaces. I dont know if there is a better way.
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(Enemy.hit != 28 || PlayerPlacedShips.hit != 28) {
			System.out.println("Enter Coordinates To Fire Upon, Separate X and Y with a Space");
		
			String str = br.readLine();
			
			//this block splits the one string into 2 ints and passes them to the checkError method
			String[] coords = str.split(" ");
			int x = Integer.parseInt(coords[0])-1; int y = Integer.parseInt(coords[1])-1;
			int Condition = InGame.checkCoordsError(x,y);
        	
			//CONDITION: COORDINATES ARE VALID
			if(Condition == 0) {
				String fire = Enemy.playerMove(x,y, InGame);
				System.out.println(fire);
				InGame.printGrid();
				TimeUnit.SECONDS.sleep(1);
				
				//check if player wins (INEFFICIENT)
				if(Enemy.hit == 28) {
					Win();
				}
				
				String eFire = PlayerPlacedShips.enemyMove(Player); // Enemy fires their shot at random
				System.out.println(eFire);
				Player.printGrid();
				TimeUnit.SECONDS.sleep(1);
				
				//check if enemy wins (INEFFICIENT)
				if(PlayerPlacedShips.hit == 28) {
					Lose();
				}
			}
			//CONDITION: COORDINATES ARE OUT OF BOUNDS
			else if(Condition == 1) {
				System.out.println("Coordinates Are out of Bounds, Select Again");
			}
			//CONIDITON: COORIDNATES HAVE ALREADY BEEN USED PREVIOUSLY
			else {
				System.out.println("Coordinates Have Already Been Fired Upon, Select Again");
			}
		}
		
		in.close();
	}
	
	
	
	
	/*
	 * This method displays instructions if the player requests.
	 * Void Method
	 * Prints Detailed description of game goal, mechanics, and rules. (RULES NOT COMPLETE)
	 */
	public static void displayInstructions() throws InterruptedException {
		System.out.println("INSTRUCTIONS__________________________________________________________________________________________");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("\nThis is BattleShip.\n");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("In this game, you are given a 14x14 grid and a number of ships (Quantity and Size will be displayed)\n"
							+ "You must place these ships in locations of your choice while the enemy does the same for their own grid\n"
							+ "You will then fire upon one of the cells of the grid and either hit an enemy ship, or miss, the cell will then\n"
							+ "be marked depending on whether the shot was a hit or not.\n"
							+ "Your goal is to sink all of the enemy ships before they sink your own.\n\n");
		TimeUnit.SECONDS.sleep(20);
		System.out.println("PLACING SHIPS_________________________________________________________________________________________");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("When placing a ship, you will be presented with two options.\n"
				+ "The first option is to set the X/Y position and the second option being rotate the ship.\n"
				+ "To rotate a ship, type 'r' when the option is presented.\n\n");
		TimeUnit.SECONDS.sleep(12);
		System.out.println("               X = 4, Y = 4\n\n"
						 + "         -------             -------\n"
						 + "         ---X---             ---XXX-\n"
						 + "         ---X---             -------\n"
						 + "         ---X---             -------\n"
						 + "         -------             -------\n"
						 + "*Ship will rotate about the top point of the ship*\n");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("The x and y coordinate that the user enters marks the top most or left most point of the ship\n\n");
		TimeUnit.SECONDS.sleep(10);
		System.out.println("FIRING_________________________________________________________________________________________________");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("When entering coordinates when firing on the enemy gird, type both coordinates in one line\n"
				+ "separated by a single space\n");
		TimeUnit.SECONDS.sleep(7);
		System.out.print("EXAMPLE: ");
		TimeUnit.SECONDS.sleep(1);
		System.out.print("5 5    ");
		TimeUnit.SECONDS.sleep(3/2);
		System.out.println("Typing this will fire upon the coordinates (5,5) on the enemy grid.\n");
		TimeUnit.SECONDS.sleep(2);
		System.out.println("Once either the player or the enemy has sunken the oppositions fleet, the game will end.\n\n");
		TimeUnit.SECONDS.sleep(3); 
		System.out.println("Good Luck.\n");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("The Game will being shortly....");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("--------------------------------------------------------------------------------------------------------");
		TimeUnit.SECONDS.sleep(5);
	}
	/*
	 * this method is a stray (Can and will be moved to main method in the future)
	 * Void Method
	 * Sets the enemy ingame grid to a blank grid, and removes all '0' in players grid.
	 * 
	 * this may be a duplicate block of code already in the main
	 */
	public static void createGameBoard(Grid Player, Grid InGame) {
		//set the enemy grid player will see to blank
		InGame.setGrid(' ');
		for(int i=0; i<14; i++) {
			for( int k=0; k<14; k++) {
				if(Player.getGrid()[i][k] != 'X') {
					Player.getGrid()[i][k] = ' '; 	//clear all cells without 'X' in them in players grid.
				}
			}
		}
		
	}
	/*
	 * This method checks if the player wins;
	 * Void Method 
	 * Prints ASCII art (WINNING SCREEN)
	 */
	public static void Win() throws InterruptedException {
		TimeUnit.SECONDS.sleep(1);
			System.out.println("_     _  ___   __    _  __    _  _______  ______   \r\n"
					+ "| | _ | ||   | |  |  | ||  |  | ||       ||    _ |  \r\n"
					+ "| || || ||   | |   |_| ||   |_| ||    ___||   | ||  \r\n"
					+ "|       ||   | |       ||       ||   |___ |   |_||_ \r\n"
					+ "|       ||   | |  _    ||  _    ||    ___||    __  |\r\n"
					+ "|   _   ||   | | | |   || | |   ||   |___ |   |  | |\r\n"
					+ "|__| |__||___| |_|  |__||_|  |__||_______||___|  |_|");
			TimeUnit.SECONDS.sleep(1/2);
			System.out.println("_     _  ___   __    _  __    _  _______  ______   \r\n"
					+ "| | _ | ||   | |  |  | ||  |  | ||       ||    _ |  \r\n"
					+ "| || || ||   | |   |_| ||   |_| ||    ___||   | ||  \r\n"
					+ "|       ||   | |       ||       ||   |___ |   |_||_ \r\n"
					+ "|       ||   | |  _    ||  _    ||    ___||    __  |\r\n"
					+ "|   _   ||   | | | |   || | |   ||   |___ |   |  | |\r\n"
					+ "|__| |__||___| |_|  |__||_|  |__||_______||___|  |_|");
			TimeUnit.SECONDS.sleep(1/2);
			System.out.println(" _______  __   __  ___   _______  ___   _  _______  __    _ \r\n"
					+ "|       ||  | |  ||   | |       ||   | | ||       ||  |  | |\r\n"
					+ "|       ||  |_|  ||   | |       ||   |_| ||    ___||   |_| |\r\n"
					+ "|     __||       ||   | |     __||      _||   |___ |       |\r\n"
					+ "|    |   |       ||   | |    |   |     |_ |    ___||  _    |\r\n"
					+ "|    |__ |   _   ||   | |    |__ |    _  ||   |___ | | |   |\r\n"
					+ "|_______||__| |__||___| |_______||___| |_||_______||_|  |__|");
			TimeUnit.SECONDS.sleep(1/2);
			System.out.println(" ______   ___   __    _  __    _  _______  ______    __  \r\n"
					+ "|      | |   | |  |  | ||  |  | ||       ||    _ |  |  | \r\n"
					+ "|  _    ||   | |   |_| ||   |_| ||    ___||   | ||  |  | \r\n"
					+ "| | |   ||   | |       ||       ||   |___ |   |_||_ |  | \r\n"
					+ "| |_|   ||   | |  _    ||  _    ||    ___||    __  ||__| \r\n"
					+ "|       ||   | | | |   || | |   ||   |___ |   |  | | __  \r\n"
					+ "|______| |___| |_|  |__||_|  |__||_______||___|  |_||__| ");
			System.exit(0);
		}
	/*
	 * This method checks if the COMPUTER wins
	 * Void Method
	 * Prints ASCII art (LOSE SCREEN)
	 */
	public static void Lose() throws InterruptedException {
			System.out.println(" __   __  _______  __   __ \r\n"
					+ "|  | |  ||       ||  | |  |\r\n"
					+ "|  |_|  ||   _   ||  | |  |\r\n"
					+ "|       ||  | |  ||  |_|  |\r\n"
					+ "|_     _||  |_|  ||       |\r\n"
					+ "  |   |  |       ||       |\r\n"
					+ "  |___|  |_______||_______|");
			TimeUnit.SECONDS.sleep(1/2);
			System.out.println(" ___      _______  _______  _______  __  \r\n"
					+ "|   |    |       ||       ||       ||  | \r\n"
					+ "|   |    |   _   ||  _____||    ___||  | \r\n"
					+ "|   |    |  | |  || |_____ |   |___ |  | \r\n"
					+ "|   |___ |  |_|  ||_____  ||    ___||__| \r\n"
					+ "|       ||       | _____| ||   |___  __  \r\n"
					+ "|_______||_______||_______||_______||__| ");
			System.exit(0);
			
		}
	/*
	 * This method is for the player to place their own ships on the grid. 
	 * Takes (Grid)Player to add ships to the grid and (Scanner)placeShip for user input
	 * Void Method
	 */
	public static void placeShips(Grid Player, Scanner placeShip) throws InterruptedException {
		for(; shipIterator <= 7; shipIterator++ ) {
			if(shipIterator < 4) {
				System.out.println("place 3x1 ship: Enter x coordinate or type r to rotate the ship.");
				String in1 = placeShip.next();
				//if the player rotates the ship, decrease the iterator and loop
				if(in1.equals("r")) {
					Player.rotate();
					shipIterator--;
					
				}
				else {
				System.out.println("Enter y coordinate:");
				String in2 = placeShip.next();
				//Parse coordinates to INT and pass to PLACEMENTRESULT method.
				String place = Player.placeShip(Integer.parseInt(in2), Integer.parseInt(in1), 3, 1);
				String result = CheckPlacementResult(place);
				System.out.println(result);
				//if there is an error in the coordinates inputted, decrease iterator and loop
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
				//otherwise print the grid with the newly placed ship on screen
				else {
					Player.printGrid();
				}
				}
			}
			
			//ALL COMMENTS IN THE IF BLOCK ABOVE ALSO APPLY TO THE NEXT 3 ELSEIF BLOCKS***
			
			
			else if ( shipIterator==4 || shipIterator == 5) {
				System.out.println("place 4x1 ship: Enter x coordinate or type r to rotate the ship.");
				String in1 = placeShip.next();
				if(in1.equals("r")) {
					Player.rotate();
					shipIterator--;
				}
				else {
				System.out.println("Enter y coordinate:");
				String in2 = placeShip.next();
				String place = Player.placeShip(Integer.parseInt(in2), Integer.parseInt(in1), 4, 1);
				String result = CheckPlacementResult(place);
				System.out.println(result);
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
				else {
					Player.printGrid();
				}
				}
			}
			else if ( shipIterator == 6 ) {
				System.out.println("place 5x1 ship: Enter x coordinate or type r to rotate the ship.");
				String in1 = placeShip.next();
				if(in1.equals("r")) {
					Player.rotate();
					shipIterator--;
				}
				else {
				System.out.println("Enter y coordinate:");
				String in2 = placeShip.next();
				String place = Player.placeShip(Integer.parseInt(in2), Integer.parseInt(in1), 5, 1);
				String result = CheckPlacementResult(place);
				System.out.println(result);
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
				else {
					Player.printGrid();
				}
				}
			}
			else if (shipIterator == 7) {
				System.out.println("place 6x1 ship: Enter x coordinate or type r to rotate the ship.");
				String in1 = placeShip.next();
				if(in1.equals("r")) {
					Player.rotate();
					shipIterator--;
				}
				else {
				System.out.println("Enter y coordinate:");
				String in2 = placeShip.next();
				String place = Player.placeShip(Integer.parseInt(in2), Integer.parseInt(in1), 6, 1);
				String result = CheckPlacementResult(place);
				System.out.println(result);
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
				else {
					Player.printGrid();
				}
				}
			}

		}
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Ship placement complete");
		TimeUnit.SECONDS.sleep(1/2);
		System.out.println("The enemy will now place their ships.");
		shipIterator = 1;

	}
	/*
	 * This method automatically places enemy ships upon random selection. Rotation is implemented
	 * Takes (Grid)Enemy to add ships to the grid
	 * Void Method
	 */
	public static void EnemyPlaceShips(Grid Enemy) throws InterruptedException {
		for(; shipIterator <= 7; shipIterator++ ) {
			if(shipIterator < 4) {
				int r = (int)(Math.random()*2) +1; 				//50% chance to rotate the ship.
				int in1 = (int)(Math.random() *14) + 1;			//choose random x coordinate
				
				//check if ship is rotated and rotate if true
				if(r == 1) {
					Enemy.rotate();
				}
				
				int in2 = (int)(Math.random() *14) + 1;			//choose random y coordinate
				String place = Enemy.placeShip(in2, in1, 3, 1);	//check if random coordinates are valid
				String result = CheckPlacementResult(place);	//Generate message, this will not be veiwed onscreen
				
				//check if coordinates failed, decrease iterator if fail
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
			}
			
			//ALL COMMENTS IN THE IF BLOCK ABOVE ALSO APPLY TO THE NEXT 3 ELSEIF BLOCKS***
			
			else if ( shipIterator==4 || shipIterator == 5) {
				int r = (int)(Math.random()*2) +1;
				int in1 = (int)(Math.random() *14) + 1;
				if(r == 1) {
					Enemy.rotate();
				}
				int in2 = (int)(Math.random() *14) + 1;
				String place = Enemy.placeShip(in2, in1, 4, 1);
				String result = CheckPlacementResult(place);
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
			}
			else if ( shipIterator == 6 ) {
				int r = (int)(Math.random()*2) +1;
				int in1 = (int)(Math.random() *14) + 1;
				if(r == 1) {
					Enemy.rotate();
				}
				int in2 = (int)(Math.random() *14) + 1;
				String place = Enemy.placeShip(in2, in1, 5, 1);
				String result = CheckPlacementResult(place);
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
			}
			else if (shipIterator == 7) {
				int r = (int)(Math.random()*2) +1;
				int in1 = (int)(Math.random() *14) + 1;
				if(r == 1) {
					Enemy.rotate();
				}
				int in2 = (int)(Math.random() *14) + 1;
				String place = Enemy.placeShip(in2, in1, 6, 1);
				String result = CheckPlacementResult(place);
				if(result != "Ship was placed successfully.") {
					shipIterator--;
				}
			}

		}
		Enemy.printGrid();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Enemy Ship placement complete");
		TimeUnit.SECONDS.sleep(1/2);
		shipIterator = 1;
	}
	/*
	 * This method generates Strings upon return of the checkPlacementResult() method
	 * Takes (String)s which is the return of the checkPlacementResult() method
	 * Returns String based on result of placement.
	 */
	public static String CheckPlacementResult(String s) {
		// 3 CASES
		
		//if coordinates are out of the 14x14 grid
		if(s.equals("OOB")) {
			return "Ship is out of bounds, choose new coordinates.";
		}
		//if coordinates are already occupied by a ship
		//NOTE: player cannot place 2 ships side by side. Atleast one space should be between ships.
		else if(s.equals("OCC")){
			return "coordinates are already occupied or is too close to another ship, choose new coordinates.";
		}
		//if no error, return success message.
		else {
			return "Ship was placed successfully.";
		}
	}
	
}
