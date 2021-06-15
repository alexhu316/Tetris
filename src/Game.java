//Alex Hu
//June 13 2021 
//A Game object stores the name and durationg of a single-player game
public class Game implements Comparable<Game> {

	//name, duration
	private String name;
	private int timeMillis;

	//Constructor
	public Game(String name, int time) {
		this.name=name;
		this.timeMillis=Math.min(time, 3599999);
	}

	//Parameters: none
	//Return: String
	//Description: returns the time written in xx:xx.xxxs format
	public String timeString() {
		return (String.format("%02d:%06.3fs", (timeMillis)/60000, (timeMillis)/1000.0%60));
	}

	//Parameters: Game o
	//Return: int
	//Description: compares two games by their name
	@Override
	public int compareTo(Game o) {
		if (this.name.toLowerCase().compareTo(o.name.toLowerCase())==0) {
			return this.timeMillis-o.timeMillis;
		}
		return this.name.toLowerCase().compareTo(o.name.toLowerCase());
	}

	//Getter methods
	public String getName() {
		return name;
	}
	public int getTime() {
		return timeMillis;
	}
}		
