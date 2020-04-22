package GridWorld;

/**
			INTELLIGENCE LAB
	course		: 	COMP 417 - Artificial Intelligence
	authors		:	A. Vogiatzis, N. Trigkas
	excercise	:	1st Programming
	term 		: 	Spring 2019-2020
	date 		:   March 2020
*/
class Cell {
	private int cost=1;
	private int heuristic=0;
	private int gCost=0;
	private int LRTAgCost=0;
	private int LRTAfCost=0;
	private int fCost=0;
	private Cell Parent;
	int position;

	private boolean starting_point;
	private boolean terminal_point;
	private char cell_type = 'L'; // l stands for Land

	Cell (){		
		this.starting_point = false;
		this.terminal_point = false;
	}

	Cell(char cell_type, boolean starting_point, boolean terminal_point, int world_cost){
		if(cell_type!= 'L' && cell_type!= 'W' && cell_type!= 'G'){
			System.out.println("Unknown type of cell. This cell is set to Land!");
			cell_type='L';
			world_cost = 1;
		}
		this.cell_type = cell_type;
		this.starting_point = starting_point;
		this.terminal_point = terminal_point;
		this.cost = world_cost;
	}

	public boolean isWall(){return  (this.cell_type=='W');}
	public boolean isGrass(){return  (this.cell_type=='G');}
	public boolean isLand(){return (this.cell_type=='L');}

	public boolean isStart(){return this.starting_point;}
	public boolean isTerminal(){return this.terminal_point;}

	public int getCost(){return this.cost;}

	public void changeCellType(char cell_type, int world_cost){
		if(cell_type!= 'L' && cell_type!= 'W' && cell_type!= 'G'){
			System.out.println("Unknown type of cell. This cell is set to Land!");
			cell_type='L';
			world_cost = 1;
		}
		this.cell_type = cell_type;
		this.cost = world_cost;
	}

	public int calculategCosts(Cell parent) {
		return parent.getgCost()+this.getCost();
	}

	public char getCellType(){return this.cell_type;}
	public int getHeuristic(){return this.heuristic;}
	public int getfCost(){return heuristic+gCost;}
	public int getgCost(){return this.gCost;}
	public void setParent(Cell c1){ this.Parent=c1;}
	public void setPosition(int pos) {this.position=pos;}
	public int getLRTAgCost(){return this.getParent().getgCost() + 1;}
	public int getLRTAfCost(){return this.heuristic+this.LRTAgCost;}

	
	public void setStartingPoint(boolean sp){this.starting_point=sp;}
	public void setTerminalPoint(boolean sp){this.terminal_point=sp;}
	public void setHeuristic(int h){this.heuristic=h;}
	public void setfCost(){this.fCost=this.heuristic+this.gCost;}
	public void setgCost(){this.gCost=this.getParent().getgCost()+this.getCost();}
	public Cell getParent(){ return this.Parent;}
	public int getPosition() {return this.position;}
	public void setLRTAgCost(){this.LRTAgCost=getParent().getgCost()+1;}


}