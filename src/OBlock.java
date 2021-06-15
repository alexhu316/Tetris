//Alex Hu
//June 13 2021 
//The OBlock object is a Block in the shape of an O
public class OBlock extends Block{

	//Constructor
	public OBlock() {
		super(1);
		resetList();
	}	

	//Implements the Block method's resetList() method specific to the shape of the block
	@Override
	void resetList() {
		int[]arr = {getX(),getY()};
		getOccupies()[0]=(arr);
		int[]arr1 = {getX()+1,getY()};
		getOccupies()[1]=(arr1);
		int[] arr2 = {getX()+1,getY()-1};
		getOccupies()[2]=(arr2);
		int[]arr3 = {getX(),getY()-1};
		getOccupies()[3]=(arr3);

	}
}
