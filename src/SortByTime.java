//Alex Hu
//June 13 2021 
//The SortByTime class compares two Game objects by their duration 

import java.util.Comparator;

public class SortByTime implements Comparator<Game>{


	//Parameters: Game o1, Game o2
	//Return: int
	//Description: Compares two games by their duration
	@Override
	public int compare(Game o1, Game o2) {
		if (o1.getTime()==o2.getTime()){
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
		return o1.getTime()-o2.getTime();	
	}
}
