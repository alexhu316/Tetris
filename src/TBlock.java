//Alex Hu
//June 13 2021 
//The TBlock object is a Block in the shape of a T
public class TBlock extends Block{

	//Constructor
	public TBlock() {
		super(6);
		resetList();
	}	

	//Implements the Block method's resetList() method specific to the shape of the block
	void resetList() {

		if (getRot()==0) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX()-1,getY()};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX(),getY()-1};
			getOccupies()[3]=(arr3);		
		}
		else if (getRot()==1) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX(),getY()+1};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX(),getY()-1};
			getOccupies()[3]=(arr3);		
		}
		else if (getRot()==2) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX()-1,getY()};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX(),getY()+1};
			getOccupies()[3]=(arr3);		
		}
		else if (getRot()==3) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()-1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX(),getY()+1};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX(),getY()-1};
			getOccupies()[3]=(arr3);		
		}
	}
}