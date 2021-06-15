//Alex Hu
//June 13 2021 
//A Block object is an abtract class that is a parent class to seven subclasses.

public abstract class Block {

	
	//position, rotation, and type of block
	private int x=4;
	private int y=3;
	private int rotation=0;
	private int id=0;
	/*
	 * BLOCK IDS
	 * 1 - OBlock
	 * 2 - LBlock
	 * 3 - JBlock
	 * 4 - SBlock
	 * 5 - ZBlock
	 * 6 - TBlock
	 * 7 - IBlock
	 */
	
	//the four coordinates the block occupies
	private int[][] occupies = new int[4][2];


	//Constructor
	public Block (int id) {
		this.id=id;
	}

	
	//Parameters: int[][] matrix
	//Return: void
	//Description: Rotates the block counterclockwise within the given matrix
	void rotateLeft(int[][] matrix) {

		rotation = (rotation +3)%4;
		resetList();
		
		//from an initial rotational position of 0
		if (rotation==0) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x+=1;
				}
				else if (i==1) {
					y+=1;
				}
				else if (i==2) {
					y-=3;
					x-=1;
				}
				else if (i==3) {
					x+=1;
				}
				else if (i==4) {
					x+=1;
					y-=2;
					rotation = (rotation+1)%4;
				}
				resetList();
			}
		}

		//from an initial rotational position of 1
		else if (rotation==1) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x-=1;
				}
				else if (i==1) {
					y-=1;
				}
				else if (i==2) {
					y+=3;
					x+=1;
				}
				else if (i==3) {
					x-=1;
				}
				else if (i==4) {
					x+=1;
					y-=2;
					rotation = (rotation+1)%4;
				}
				resetList();
			}
		}

		//from an initial rotational position of 2
		else if (rotation==2) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x-=1;
				}
				else if (i==1) {
					y+=1;
				}
				else if (i==2) {
					y-=3;
					x+=1;
				}
				else if (i==3) {
					x-=1;
				}
				else if (i==4) {
					x+=1;
					y+=2;
					rotation = (rotation+1)%4;
				}
				resetList();
			}
		}
		
		//from an initial rotational position of 3
		else if (rotation==3) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x+=1;
				}
				else if (i==1) {
					y-=1;
				}
				else if (i==2) {
					y+=3;
					x-=1;
				}
				else if (i==3) {
					x+=1;
				}
				else if (i==4) {
					x-=1;
					y-=2;
					rotation = (rotation+1)%4;
				}
				resetList();
			}
		}

	}

	//Parameters: int[][] matrix
	//Return: void
	//Description: Rotates the block clockwise within the given matrix
	void rotateRight(int[][] matrix) {
		rotation = (rotation+1)%4;
		resetList();

		//from an initial rotational position of 1
		if (rotation==1) {
			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x-=1;
				}
				else if (i==1) {
					y-=1;
				}
				else if (i==2) {
					y+=3;
					x+=1;
				}
				else if (i==3) {
					x-=1;
				}
				else if (i==4) {
					x+=1;
					y-=2;
					rotation = (rotation+3)%4;
				}
				resetList();
			}
		}

		//from an initial rotational position of 2
		else if (rotation==2) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x+=1;
				}
				else if (i==1) {
					y+=1;
				}
				else if (i==2) {
					y-=3;
					x-=1;
				}
				else if (i==3) {
					x+=1;
				}
				else if (i==4) {
					x-=1;
					y+=2;
					rotation = (rotation+3)%4;
				}
				resetList();
			}
		}

		//from an initial rotational position of 3
		else if (rotation==3) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x+=1;
				}
				else if (i==1) {
					y-=1;
				}
				else if (i==2) {
					y+=3;
					x-=1;
				}
				else if (i==3) {
					x+=1;
				}
				else if (i==4) {
					x-=1;
					y-=2;
					rotation = (rotation+3)%4;
				}
				resetList();
			}
		}
		
		//from an initial rotational position of 0
		else if (rotation==0) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : occupies) {
						if (matrix[arr[0]][Math.max(arr[1]-4,0)] !=0) {
							done=false;
							break;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					done=false;
				}
				if (done) {
					return;
				}
				if (i==0) {
					x-=1;
				}
				else if (i==1) {
					y+=1;
				}
				else if (i==2) {
					y-=3;
					x+=1;
				}
				else if (i==3) {
					x-=1;
				}
				else if (i==4) {
					x+=1;
					y+=2;
					rotation = (rotation+3)%4;
				}
				resetList();
			}
		}
	}


	//Parameters: none
	//Return: void
	//Description: moves a block in that direction
	void move(int d) {
		if (d==0) {
			y++;
		}
		else if (d==1) {
			x--;
		}
		else if (d==2) {
			x++;
		}
		else if (d==3) {
			y--;
		}
		resetList();

	}	

	//Parameters: none
	//Return: void
	//Description: resets the 'int[][] occupies' of this block after being moved 
	abstract void resetList();


	//Getter and Setter methods
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getID() {
		return id;
	}
	public int getRot() {
		return rotation;
	}
	public int[][] getOccupies() {
		return occupies;
	}
	public void setX(int i) {
		x+=i;
	}
	public void setY(int i) {
		y+=i;
	}
	public void setID(int i) {
		id=i;
	}
	public void setRot(int i) {
		rotation=i;
	}

	
}