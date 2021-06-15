//Alex Hu
//June 13 2021 
//The JBlock object is a Block in the shape of a J
public class JBlock extends Block{

	//Constructor
	public JBlock() {
		super(3);
		resetList();
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
			int[]arr3 = {getX()-1,getY()-1};
			getOccupies()[3]=(arr3);			
		}
		else if (getRot()==1) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX(),getY()+1};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX(),getY()-1};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX()+1,getY()-1};
			getOccupies()[3]=(arr3);			
		}
		else if (getRot()==2) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX()+1,getY()};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX()-1,getY()};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX()+1,getY()+1};
			getOccupies()[3]=(arr3);			
		}
		else if (getRot()==3) {
			int[]arr = {getX(),getY()};
			getOccupies()[0]=(arr);
			int[]arr1 = {getX(),getY()+1};
			getOccupies()[1]=(arr1);
			int[] arr2 = {getX(),getY()-1};
			getOccupies()[2]=(arr2);
			int[]arr3 = {getX()-1,getY()+1};
			getOccupies()[3]=(arr3);			
		}
		
	}
}
