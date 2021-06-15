//Alex Hu
//June 13 2021 
//The ControlCustomizer class allows the user to customize the keybinds, sensitivity, and the 
//visual ghost piece for both players 
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlCustomizer extends JPanel implements ActionListener, KeyListener, ChangeListener{

	//JComponents
	private static JFrame frame;
	private static ControlCustomizer panel;
	private JLabel leftLabel, rightLabel, downLabel, holdLabel, dropLabel, rotLeftLabel, rotRightLabel, sliderLabel;
	private JButton leftButton, rightButton, downButton, holdButton, dropButton, rotLeftButton, rotRightButton;
	private JButton[] buttons = new JButton[7];	
	private JButton ghostButton;
	private JSlider senSlider;
	private JLabel currentLabel;
	private JButton switchButton;
	
	//Initial controls, settings, sensitivities for both players
	private static int sensitivity1 = 15;
	private static int sensitivity2 = 10;		
	
	private static int moveLeft1 = KeyEvent.VK_LEFT;
	private static int moveRight1 = KeyEvent.VK_RIGHT;
	private static int moveDown1 = KeyEvent.VK_DOWN;
	private static int hardDrop1 = KeyEvent.VK_SPACE;
	private static int rotateLeft1 = KeyEvent.VK_F;
	private static int rotateRight1 = KeyEvent.VK_UP;
	private static int hold1 = KeyEvent.VK_SHIFT;
	private static boolean ghostPiece1 = true;
	
	private static int moveLeft2 = KeyEvent.VK_A;
	private static int moveRight2 = KeyEvent.VK_D;
	private static int moveDown2 = KeyEvent.VK_S;
	private static int hardDrop2 = KeyEvent.VK_C;
	private static int rotateLeft2 = KeyEvent.VK_Z;
	private static int rotateRight2 = KeyEvent.VK_W;
	private static int hold2 = KeyEvent.VK_2;
	private static boolean ghostPiece2 = true;

	//boolean[] choice is the selected control to be changed
	private boolean[] choice = new boolean[14];
	
	//List of all the key codes currently assigned as keybinds
	private ArrayList<Integer> keyCodes = new ArrayList<Integer>();
	
	//contains the actionCommands of the JButtons
	private String [] actionCommands = {"Left1", "Right1", "Down1", "Hold1", "Drop1", "RLeft1", "RRight1",
			"Left2", "Right2", "Down2", "Hold2", "Drop2", "RLeft2", "RRight2"};

	//Maps an integer keycode to its actual key name
	private HashMap<Integer, String> keyCodeMap = new HashMap<Integer, String>();

	
	//Constructor
	public ControlCustomizer() {
	
		
		//Manually adds all the keycodes in use
		keyCodes.add(moveLeft1+0);
		keyCodes.add(moveRight1+0);
		keyCodes.add(moveDown1+0);
		keyCodes.add(hardDrop1+0);
		keyCodes.add(rotateLeft1+0);
		keyCodes.add(rotateRight1+0);
		keyCodes.add(hold1+0);
		keyCodes.add(moveLeft2+0);
		keyCodes.add(moveRight2+0);
		keyCodes.add(moveDown2+0);
		keyCodes.add(hardDrop2+0);
		keyCodes.add(rotateLeft2+0);
		keyCodes.add(rotateRight2+0);
		keyCodes.add(hold2+0);

		//Manually adds maps the keys to their names
		for (int i =48; i<58; i++) {
			keyCodeMap.put(i,(i-48)+" ");
		}
		for (int i =65; i<91; i++) {
			keyCodeMap.put(i,((char)(i+32)+" "));
		}
		keyCodeMap.put(30, "accept");
		keyCodeMap.put(107, "+");
		keyCodeMap.put(65481, "again");
		keyCodeMap.put(240, "alphanumeric");
		keyCodeMap.put(18, "alt");
		keyCodeMap.put(150, "&");
		keyCodeMap.put(151, "*");
		keyCodeMap.put(192, "`");
		keyCodeMap.put(92, "\\");
		keyCodeMap.put(161, "{");
		keyCodeMap.put(162, "}");
		keyCodeMap.put(3, "cancel");
		keyCodeMap.put(20, "caps lock");
		keyCodeMap.put(514, "^");
		keyCodeMap.put(12, "clear");
		keyCodeMap.put(93, "]");
		keyCodeMap.put(258, "input");
		keyCodeMap.put(513, ":");
		keyCodeMap.put(44, ",");
		keyCodeMap.put(17, "control");
		keyCodeMap.put(110, ".");
		keyCodeMap.put(111, "divide");
		keyCodeMap.put(515, "$");
		keyCodeMap.put(40, "down");
		keyCodeMap.put(35, "end");
		keyCodeMap.put(10, "enter");
		keyCodeMap.put(61, "=");
		keyCodeMap.put(27, "escape");
		keyCodeMap.put(517, "!");
		for (int i =112; i<121; i++) {
			keyCodeMap.put(i, "f"+(i-111));
		}
		keyCodeMap.put(160, ">");
		keyCodeMap.put(36, "home");
		keyCodeMap.put(155, "insert");
		keyCodeMap.put(225, "down");
		keyCodeMap.put(226, "left");
		keyCodeMap.put(227, "right");
		keyCodeMap.put(224, "up");
		keyCodeMap.put(37, "left");
		keyCodeMap.put(519, "(");
		keyCodeMap.put(153, "<");
		keyCodeMap.put(54, "-");
		keyCodeMap.put(144, "num lock");
		keyCodeMap.put(520, "#");
		for (int i = 96; i<106; i++) {
			keyCodeMap.put(i, "num"+(i-96));
		}
		keyCodeMap.put(91, "[");
		keyCodeMap.put(34, "page up");
		keyCodeMap.put(33, "page down");
		keyCodeMap.put(19, "pause");
		keyCodeMap.put(46, ".");
		keyCodeMap.put(521, "+");
		keyCodeMap.put(222, "\'");
		keyCodeMap.put(152, "\"");
		keyCodeMap.put(39, "right");
		keyCodeMap.put(522, ")");
		keyCodeMap.put(145, "scroll lock");
		keyCodeMap.put(59, ";");
		keyCodeMap.put(16, "shift");
		keyCodeMap.put(47, "/");
		keyCodeMap.put(32, "space");
		keyCodeMap.put(109, "-");
		keyCodeMap.put(9, "tab");
		keyCodeMap.put(523, "_");
		keyCodeMap.put(38, "up");
		keyCodeMap.put(524, "windows");

		//sets up the JPanel
		setPreferredSize(new Dimension(450,450));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//Creates all the labels
		leftLabel = new JLabel ("Move left:",SwingConstants.CENTER); 
		rightLabel = new JLabel ("Move right:",SwingConstants.CENTER); 
		downLabel = new JLabel ("Soft drop:",SwingConstants.CENTER); 
		holdLabel = new JLabel ("Hold piece:",SwingConstants.CENTER); 
		dropLabel = new JLabel ("Hard drop:",SwingConstants.CENTER); 
		rotLeftLabel = new JLabel ("Rotate left:",SwingConstants.CENTER); 
		rotRightLabel = new JLabel ("Rotate right:",SwingConstants.CENTER);
		sliderLabel = new JLabel ("Sensitivity",SwingConstants.CENTER);
		currentLabel = new JLabel ("Edit Controls for Player 1",SwingConstants.CENTER);

		
		//Initializes all JButtons and JSlider
		leftButton = new JButton(keyCodeMap.get(moveLeft1));
		leftButton.setActionCommand("Left1");
		leftButton.addActionListener(this);
		leftButton.setFocusable(false);
		rightButton = new JButton(keyCodeMap.get(moveRight1));
		rightButton.setActionCommand("Right1");
		rightButton.addActionListener(this);
		rightButton.setFocusable(false);
		downButton = new JButton(keyCodeMap.get(moveDown1));
		downButton.setActionCommand("Down1");
		downButton.addActionListener(this);
		downButton.setFocusable(false);
		holdButton = new JButton(keyCodeMap.get(hold1));
		holdButton.setActionCommand("Hold1");
		holdButton.addActionListener(this);
		holdButton.setFocusable(false);
		dropButton = new JButton(keyCodeMap.get(hardDrop1));
		dropButton.setActionCommand("Drop1");
		dropButton.addActionListener(this);
		dropButton.setFocusable(false);
		rotLeftButton = new JButton(keyCodeMap.get(rotateLeft1));
		rotLeftButton.setActionCommand("RLeft1");
		rotLeftButton.addActionListener(this);
		rotLeftButton.setFocusable(false);
		rotRightButton = new JButton(keyCodeMap.get(rotateRight1));	
		rotRightButton.setActionCommand("RRight1");
		rotRightButton.addActionListener(this);
		rotRightButton.setFocusable(false);

		ghostButton = new JButton("Disable Ghost Piece");
		ghostButton.setActionCommand("Ghost1");
		ghostButton.addActionListener(this);
		ghostButton.setFocusable(false);
		
		senSlider = new JSlider(JSlider.HORIZONTAL, 1,20,sensitivity1);
		senSlider.addChangeListener((ChangeListener) this);
		senSlider.setMajorTickSpacing(1);
		senSlider.setPaintTicks(true);
		senSlider.setPaintLabels(true);
		senSlider.setSnapToTicks(true);
		senSlider.setFocusable(false);
		
		switchButton = new JButton("Change Player 2 Controls");
		switchButton.setActionCommand("Switch2");
		switchButton.addActionListener(this);
		switchButton.setFocusable(false);

		buttons[0]=leftButton;
		buttons[1]=rightButton;
		buttons[2]=downButton;
		buttons[3]=holdButton;
		buttons[4]=dropButton;
		buttons[5]=rotLeftButton;
		buttons[6]=rotRightButton;


		//Adds all JComponents with GridBagLayout (I probably should have used a helper method here)
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=0;
		gbc.gridwidth=2;
		gbc.anchor=GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(currentLabel,gbc);
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.gridwidth=1;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(leftLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(leftButton,gbc);
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(rightLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=2;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(rightButton,gbc);
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(downLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=3;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(downButton,gbc);
		gbc.gridx=0;
		gbc.gridy=4;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(holdLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=4;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(holdButton,gbc);
		gbc.gridx=0;
		gbc.gridy=5;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(dropLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=5;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(dropButton,gbc);
		gbc.gridx=0;
		gbc.gridy=6;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(rotLeftLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=6;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(rotLeftButton,gbc);
		gbc.gridx=0;
		gbc.gridy=7;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(rotRightLabel,gbc);
		gbc.gridx=1;
		gbc.gridy=7;
		gbc.weightx=0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(rotRightButton,gbc);
		gbc.gridx=0;
		gbc.gridy=8;
		gbc.weightx=0;
		gbc.gridwidth=2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add (ghostButton,gbc);
		gbc.gridx=0;
		gbc.gridy=9;
		gbc.weightx=0;
		gbc.gridwidth=2;
		gbc.insets = new Insets(20,0,0,0); 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add (sliderLabel,gbc);
		gbc.gridx=0;
		gbc.gridy=10;
		gbc.weightx=0;
		gbc.gridwidth=2;
		gbc.insets = new Insets(0,0,0,0); 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add (senSlider,gbc);
		gbc.gridx=0;
		gbc.gridy=11;
		gbc.weightx=0;
		gbc.gridwidth=2;
		gbc.ipadx=60;
		gbc.insets = new Insets(50,0,0,0); 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add (switchButton,gbc);
	}


	//Main Method
	public static void main (String[] args) {

		frame = new JFrame("Controls");
		panel = new ControlCustomizer();

		frame.add(panel);
		frame.addKeyListener(panel);
		frame.pack();
		frame.setVisible(true);

	}


	//Parameters: KeyEvent e
	//Return: void
	//Description: Overridden method 
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	//Parameters: KeyEvent e
	//Return: void
	//Description: Overridden method to assign the key pressed to the selected control
	@Override
	public void keyPressed(KeyEvent e) {
		

		if (!keyCodes.contains(e.getKeyCode())) {

			if (keyCodeMap.get(e.getKeyCode())==(null)) {
				for (int i =0; i<7; i++) {
						choice[i]=false;
						choice[i+7]=false;
				}
				return;	

			}
			for (int i =0; i<7; i++) {
				if (choice[i]||choice[i+7]) {
					choice[i]=false;
					choice[i+7]=false;
					String text = buttons[i].getText();
					for (Integer n :keyCodeMap.keySet()) {
						try {
							if (text.equals(keyCodeMap.get(n))) {
								keyCodes.remove(n);
							}
						}
						catch (NullPointerException e1) {
							return;	
						}
					} 
					keyCodes.add(e.getKeyCode());
					buttons[i].setText(keyCodeMap.get(e.getKeyCode()));
					if (buttons[i].getActionCommand().equals("Left1")) 
						moveLeft1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Right1")) 
						moveRight1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Down1"))
						moveDown1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Drop1")) 
						hardDrop1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("RLeft1")) 
						rotateLeft1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("RRight1"))
						rotateRight1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Hold1"))
						hold1=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Left2")) 
						moveLeft2=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Right2")) 
						moveRight2=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Down2"))
						moveDown2=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Drop2")) 
						hardDrop2=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("RLeft2")) 
						rotateLeft2=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("RRight2"))
						rotateRight2=e.getKeyCode();
					else if (buttons[i].getActionCommand().equals("Hold2/"))
						hold2=e.getKeyCode();
				}
			}
		}
	}


	//Parameters: KeyEvent e
	//Return: void
	//Description: Overridden method 
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();

		//Switches the current player to player 1
		if (event.equals("Switch1")) {

			leftButton.setText(keyCodeMap.get(moveLeft1));
			leftButton.setActionCommand("Left1");
			rightButton.setText(keyCodeMap.get(moveRight1));
			rightButton.setActionCommand("Right1");
			downButton.setText(keyCodeMap.get(moveDown1));
			downButton.setActionCommand("Down1");
			holdButton.setText(keyCodeMap.get(hold1));
			holdButton.setActionCommand("Hold1");
			dropButton.setText(keyCodeMap.get(hardDrop1));
			dropButton.setActionCommand("Drop1");
			rotLeftButton.setText(keyCodeMap.get(rotateLeft1));
			rotLeftButton.setActionCommand("RLeft1");
			rotRightButton.setText(keyCodeMap.get(rotateRight1));	
			rotRightButton.setActionCommand("RRight1");
			ghostButton.setActionCommand("Ghost1");
			
			if (ghostPiece1)
				ghostButton.setText("Disable Ghost Piece");
			else
				ghostButton.setText("Enable Ghost Piece");
			
			currentLabel.setText("Edit Controls for Player 1");
			switchButton.setText("Change Player 2 Controls");
			switchButton.setActionCommand("Switch2");	
			
			senSlider.setValue(sensitivity1);
		}
		
		//Switches the current player to player 2
		else if (event.equals("Switch2")){
			leftButton.setText(keyCodeMap.get(moveLeft2));
			leftButton.setActionCommand("Left2");
			rightButton.setText(keyCodeMap.get(moveRight2));
			rightButton.setActionCommand("Right2");
			downButton.setText(keyCodeMap.get(moveDown2));
			downButton.setActionCommand("Down2");
			holdButton.setText(keyCodeMap.get(hold2));
			holdButton.setActionCommand("Hold2");
			dropButton.setText(keyCodeMap.get(hardDrop2));
			dropButton.setActionCommand("Drop2");
			rotLeftButton.setText(keyCodeMap.get(rotateLeft2));
			rotLeftButton.setActionCommand("RLeft2");
			rotRightButton.setText(keyCodeMap.get(rotateRight2));	
			rotRightButton.setActionCommand("RRight2");
			ghostButton.setActionCommand("Ghost2");
			
			if (ghostPiece2)
				ghostButton.setText("Disable Ghost Piece");
			else
				ghostButton.setText("Enable Ghost Piece");
			
			currentLabel.setText("Edit Controls for Player 2");
			switchButton.setText("Change Player 1 Controls");
			switchButton.setActionCommand("Switch1");
			
			senSlider.setValue(sensitivity2);
		}
		
		//Manages the Ghost Piece button for player 1
		else if (event.equals("Ghost1")) {
			ghostPiece1=!ghostPiece1;
			if (ghostPiece1)
				ghostButton.setText("Disable Ghost Piece");
			else
				ghostButton.setText("Enable Ghost Piece");	
		}
		
		//Manages the Ghost Piece button for player 2
		else if (event.equals("Ghost2")) {
			ghostPiece2=!ghostPiece2;
			if (ghostPiece2)
				ghostButton.setText("Ghost Piece: Enabled");
			else
				ghostButton.setText("Ghost Piece: Disabled");	
		}
		
		//Manages the assignment of controls based on the JButton selected
		else {
			for (int j= 0; j< 14; j++) {
				if (event.equals(actionCommands[j])){
					for (int i =0; i<14; i++){
						choice[i]=false;
					}
					choice[j]=true;
					break;
				}
			}
		}
	}

	//Parameters: ChangeEvent e
	//Return: void
	//Description: Overridden method for getting the value from the slider 
	@Override
	public void stateChanged(ChangeEvent e) {
		if (switchButton.getActionCommand().equals("Switch1")) {
			sensitivity2=senSlider.getValue();
		}
		if (switchButton.getActionCommand().equals("Switch2")) {
			sensitivity1=senSlider.getValue();
		}	
	}	
	
	
	//Getter methods
	public static int getSens1() {
		return sensitivity1;
	}
	public static int getLeft1() {
		return moveLeft1;
	}
	public static int getRight1() {
		return moveRight1;
	}
	public static int getDown1() {
		return moveDown1;
	}
	public static int getDrop1() {
		return hardDrop1;
	}
	public static int getHold1() {
		return hold1;
	}
	public static int getRotLeft1() {
		return rotateLeft1;
	}
	public static int getRotRight1() {
		return rotateRight1;
	}
	public static boolean getGhost1() {
		return ghostPiece1;
	}
	public static int getSens2() {
		return sensitivity2;
	}
	public static int getLeft2() {
		return moveLeft2;
	}
	public static int getRight2() {
		return moveRight2;
	}
	public static int getDown2() {
		return moveDown2;
	}
	public static int getDrop2() {
		return hardDrop2;
	}
	public static int getHold2() {
		return hold2;
	}
	public static int getRotLeft2() {
		return rotateLeft2;
	}
	public static int getRotRight2() {
		return rotateRight2;
	}
	public static boolean getGhost2() {
		return ghostPiece2;
	}
	
}
