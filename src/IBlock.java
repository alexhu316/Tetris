//Alex Hu
//June 13 2021 
//The IBlock object is a Block in the shape of an I
public class IBlock extends Block{

	//Constructor
	public IBlock() {
		super(7);
		resetList();

	}	

	//Parameters: int[][] matrix
	//Return: void
	//Description: overrides the Block method's rotateLeft()
	void rotateLeft(int[][] matrix) {

		setRot((getRot()+3)%4);
		resetList();

		if (getRot()==0) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(2);
				}
				else if (i==1) {
					setX(-3);
				}
				else if (i==2) {
					setX(3);
					setY(-1);
				}
				else if (i==3) {
					setX(-3);
					setY(3);
				}
				else if (i==4) {
					setX(1);
					setY(-2);
					setRot((getRot()+1)%4);
				}
				resetList();
			}
		}


		else if (getRot()==1) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(1);
				}
				else if (i==1) {
					setX(-3);
				}
				else if (i==2) {
					setY(2);
					setX(3);
				}
				else if (i==3) {
					setX(-3);
					setY(-3);
				}
				else if (i==4) {
					setX(2);
					setY(1);
					setRot((getRot()+1)%4);
				}
				resetList();
			}
		}

		else if (getRot()==2) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(-2);
				}
				else if (i==1) {
					setX(3);
				}
				else if (i==2) {
					setY(1);
					setX(-3);
				}
				else if (i==3) {
					setX(3);
					setY(-3);
				}
				else if (i==4) {
					setX(-1);
					setY(2);
					setRot((getRot()+1)%4);
				}
				resetList();
			}
		}
		else if (getRot()==3) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(-1);
				}
				else if (i==1) {
					setX(3);
				}
				else if (i==2) {
					setY(-2);
					setX(-3);
				}
				else if (i==3) {
					setX(3);
					setY(3);
				}
				else if (i==4) {
					setX(-2);
					setY(-1);
					setRot((getRot()+1)%4);
				}
				resetList();
			}
		}

	}

	//Parameters: int[][] matrix
	//Return: void
	//Description: overrides the Block method's rotateRight()
	void rotateRight(int[][] matrix) {
		setRot((getRot()+1)%4);
		resetList();

		if (getRot()==1) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(-2);
				}
				else if (i==1) {
					setX(3);
				}
				else if (i==2) {
					setY(1);
					setX(-3);
				}
				else if (i==3) {
					setX(3);
					setY(-3);
				}
				else if (i==4) {
					setX(-1);
					setY(2);
					setRot((getRot()+3)%4);	
				}
				resetList();
			}
		}

		else if (getRot()==2) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(-1);
				}
				else if (i==1) {
					setX(3);
				}
				else if (i==2) {
					setY(-2);
					setX(-3);
				}
				else if (i==3) {
					setX(3);
					setY(3);
				}
				else if (i==4) {
					setX(-2);
					setY(-1);
					setRot((getRot()+3)%4);	
					}
				resetList();
			}
		}

		else if (getRot()==3) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(2);
				}
				else if (i==1) {
					setX(-3);
				}
				else if (i==2) {
					setX(3);
					setY(-1);
				}
				else if (i==3) {
					setX(-3);
					setY(3);
				}
				else if (i==4) {
					setX(1);
					setY(-2);
					setRot((getRot()+3)%4);	
				}
				resetList();
			}
		}
		else if (getRot()==0) {

			for (int i =0; i<5; i++) {
				boolean done = true;
				try {
					for (int[] arr : getOccupies()) {
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
					setX(1);
				}
				else if (i==1) {
					setX(-3);
				}
				else if (i==2) {
					setX(3);
					setY(2);
				}
				else if (i==3) {
					setX(-3);
					setY(-3);
				}
				else if (i==4) {
					setX(2);
					setY(1);
					setRot((getRot()+3)%4);	
				}
				resetList();
			}
		}
	}
	
	
	//Implements the Block method's resetList() method specific to the shape of the block
	@Override
	void resetList() {

		if (getRot()==0) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX()-1,getY()};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX()+2,getY()};
			getOccupies()[3]=(arr3);			
		}
		else if (getRot()==1) {
			int[]arr = {getX()+1,getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()+1};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX()+1,getY()-1};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX()+1,getY()-2};
			getOccupies()[3]=(arr3);			
		}
		else if (getRot()==2) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX()-1,getY()};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX()-2,getY()};
			getOccupies()[3]=(arr3);			
		}
		else if (getRot()==3) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX(),getY()+1};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX(),getY()-1};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX(),getY()+2};
			getOccupies()[3]=(arr3);			
		}
	}


}
