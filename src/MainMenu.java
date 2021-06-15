//Alex Hu
//June 13 2021 
//The MainMenu class is the Driver class of the project, displaying information for 
//the user and allowing them to pick a feature to access
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class MainMenu extends JPanel implements ActionListener{

	//JFrame and JPanel for this class
	private static JPanel panel;
	private static JFrame frame;	

	//instances of JComponents for other classes
	private JPanel tetrisPanel, controlPanel, versusPanel, instrucPanel, leaderPanel;
	private static JFrame tetrisFrame;
	private JFrame controlFrame;
	private static JFrame versusFrame;
	private JFrame instrucFrame;
	private JFrame leaderFrame;	
	private JLabel tetrisLabel, loopLabel;
	private JLabel instructions;
	
	//Allows the instructions menu game to be animated
	private Timer animation;
	private int frameNo = 1;
	
	//Buttons to access other menus
	private JButton soloButton, versusButton, controlButton, instrucButton, leaderButton, aboutButton;
	private JButton closeInstrucButton;

	
	//Constructor
	public MainMenu() {

		setPreferredSize(new Dimension(550,625));
		setLayout(null);
		
		//Initializes all JButtons and JLabels
		tetrisLabel = new JLabel ("TETRIS");
		tetrisLabel.setForeground(Color.white);
		tetrisLabel.setFont(new Font("Comic Sans MS", 0, 90));
		tetrisLabel.setBounds(100,25,450,200);

		soloButton = new JButton ("New Solo Game");
		soloButton.setBounds(50, 225,200,50);
		soloButton.addActionListener(this);
		soloButton.setActionCommand("Solo");
		soloButton.setForeground(Color.white);

		versusButton = new JButton ("New Versus Game");
		versusButton.setBounds(300, 225,200,50);
		versusButton.addActionListener(this);
		versusButton.setActionCommand("Versus");
		versusButton.setForeground(Color.white);

		controlButton = new JButton ("Change Controls");
		controlButton.setBounds(200, 325,150,50);
		controlButton.addActionListener(this);
		controlButton.setActionCommand("Control");
		controlButton.setForeground(Color.white);

		instrucButton = new JButton ("Instructions");
		instrucButton.setBounds(100, 425,150,50);
		instrucButton.addActionListener(this);
		instrucButton.setActionCommand("Instruc");
		instrucButton.setForeground(Color.white);
		
		leaderButton = new JButton ("Leaderboards");
		leaderButton.setBounds(300, 425,150,50);
		leaderButton.addActionListener(this);
		leaderButton.setActionCommand("Leader");
		leaderButton.setForeground(Color.white);

		aboutButton = new JButton ("About");
		aboutButton.setBounds(225, 525, 100, 50);
		aboutButton.addActionListener(this);
		aboutButton.setActionCommand("About");
		aboutButton.setForeground(Color.white);
		
		//Adds JComponents to the JPanel
		add(tetrisLabel);
		add(soloButton);
		add(versusButton);
		add(controlButton);
		add(instrucButton);
		add(leaderButton);
		add(aboutButton);
		
		
	}

	//Main method
	public static void main (String[] args) {

		frame = new JFrame("Tetris");
		panel = new MainMenu();

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon icon = new ImageIcon("Images/background.png");
		Image image = (icon).getImage();
        g.drawImage(image, 0, 0, 550,625, null);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();

		
		//Creates an instance of TetrisMain
		if (event.equals("Solo")) {
			try {
				tetrisFrame.dispose();
				tetrisFrame.setVisible (false);	
			}
			catch(NullPointerException e1) {

			}
			tetrisFrame = new JFrame ("Tetris");
			tetrisPanel = new TetrisMain();
			tetrisFrame.add (tetrisPanel);
			tetrisFrame.addKeyListener((KeyListener) tetrisPanel);
			tetrisFrame.pack ();
			tetrisFrame.setVisible (true);
		}

		//Creates an instance of ControlCustomizer
		else if (event.equals("Control")) {
			try {
				controlFrame.dispose();
				controlFrame.setVisible (false);	
			}
			catch(NullPointerException e1) {
			}
			
			controlFrame = new JFrame ("Control Customizer");
			controlPanel = new ControlCustomizer();
			controlFrame.add (controlPanel);
			controlFrame.addKeyListener((KeyListener)controlPanel);
			controlFrame.pack ();
			controlFrame.setVisible (true);
		}
		
		//Creates an instance of TetrisVersus
		else if (event.equals("Versus")) {
			try {
				versusFrame.dispose();
				versusFrame.setVisible (false);	
			}
			catch(NullPointerException e1) {

			}
			versusFrame = new JFrame ("Tetris Versus");
			versusPanel = new TetrisVersus();
			versusFrame.add (versusPanel);
			versusFrame.addKeyListener((KeyListener)versusPanel);
			versusFrame.pack ();
			versusFrame.setVisible (true);
		}
		
		//Creates the Instructions menu
		else if (event.equals("Instruc")){
			try {
				instrucFrame.dispose();
				instrucFrame.setVisible (false);
				animation.stop();
				frameNo=0;
			}
			catch(NullPointerException e1) {

			}
			instrucFrame = new JFrame ("Instructions");
			instrucPanel = new JPanel();
			instrucPanel.setPreferredSize(new Dimension(650,600));
			instrucPanel.setLocation(100, 100);
			instrucPanel.setLayout(null);
			instrucPanel.setBackground(Color.black);

			instructions = new JLabel("<html>Place pieces. Clear lines. Don't top out.<br/><br/>"
					+ "Solo: Clear 40 lines as fast as you can. <br/>"
					+ "Versus: Survive longer than your opponent.<br/><br/>"
					+ "Default controls are arrow keys to move, up-arrow to rotate, spacebar to drop, "
					+ "shift key to hold. Feel free to customize controls for both players.</html>");
			instructions.setFont(new Font ("Comic Sans MS", 0, 15));
			instructions.setForeground(Color.white);
			instructions.setBounds(25, 120,300,300);

			closeInstrucButton = new JButton ("OK");
			closeInstrucButton.setActionCommand("Close Instruc");
			closeInstrucButton.addActionListener(this);
			closeInstrucButton.setBounds(137,420,75,50);

			loopLabel= new JLabel(new ImageIcon(new ImageIcon("Images/ezgif-frame-001.png").getImage().getScaledInstance(250, 500, Image.SCALE_DEFAULT)));
			loopLabel.setBounds(350,50,250,500);

			instrucPanel.add(instructions);
			instrucPanel.add(closeInstrucButton);
			instrucPanel.add(loopLabel);

			instrucFrame.add(instrucPanel);
			instrucFrame.pack ();
			instrucFrame.setVisible (true);

			//Manages the animation
			animation = new Timer(100, this);
			animation.addActionListener(this);
			animation.setActionCommand("Animate");
			animation.start();
		
		}
		
		//Creates an instance of Leaderboards
		else if (event.equals("Leader")){
			try {
				leaderFrame.dispose();
				leaderFrame.setVisible (false);	
			}
			catch(NullPointerException e1) {

			}
			leaderFrame = new JFrame ("Leaderboards");
			leaderPanel = new Leaderboards();
			leaderFrame.add (leaderPanel);
			leaderFrame.pack ();
			leaderFrame.setVisible (true);
		}
		
		//JButton in Instructions Menu
		else if (event.equals("Close Instruc")) {
			try {
				instrucFrame.dispose();
				instrucFrame.setVisible (false);	
				animation.stop();
				frameNo=0;
			}
			catch(NullPointerException e1) {

			}
		}
		
	
		//Displays the About menu
		else if (event.equals("About"))
			JOptionPane.showMessageDialog (this, "I'm Alex, I made Tetris", "About", JOptionPane.INFORMATION_MESSAGE);		
		
		//Animates the replay in the Instructions menu through many recorded screenshots
		else if (event.equals("Animate")) {
			loopLabel.setIcon(new ImageIcon(new ImageIcon(String.format("Images/ezgif-frame-%03d.png", (frameNo%150)+1)).getImage().getScaledInstance(250, 500, Image.SCALE_DEFAULT)));
			frameNo++;
		}
	}

	
	//Getter and Setter methods
	public static JFrame getTetrisFrame() {
		return tetrisFrame;
	}
	public static JFrame getVersusFrame() {
		return versusFrame;
	}

}