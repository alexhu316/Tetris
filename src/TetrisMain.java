//Alex Hu
//June 13 2021 
//The TetrisMain class allows the user to play a game of single-player Tetris,
//saving the user's game data after they clear 40 lines.
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.io.*;

public class TetrisMain extends JPanel implements ActionListener, KeyListener {

	private final int LINES=40;
	private final int hangTime=1000;

	//User-inputted controls and sensitivity taken from ControlCustomizer
	private int sensitivity=ControlCustomizer.getSens1();
	private int moveLeft=ControlCustomizer.getLeft1();
	private int moveRight=ControlCustomizer.getRight1();
	private int moveDown=ControlCustomizer.getDown1();
	private int hold=ControlCustomizer.getHold1();
	private int hardDrop=ControlCustomizer.getDrop1();
	private int rotateLeft=ControlCustomizer.getRotLeft1();
	private int rotateRight=ControlCustomizer.getRotRight1();
	private boolean ghostPieceOn=ControlCustomizer.getGhost1();

	//JComponents
	private static JPanel panel;
	private static JFrame frame;
	private JButton startButton;
	private JMenuItem sfxOption, backgroundOption;
	private JMenu audioMenu;
	private JMenuBar menu;

	//Stores the pieces played/being played
	private int[][] backMatrix;
	private int[][] curMatrix;
	private int[][] ghostMatrix;

	//Controls animation
	private Timer timer;

	//controls upcoming and current pieces
	private Queue<Block> queue = new LinkedList<Block>();
	private Block curPiece;

	//Controls automatic movement of the game
	private long lastDowned=System.currentTimeMillis();
	private long buffer=0; 

	//Allows colors to be accessed and changed easily
	private HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
	private HashMap<Color, Color> ghostColors = new HashMap<Color, Color>();

	private boolean drawGhost=true;

	//held piece of the player
	private boolean held = false;
	private int heldID = 0;

	//Information about the current game
	private boolean firstStart=true;
	private long startTime=0;
	private boolean started = false;
	private int lineCount=0;

	//Information about the current key(s) being pressed
	private long frameCount = 0;
	private long pressedLeft=0;
	private long pressedRight=0;
	private long pressedDown=0;

	//JComponents of the winner menu
	private JFrame scoreFrame;
	private JPanel scorePanel;
	private JLabel timeLabel;
	private JLabel linesLabel;
	private JTextField nameField;
	private JLabel scoreLabel;
	private JButton okButton;
	private int timeMillis=0;

	//Manages audio 
	private boolean sfxOn=true;
	private boolean musicOn=false;
	private AudioInputStream inputStream;
	private Clip clip;


	//Constructor
	public TetrisMain(){

		//Initializes information about JPanel
		setPreferredSize(new Dimension(800,750));
		setLocation(0,50);
		setLayout(null);

		//Initializes the game matrices
		backMatrix = new int[10][20];
		curMatrix = new int[10][24];
		ghostMatrix = new int[10][24];

		//Initializes JComponents
		startButton = new JButton("Start Game");
		startButton.addActionListener(this);
		startButton.setActionCommand("Start");
		startButton.setFocusable(false);
		startButton.setBounds(350, 35, 100, 30);

		timeLabel = new JLabel();
		timeLabel.setBounds(350,650,200,40);
		timeLabel.setFont(new Font("Comic Sans MS", 1, 20));
		timeLabel.setForeground(Color.white);

		linesLabel = new JLabel();
		linesLabel.setBounds(350,620,200,40);
		linesLabel.setFont(new Font("Comic Sans MS", 1, 20));
		linesLabel.setForeground(Color.white);

		backgroundOption  = new JCheckBoxMenuItem ("Background Music", false);
		backgroundOption.setActionCommand ("Background");
		backgroundOption.addActionListener (this);

		sfxOption  = new JCheckBoxMenuItem ("Sound Effects", true);
		sfxOption.setActionCommand ("SFX");
		sfxOption.addActionListener (this);

		audioMenu = new JMenu ("Audio");
		audioMenu.add(backgroundOption);
		audioMenu.addSeparator();
		audioMenu.add(sfxOption);

		menu = new JMenuBar ();
		menu.add (audioMenu);

		try {
			frame.setJMenuBar(menu);
		}
		catch (NullPointerException e) {
			MainMenu.getTetrisFrame().setJMenuBar(menu);
		}

		add(startButton);
		add(timeLabel);
		add(linesLabel);

		//Initializes timer
		timer = new Timer(20, this);
		timer.setActionCommand("Main");


		//Loads up the current queue
		addTenPieces();

		pieceColors(1);

		ghostColors.put(Color.yellow, new Color(135,140,69));
		ghostColors.put(Color.orange, new Color(155,124,61));
		ghostColors.put(Color.blue, new Color(89,91,136));
		ghostColors.put(Color.green, new Color(14,118, 29));
		ghostColors.put(Color.red, new Color(116,71,71));
		ghostColors.put(Color.magenta, new Color(108,1,146));
		ghostColors.put(Color.cyan, new Color(14,108,118));

		//Adds effect of background music
		try {
			inputStream = AudioSystem.getAudioInputStream(new File("Sounds/background track.wav"));
			clip = AudioSystem.getClip();
			clip.open(inputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	//Parameters: none
	//Return: void
	//Description adds ten random pieces to the queue, one of each of the seven and three random pieces
	public void addTenPieces() {
		ArrayList<Integer> starters = new ArrayList<Integer>();

		for (int i =1 ; i<8; i++) {
			starters.add(i);
		}

		for (int i =0 ; i<3; i++) {
			starters.add((int)(Math.random()*7+1));
		}

		for (int i =10 ; i>0; i--) {
			int rand = (int) (Math.random()*i);
			queue.add(makePiece(starters.get(rand)));
			starters.remove(rand);
		}
	}

	//Parameters: int direction
	//Return: void
	//Description checks for collisions, reshifting a piece to its original spot if the movement causes a collision.
	public void checkCollisions(int d) {

		int[] arr;

		for (int i=0; i<4; i++) {
			arr=curPiece.getOccupies()[i];
			if (arr[0]<0) {
				curPiece.move(2);
			}
			else if (arr[0]>9) {
				curPiece.move(1);
			}
			//Plants pieces that are being pushed down into a block if the buffer has passed
			else if (arr[1]>23) {
				curPiece.move(3);
				if (buffer==0) 
					buffer=System.currentTimeMillis();
				else if (System.currentTimeMillis()-buffer>1000)
					plant();
			}
			else if (backMatrix[arr[0]][Math.max(arr[1]-4,0)]!=0) {
				if (d==0) {
					curPiece.move(3);
					if (buffer==0) 
						buffer=System.currentTimeMillis();
					else if (System.currentTimeMillis()-buffer>1000)
						plant();
					return;
				}
				else if (d==1)
					curPiece.move(2);
				else if (d==2)
					curPiece.move(1);
			}
			else if (d==0) {
				lastDowned=System.currentTimeMillis();
			}
		}

		//Resets the buffer if the piece is now no longer directly above another
		if (buffer!=0) {
			boolean resetBuffer=true;

			for (int i=0; i<4; i++) {
				arr=curPiece.getOccupies()[i];
				if (arr[1]==23) {
					resetBuffer=false;
					break;
				}

				try {
					if (arr[1]>0&&backMatrix[arr[0]][arr[1]-3]!=0) {
						resetBuffer=false;
						break;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {

				}
			}
			if (resetBuffer)
				buffer=0;
		}
	}

	//Parameters: int choice
	//Return: void
	//Description: recolours all pieces
	public void pieceColors(int choice) {

		//normal colours
		if (choice==1) {
			colors.put(1, Color.yellow);
			colors.put(2, Color.orange);
			colors.put(3, Color.blue);
			colors.put(4, Color.green);
			colors.put(5, Color.red);
			colors.put(6, Color.magenta);
			colors.put(7, Color.cyan);
		}
		//game-over colors
		else {
			colors.put(1, Color.gray);
			colors.put(2, Color.gray);
			colors.put(3, Color.gray);
			colors.put(4, Color.gray);
			colors.put(5, Color.gray);
			colors.put(6, Color.gray);
			colors.put(7, Color.gray);
		}
	}

	//Parameters: none
	//Return: void
	//Description: ends the game on a loss
	public void gameOver() {
		pieceColors(2);
		repaint();
		started=false;

		timer.stop();

		JOptionPane.showMessageDialog (this,"You lost","Game Over",JOptionPane.WARNING_MESSAGE);

	}
	
	//Parameters: none
	//Return: void
	//Description: restarts the game
	public void restart(){

		if (!firstStart) {
			queue.clear();
			addTenPieces();
		}
		pieceColors(1);

		backMatrix = new int[10][20];
		curMatrix = new int[10][24];
		ghostMatrix=new int [10][24];
		
		pressedLeft=0;
		pressedRight=0;
		pressedDown=0;
		
		timer.start();
	}

	//Parameters: int ID
	//Return: Block
	//Description: creates a new Block
	public Block makePiece (int i) {

		if (i==1) 
			return new OBlock();
		if (i==2) 
			return new LBlock();
		if (i==3) 
			return new JBlock();
		if (i==4) 
			return new SBlock();
		if (i==5) 
			return new ZBlock();
		if (i==6) 
			return new TBlock();
		return new IBlock();
	}

	//Parameters: none
	//Return: void
	//Description: Takes in user information after a successfully completed game
	public void winner() {
		String timeString = timeLabel.getText()+" ";

		timeMillis = (Integer.parseInt(timeString.substring(0,2)) * 60000)
				+(Integer.parseInt(timeString.substring(3,5)) * 1000)
				+(Integer.parseInt(timeString.substring(6,9)));

		scoreFrame = new JFrame("Save This Game");
		scoreFrame.setPreferredSize(new Dimension(190,130));
		scoreFrame.setLocation(285,250);
		scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel,BoxLayout.PAGE_AXIS));
		scorePanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		scoreLabel = new JLabel("Your Time: "+timeString,SwingConstants.CENTER);
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameField = new JTextField("name");
		nameField.setSize(210, 30);
		nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		okButton = new JButton("OK");
		okButton.setActionCommand("Save Game");
		okButton.addActionListener(this);
		okButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		scorePanel.add(scoreLabel);
		scorePanel.add(nameField);
		scorePanel.add(okButton);

		scoreFrame.add (scorePanel);
		scoreFrame.pack ();
		scoreFrame.setVisible (true);
	}

	//Parameters: none
	//Return: void
	//Description: Adds the information from a game onto the leaderboards
	public void makeGame() throws IOException {
		PrintWriter outFile = new PrintWriter (new FileWriter ("leaderboards.txt", true));
		outFile.println(timeMillis+" "+nameField.getText());
		outFile.close();
	}

	//Parameters: none
	//Return: void
	//Description: Draws the ghost piece of a current piece onto the ghostMatrix
	public void drawGhostPiece() {

		int[][] ghostSquares = new int [4][2];
		for (int i=0; i<4; i++) {
			ghostSquares [i][0]=curPiece.getOccupies()[i][0];
			ghostSquares [i][1]=curPiece.getOccupies()[i][1];
		}

		try {
			if (curPiece.getID()==7&&curPiece.getRot()%2==1) {
				int highestY = -1;
				for (int i=0; i<4; i++) {
					highestY = Math.max(curPiece.getOccupies()[i][1], highestY);
				}
				while (highestY<23&&backMatrix[curPiece.getOccupies()[0][0]][highestY-3]==0) {			
					highestY++;
				}
				ghostMatrix=new int [10][24];
				for (int i=0; i<4; i++) {
					ghostMatrix[curPiece.getOccupies()[0][0]][highestY-i]=3;
				}
				return;
			}
			else {

				while (true) {

					for (int i=0; i<4; i++) {
						if (backMatrix[ghostSquares[i][0]][Math.max(0, ghostSquares[i][1]-4)]!=0) {
							return;
						}
					}
					ghostMatrix=new int [10][24];
					for (int i=0; i<4; i++) {
						ghostMatrix[ghostSquares[i][0]][ghostSquares[i][1]]=3;
					}
					for (int i=0; i<4; i++) {
						ghostSquares [i][1]++;
					}
				}
			}

		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	//Parameters: none
	//Return: void
	//Description: Draws the current piece onto the curMatrix
	public void drawCur () {	

		int[] arr;
		try {
			for (int i=0; i<4; i++) {
				arr=curPiece.getOccupies()[i];
				curMatrix[arr[0]][arr[1]]=curPiece.getID();
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return;
		}

		drawGhostPiece();
	}

	//Parameters: none
	//Return: void
	//Description: Clears out the current matrix
	public void clearCur () {	

		curMatrix = new int[10][24];
		ghostMatrix = new int[10][24];
	}

	//Parameters: int row
	//Return: void
	//Description: removes a line
	public void clearLine(int row) {
		lineCount++;
		for (int j = 0; j<10; j++) {
			backMatrix[j][row]=0;
		}
		for (int i=row; i>0; i--) {
			for (int j=0; j<10; j++) {
				backMatrix[j][i]=backMatrix[j][i-1];
			}
		}
		for (int i = 0; i<10; i++) {
			backMatrix[i][0]=0;
		}
	}

	//Parameters: none
	//Return: void
	//Description: holds the current piece if allowed
	public void hold() {
		held=true;
		curMatrix = new int[10][24];

		if (heldID==0) {
			heldID=curPiece.getID();
			curPiece=queue.remove();
			curPiece.move(0);
			checkCollisions(0);
			queue.add(makePiece((int)(Math.random()*7)+1));
		}
		else {
			int temp = curPiece.getID();
			curPiece=makePiece(heldID);
			heldID=temp;
		}		
	}

	//Parameters: none
	//Return: void
	//Description: plants the current piece onto the backMatrix, managing game-overs and line-clears
	public void plant () {

		lastDowned=System.currentTimeMillis();
		Set<Integer> rowsToClear = new TreeSet<Integer>();
		int[] arr;
		for (int i=0; i<4; i++) {
			boolean clear = true;
			arr=curPiece.getOccupies()[i];
			try {
				backMatrix[arr[0]][arr[1]-4]=curPiece.getID();

				curMatrix[arr[0]][arr[1]]=0;	

				for (int j = 0; j<10; j++) {
					if (backMatrix[j][arr[1]-4]==0) {
						clear = false;
						break;
					}
				}
				if (clear) {
					rowsToClear.add(arr[1]-4);
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				if (arr[1]-4<19) {

					for (int j = 0 ; j<4; j++) {
						arr=curPiece.getOccupies()[i];
						if (arr[1]-4>=0) {
							backMatrix[arr[0]][arr[1]-4]=curPiece.getID();
						}
					}
					gameOver();
					return;
				}
			}
		}

		boolean playSound=false;
		if (!rowsToClear.isEmpty()) {
			playSound=true;
			Iterator iter = rowsToClear.iterator();
			while (iter.hasNext()) {
				clearLine((int) iter.next());
				for (Integer i : rowsToClear) {
					i++;
				}
			}
		}
		if (playSound) {
			playSound("Sounds/clear.wav");
		}

		curPiece=queue.remove();
		curPiece.move(0);
		checkCollisions(0);
		if (queue.size()<6) {
			addTenPieces();
		}		

		held=false;

		playSound("Sounds/drop.wav");
	}

	//Parameters: none
	//Return: void
	//Description: forcefully plants a piece from any position onto the backMatrix
	public void hardDrop() {

		try {
			clearCur();
		}
		catch (ArrayIndexOutOfBoundsException e) {
			gameOver();
			return;
		}
		while(true) {
			curPiece.move(0);
			int[] arr;

			for (int i=0; i<4; i++) {
				arr=curPiece.getOccupies()[i];
				if (arr[1]>23) {
					curPiece.move(3);
					plant();
					return;
				}
				else if (backMatrix[arr[0]][Math.max(arr[1]-4,0)]!=0) {
					curPiece.move(3);
					plant();
					return;
				}
			}
		}
	}


	//Main Method
	public static void main (String[] args) {

		frame = new JFrame ("Tetris");
		panel = new TetrisMain();
		frame.add (panel);
		frame.addKeyListener((KeyListener) panel);
		frame.pack ();
		frame.setVisible (true);

	}	

	//Parameters: Graphics g
	//Return: void
	//Description: Draws out the matrix, queue, held piece, and game info
	public void paintComponent(Graphics g) {

		//Fills the background
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 1000);

		//Draws the grid lines
		g.setColor(Color.white);
		for (int r = 0; r<20; r++) {
			for (int c = 0; c<10; c++) {		
				g.drawRect(275+c*25, 120+r*25, 25, 25);			
			}		
		}

		//Draws the pieces on the backMatrix
		g.setColor(Color.gray);
		for (int r = 0; r<20; r++) {
			for (int c = 0; c<10; c++) {		
				if (backMatrix[c][r]!=0) {
					g.setColor(colors.get(backMatrix[c][r]));
					g.fillRect(275+c*25, 120+r*25, 25, 25);
				}			
			}	
		}


		if (started) {
			//Draws the ghost piece

			if (drawGhost&&ghostPieceOn) {
				Color starter = colors.get(curPiece.getID());

				g.setColor(ghostColors.get(starter));
				for (int r = 4; r<24; r++) {
					for (int c = 0; c<10; c++) {		
						if (ghostMatrix[c][r]!=0) {
							g.fillRect(275+c*25, 20+r*25, 25, 25);			
						}
					}	
				}
			}
		}

		//Draws the pieces on the curMatrix
		for (int r = 4; r<24; r++) {
			for (int c = 0; c<10; c++) {		
				if (curMatrix[c][r]!=0) {
					g.setColor(colors.get(curMatrix[c][r]));
					g.fillRect(275+c*25, 20+r*25, 25, 25);			
				}
			}	
		}


		//Draws the queue
		g.setColor(Color.white);		
		Iterator iter = queue.iterator();

		double counter=0;
		for (int i = 0; i<5; i++) {

			Block b = (Block) iter.next();
			g.setColor(colors.get(b.getID()));
			int x = 625;
			int y = (int)(170+75*counter);

			if (b.getID()==1) {
				g.fillRect(x, y-25, 50, 50);
			}
			else if (b.getID()==2) {
				g.fillRect(x-25, y, 75, 25);
				g.fillRect(x+25, y-25, 25, 25);
			}
			else if (b.getID()==3) {
				g.fillRect(x-25, y, 75, 25);
				g.fillRect(x-25, y-25, 25, 25);
			}
			else if (b.getID()==4) {
				g.fillRect(x-25, y, 50, 25);
				g.fillRect(x, y-25, 50, 25);
			}
			else if (b.getID()==5) {
				g.fillRect(x, y, 50, 25);
				g.fillRect(x-25, y-25, 50, 25);
			}
			else if (b.getID()==6) {
				g.fillRect(x-25, y, 75, 25);
				g.fillRect(x, y-25, 25, 25);
			}
			else if (b.getID()==7) {
				g.fillRect(x-25, y, 100, 25);
			}
			counter++;

			if (counter==1) {
				counter+=0.3;
			}
		}


		//Draws the held piece
		g.setColor(colors.get(heldID));
		int x = 125;
		int y = (int)(170);

		if (heldID==1) {
			g.fillRect(x, y-25, 50, 50);
		}
		else if (heldID==2) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x+25, y-25, 25, 25);
		}
		else if (heldID==3) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x-25, y-25, 25, 25);
		}
		else if (heldID==4) {
			g.fillRect(x-25, y, 50, 25);
			g.fillRect(x, y-25, 50, 25);
		}
		else if (heldID==5) {
			g.fillRect(x, y, 50, 25);
			g.fillRect(x-25, y-25, 50, 25);
		}
		else if (heldID==6) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x, y-25, 25, 25);
		}
		else if (heldID==7) {
			g.fillRect(x-25, y, 100, 25);
		}

		//Displays the game info
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", 1, 20));

		if (started) {
			timeLabel.setText(String.format("%02d:%06.3fs", (int)((System.currentTimeMillis()-startTime)/1000.0)/60, (System.currentTimeMillis()-startTime)/1000.0%60));
			linesLabel.setText((LINES-lineCount)+" Lines Remaining");
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		String event = e.getActionCommand();

		//Starts a new game
		if (event.equals("Start")){

			try {
				scoreFrame.dispose();
				scoreFrame.setVisible(false);
			}
			catch (NullPointerException e1) {

			}
			drawGhost=true;
			held = false;
			heldID = 0;
			startTime=System.currentTimeMillis();
			started=true;
			lineCount=0;

			restart();
			if (firstStart) {
				firstStart=false;
			}
			curPiece = queue.remove();
			curPiece.move(0);
			checkCollisions(0);
		}

		//Saves the information of a game
		else if (event.equals("Save Game")){

			if (nameField.getText().trim().equals("")){
				JOptionPane.showMessageDialog (scorePanel,"Name cannot be blank; please re-enter","Re-Enter Name!",JOptionPane.WARNING_MESSAGE);
			}
			else if (nameField.getText().trim().length()>15) {
				JOptionPane.showMessageDialog (scorePanel,"Name must be under 15 characters","Re-Enter Name!",JOptionPane.WARNING_MESSAGE);
			}
			else {
				try {
					makeGame();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				scoreFrame.dispose();
				scoreFrame.setVisible(false);
			}
		}

		//Calculates and animates what is supposed to happen in the next frame
		else if (event.equals("Main")){
			try {
				//Moves piece
				clearCur();
				if (pressedLeft!=0&&((sensitivity*(frameCount-pressedLeft))/20.0)%1<sensitivity/20.0) {
					if (frameCount-pressedLeft>40/sensitivity||frameCount-pressedLeft<20/sensitivity) {
						curPiece.move(1);
						checkCollisions(1);
					}
				}
				if (pressedRight!=0&&((sensitivity*(frameCount-pressedRight))/20.0)%1<sensitivity/20.0) {
					if (frameCount-pressedRight>40/sensitivity||frameCount-pressedRight<20/sensitivity) {
						curPiece.move(2);
						checkCollisions(2);
					}
				}
				if (pressedDown!=0&&((sensitivity*(frameCount-pressedDown))/20.0)%1<sensitivity/20.0) {
					curPiece.move(0);
					checkCollisions(0);
				}

			}
			catch (ArrayIndexOutOfBoundsException e1) {
				gameOver();
				return;
			}

			if (System.currentTimeMillis()-lastDowned>hangTime) {
				lastDowned=System.currentTimeMillis();
				curPiece.move(0);
				checkCollisions(0);
			}

			drawCur();

			//Repaints with new positions
			repaint(275, 150, 250, 500);
			repaint(625, 120, 170, 500);
			repaint(100, 100, 100, 500);
			repaint (300,700,100,100);

			frameCount++;

			//Ends game if 40 lines have been cleared
			if (lineCount>=LINES) {
				lineCount=LINES;
				linesLabel.setText((LINES-lineCount)+" Lines Remaining");
				pieceColors(2);
				timer.stop();
				started=false;
				drawGhost = false;
				repaint();
				winner();
			}
		}

		//Toggles SFX
		else if (event.equals("SFX")) {
			if (sfxOn) 
				sfxOn=false;
			else
				sfxOn=true;
		}

		//Toggles background music
		else if (event.equals("Background")){
			if (musicOn) {
				clip.stop();
				musicOn=false;
			}
			else {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				musicOn=true;
			}
		}


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
	//Description: Overridden method for moving pieces, other actions
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (started) {
			if (key==moveLeft) {
				if (pressedLeft==0) {
					pressedRight=0;
					pressedLeft=frameCount;
				}
			}
			else if (key==moveRight) {
				if (pressedRight==0) {
					pressedLeft=0;
					pressedRight=frameCount;
				}
			}
			else if (key==moveDown) {
				if (pressedDown==0) {
					pressedDown=frameCount;
				}
			}
			else if (key==hardDrop) {
				hardDrop();
			}
			else if (key==rotateRight) {
				curPiece.rotateRight(backMatrix);
				drawCur();
			}
			else if (key==hold) {
				if (!held) 
					hold();
			}
			else if (key==rotateLeft) {
				curPiece.rotateLeft(backMatrix);
				drawCur();

			}
			else if (key==127||key==8) { 
				curPiece=makePiece((int)(Math.random()*7+1));
			}
		}
	}

	//Parameters: KeyEvent e
	//Return: void
	//Description: Overridden method for moving pieces
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key==moveLeft) {
			if (frameCount==pressedLeft) {
				if (moveRight==0) {
					curPiece.move(1);
					checkCollisions(1);
				}
			}
			pressedLeft=0;
		}
		else if (key==moveRight) {
			if (moveLeft==0) {
				if (frameCount==pressedRight) {
					curPiece.move(2);
					checkCollisions(2);

				}
			}
			pressedRight=0;
		}
		else if (key==moveDown) {
			pressedDown=0;
		}
	}

	//Parameters: String fileName
	//Return: void
	//Description: Plays the sound of that file name
	public void playSound(String fileName)  {
		if (sfxOn) {
			try {
				File f = new File(fileName);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}

		}
	}
}
