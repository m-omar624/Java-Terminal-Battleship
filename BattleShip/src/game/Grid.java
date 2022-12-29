package game;

public class Grid {

	char[][] grid = new char[14][14];
	boolean vertical = true;
	int recentX;
	int recentY;
	int initialX;
	int initialY;
	boolean targetLock = false;
	int targetLockDirection = 1; //1 is down, 2 is right, 3 is up, 4 is left
	int hit;
	
	public Grid(char[][] grid) {
		this.grid = grid;
		
	}
	public void setGrid(char c) {
		for(int i = 0; i < 14; i++) {
			for(int k = 0; k < 14; k ++) {
				grid[i][k] = c;
			}
		}
	}
	public char[][] getGrid(){
		return grid;
	}
	public void printGrid() {
		for(int i = 0; i < 14; i++) {
			for(int k = 0; k < 14; k ++) {
				System.out.print(grid[i][k]+ "  ");
			}
			System.out.println("|"+(i+1));
		}
		
	for(int i = 0; i<42; i++) {
		System.out.print("_");
	}
	System.out.println();
	for(int i = 1; i<10; i++) {
		System.out.print(i + "  ");
	}
	for(int i = 10; i<15; i++) {
		System.out.print(i + " ");
	}
	System.out.println();
	}


	public void rotate() {
		if(vertical == true) {
			vertical = false;
		}
		else {
			vertical = true;
		}
	}
	
	public String placeShip(int x, int y, int length, int width) {
		x--; y--;
		if(checkOutOfBounds(x,y,length,width)==true) {
			return "OOB";
		}
		else if(checkOccupied(x,y,length,width)==true) {
				return "OCC";
		}
		else {
			if(vertical == true) {
				for(int i = 0; i<width; i++) {
					for(int k = 0; k < length; k++) {
						grid[x][y] = 'X';
						x++;
					}
					x-=length;
					y++;
				}
			}
			else {
				for(int i = 0; i<width; i++) {
					for(int k = 0; k < length; k++) {
						grid[x][y] = 'X';
						y++;
					}
					y-=length;
					x++;
				}
			}
			return "SUCCESS";
		}

	}

	public boolean checkOutOfBounds(int x, int y, int length, int width) {
		boolean fail= false;
		if(vertical == true) {
			if(x+length > 14) {
				fail = true;
			}
			if(y+width > 14) {
				fail = true;
			}
		}
		else {
			if(y+length > 14) {
				fail = true;
			}
			if(x+width > 14) {
				fail = true;
			}
		}
		return fail;
	}
		
	public boolean checkOccupied(int x, int y, int length, int width) {
		boolean fail = false;
		int a=0; int b=0;
		if(vertical == true) {
			char check[][] = new char[length +2][width+2];
			for( int i = x-1; i < x+(length+1); i++  ) {
				for( int k = y-1; k < y+(width+1); k++  ) {
					if((i >= 0 && i <14) && (k >=0 && k < 14) ) {
						check[a][b] = grid[i][k];
						b++;
					}
				}
				b=0;
				a++;
			}
			for(int i = 0; i < length+2; i++) {
				for(int k = 0; k < width+2; k++) {
					if(check[i][k] == 'X') {
						fail = true;
					}
				}
			}	
		}
		else {
			char check[][] = new char[width +2][length+2];
			for( int i = x-1; i < x+(width+1); i++  ) {
				for( int k = y-1; k < y+(length+1); k++  ) {
					if((i >= 0 && i <14) && (k >=0 && k < 14) ) {
						check[a][b] = grid[i][k];
						b++;
					}
				}
				b=0;
				a++;
			}
			for(int i = 0; i < width+2; i++) {
				for(int k = 0; k < length+2; k++) {
					if(check[i][k] == 'X') {
						fail = true;
					}
				}
			}	
		}
		return fail;
	}

	public String playerMove(int y, int x, Grid g) {
		if(grid[x][y] == 'X') {
			g.getGrid()[x][y] = '!';
			this.hit ++;
			checkSink(x,y,g);
			return "HIT!";
		}
		else {
			g.getGrid()[x][y] = '.';
			return "Miss...";
		}
	}
	public void Width1HorizontalSink (int x, int y, Grid g) {
		int start=0; int end;
		boolean sink = true;
		
		while(y > -1 && this.grid[x][y] == 'X') {
			y--;
			start = y;
		}
		y++;
		while(y < 13 && this.grid[x][y] == 'X') {
			if(g.getGrid()[x][y] != '!') {
				sink = false;
			}
			y++;
	}
		if(this.grid[x][y] == 'X' && y == 13) {
			if(g.getGrid()[x][y] != '!') {
				sink = false;
			}
			y++;
		}
		
		if(sink == true) {
			this.targetLock = false;
			end = y;
			y = start;
			if(y >= 0 && y <14) {
				if(x-1 >=0 ) {
					g.getGrid()[x-1][y] = '.';
				}
				g.getGrid()[x][y] = '.';
				if( x+1 < 14) {
					g.getGrid()[x+1][y] = '.';
				}
			}
			y++;
			while(y < end) {
				if((y >= 0 && y <14) && x+1 < 14 ) {
					g.getGrid()[x+1][y] = '.';
				}
				if((y >= 0 && y <14) && x-1 >=0) {
					g.getGrid()[x-1][y] = '.';
				}
				y++;
		}
			if(y >= 0 && y <14) {
				if(x-1 >=0 ) {
					g.getGrid()[x-1][y] = '.';
				}
				g.getGrid()[x][y] = '.';
				if( x+1 < 14) {
					g.getGrid()[x+1][y] = '.';
				}
			}
		}
	}
	
	public void Width1VerticalSink (int x, int y, Grid g) {
		int start = 0; int end;
		boolean sink = true;
		while(x > -1 && this.grid[x][y] == 'X') {
			x--;
			start = x;
		}
		x++;
		while(this.grid[x][y] == 'X' && x < 13) {
			if(g.getGrid()[x][y] != '!') {
				sink = false;
			}
			x++;
			
		}
		if(this.grid[x][y] == 'X' && x == 13) {
			if(g.getGrid()[x][y] != '!') {
				sink = false;
			}
			x++;
		}

		if(sink == true) {
			this.targetLock = false;
			end = x;
			x = start;
			if(x >= 0 && x <14) {
				if(y-1 >=0 ) {
					g.getGrid()[x][y-1] = '.';
				}
					g.getGrid()[x][y] = '.';
				if( y+1 < 14 ) {
					g.getGrid()[x][y+1] = '.';
				}
			}
			x++;
			while(x < end) {
				if((x >= 0 && x <14) && y+1 < 14) {
					g.getGrid()[x][y+1] = '.';
				}
				if((x >= 0 && x <14) && y-1 >=0 ) {
					g.getGrid()[x][y-1] = '.';
				}
				x++;
		}
			if(x >= 0 && x <14) {
				if(y-1 >=0 ) {
					g.getGrid()[x][y-1] = '.';
				}
					g.getGrid()[x][y] = '.';
				if( y+1 < 14 ) {
					g.getGrid()[x][y+1] = '.';
				}
			}
		}
	}
	
	public void checkSink(int x, int y,  Grid g) {

		if(x == 0 && y == 0) {
			if(this.grid[x+1][y] == 'X' && this.grid[x][y+1] != 'X') {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if(x == 0 && y == 13) {
			if(this.grid[x+1][y] == 'X' && this.grid[x][y-1] != 'X') {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if(x == 13 && y == 13) {
			if(this.grid[x-1][y] == 'X' && this.grid[x][y-1] != 'X') {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if(x == 13 && y == 0) {
			if(this.grid[x-1][y] == 'X' && this.grid[x][y+1] != 'X') {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if((y>0 && y<13) && x == 0) {
			if(this.grid[x+1][y] == 'X' && (this.grid[x][y+1] != 'X')&& (this.grid[x][y-1] != 'X')) {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if((y>0 && y<13) && x == 13) {
			if(this.grid[x-1][y] == 'X' && (this.grid[x][y+1] != 'X')&& (this.grid[x][y-1] != 'X')) {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
	
		if(y == 0 && (x>0 && x<13)) {
			if(this.grid[x][y+1] != 'X') {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if(y == 13 && (x>0 && x<13)) {
			if(this.grid[x][y-1] != 'X') {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
		
		if((x>0 && x<13) && (y>0 && y<13)) {
			if((this.grid[x+1][y] == 'X' || this.grid[x-1][y] == 'X') && (this.grid[x][y+1] != 'X' && this.grid[x][y-1] != 'X')) {
				Width1VerticalSink(x,y,g);
			}
			else {
				Width1HorizontalSink(x,y,g);
			}
		}
	}

	
	public String enemyMove(Grid g) {
		//1 is down, 2 is right, 3 is up, 4 is left
		int x=0; int y=0;
		if(this.targetLock == false) {
			x = (int)(Math.random() *13) + 0;
			y = (int)(Math.random() *13) + 0;
			while (this.grid[x][y] == '.' || this.grid[x][y] == '!') {
				x = (int)(Math.random() *13) + 0;
				y = (int)(Math.random() *13) + 0;
			}
			this.initialX = x;
			this.initialY = y;
			this.recentX = x;
			this.recentY = y;
		}
		else {
			if(this.targetLockDirection == 1) {
				if(this.recentX == 13) {
					this.targetLockDirection = 2;
					this.recentX = this.initialX;
					this.recentY = this.initialY;
				}
				else {
					x = recentX+1;
					y = recentY;
					this.recentX = x;
					this.recentY = y;
				}
			}
			if(this.targetLockDirection == 2) {
				if(this.recentX == 0) {
					this.targetLockDirection = 3;
					this.recentX = this.initialX;
					this.recentY = this.initialY;
				}
				else {
					x = recentX-1;
					y = recentY;
					this.recentX = x;
					this.recentY = y;
				}
			}
			if(this.targetLockDirection == 3) {
				if(this.recentY == 13) {
					this.targetLockDirection = 4;
					this.recentX = this.initialX;
					this.recentY = this.initialY;
				}
				else {
					x = recentX;
					y = recentY+1;
					this.recentX = x;
					this.recentY = y;
				}
			}
			if(this.targetLockDirection == 4) {
				if(this.recentY == 0) {
					this.targetLockDirection = 1;
					this.recentX = this.initialX;
					this.recentY = this.initialY;
				}
				else {
					x = recentX;
					y = recentY-1;
					this.recentX = x;
					this.recentY = y;
				}
			}
			
		}
		if(this.grid[x][y] == 'X') {
			this.targetLock = true;
			this.hit++;
			g.grid[x][y] = '!';
			checkSink(x,y,g);
			return "HIT";
		}
		else {
			this.recentX = this.initialX;
			this.recentY = this.initialY;
			if(this.targetLockDirection == 4) {
				this.targetLockDirection = 1;
			}
			else {
				this.targetLockDirection ++;
			}
			g.grid[x][y] = '.';
			return "Miss...";
		}
		
	}

	public int checkCoordsError(int x, int y) {
		if((x < 0 || x > 13) || (y < 0 || y > 13 )) {
			return 1;
		}
		else if(this.grid[y][x] == '!' || this.grid[y][x] == '.') {
			return 2;
		}
		else {
			return 0;
		}
	}
}
