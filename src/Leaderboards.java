//Alex Hu
//June 13 2021 
//The Leaderboards class displays all games played, sorted by a criteria of the user's choice
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.*;

public class Leaderboards extends JPanel implements ActionListener{

	//JComponents
	private static JFrame frame;
	private static JPanel panel;
	private JPanel gamePanel;
	private JComboBox <String> sortCriteriaBox;
	private Vector<String> sortCriteria;
	private JLabel leaderLabel, criteriaLabel;
	private JScrollPane gameScroller;
	//All the games
	private ArrayList<Game> games = new ArrayList<Game>();


	//Constructor
	public Leaderboards() {

		//Initializes panel
		setBackground(Color.black);
		setPreferredSize(new Dimension(400,500));
		setLocation(100,50);
		setLayout(null);

		//Initializes JComponents
		sortCriteria = new Vector<String>();
		sortCriteria.add("By Time, Fastest to Slowest");
		sortCriteria.add("By Time, Slowest to Fastest");
		sortCriteria.add("By Name, A to Z");
		sortCriteria.add("By Name, Z to A");

		sortCriteriaBox = new JComboBox(sortCriteria);
		sortCriteriaBox.setBounds(50,100,300,50);
		sortCriteriaBox.setForeground(Color.white);
		sortCriteriaBox.setBackground(Color.black);
		sortCriteriaBox.addActionListener(this);

		criteriaLabel = new JLabel("Sort By:");
		criteriaLabel.setFont(new Font("Comic Sans MS", 0, 15));
		criteriaLabel.setForeground(Color.white);
		criteriaLabel.setBounds(175,85,100,25);

		leaderLabel = new JLabel("Leaderboards");
		leaderLabel.setFont(new Font("Comic Sans MS", 0, 40));
		leaderLabel.setForeground(Color.white);
		leaderLabel.setBounds(70,15,300,75);


		//Reads in the leaderboards text file
		try {
			Scanner inFile = new Scanner (new File ("leaderboards.txt"));
			while (inFile.hasNextLine()) {
				String gameInfo = inFile.nextLine();
				try {
					addToList(new Game(gameInfo.substring(gameInfo.indexOf(' ')+1, gameInfo.length()), Integer.parseInt(gameInfo.substring(0, gameInfo.indexOf(' ')))));
				}
				catch (StringIndexOutOfBoundsException | NumberFormatException e) {

				}
			}
		} catch (FileNotFoundException e) {

		}

		switchLabels();

		//Adds JComponents to panel
		add(gameScroller);
		add(sortCriteriaBox);
		add(criteriaLabel);
		add(leaderLabel);	
	}		


	//Main Method
	public static void main (String[] args) {

		frame = new JFrame ("Leaderboards");
		panel = new Leaderboards();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String selected = (String) sortCriteriaBox.getSelectedItem();

		//Sorts the list of games by the selected criteria 

		if (selected.equals("By Name, A to Z")) {
			Collections.sort(games);
		}
		else if (selected.equals("By Name, Z to A")) {
			Collections.sort(games, Collections.reverseOrder());
		}
		else if(selected.equals("By Time, Fastest to Slowest")) {
			Collections.sort(games, new SortByTime());

		}
		else if (selected.equals("By Time, Slowest to Fastest")) {
			Collections.sort(games, Collections.reverseOrder(new SortByTime()));
		}

		switchLabels();
	}


	//Parameters: none
	//Return: void
	//Description: Redisplays the list based on the order it was last sorted
	public void switchLabels() {

		gamePanel = new JPanel();
		gamePanel.setLayout(new BoxLayout(gamePanel,BoxLayout.PAGE_AXIS));
		gamePanel.setBackground(Color.black);
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

		for (Game g : games) {
			int spaces = 18 - g.getName().length();
			String s = g.getName();
			for (int i = 0; i<spaces; i++) {
				s+=" ";
			}
			s+=g.timeString();
			JLabel label = new JLabel(s);
			label.setForeground(Color.white);
			label.setFont(new Font("Courier", 0, 14));
			gamePanel.add(label);
			gamePanel.add(Box.createRigidArea(new Dimension(0,4)));
		}

		try {
			gameScroller.setViewportView(gamePanel);
		}
		catch (NullPointerException e1) {
			gameScroller = new JScrollPane (gamePanel);
			gameScroller.setBounds(65, 150, 270, 300);
		}
	}

	//Parameters: Game g
	//Return: void
	//Description: Adds a game into the list by its duration
	public void addToList(Game g) {
		int index = Collections.binarySearch(games, g, new SortByTime());
		if (index<0) 
			games.add(0-index-1,g);
		else
			games.add(index,g);
	}
}
