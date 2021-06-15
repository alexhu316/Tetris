//Alex Hu
//June 13 2021 
//The TetrisVersus class allows the user to play a game of two-player Tetris,
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisVersus extends JPanel implements ActionListener, KeyListener{

	//User-inputted controls and sensitivities taken from ControlCustomizer
	private int sensitivity1=ControlCustomizer.getSens1();
	private int moveLeft1=ControlCustomizer.getLeft1();
	private int moveRight1=ControlCustomizer.getRight1();
	private int moveDown1=ControlCustomizer.getDown1();
	private int hold1=ControlCustomizer.getHold1();
	private int hardDrop1=ControlCustomizer.getDrop1();
	private int rotateLeft1=ControlCustomizer.getRotLeft1();
	private int rotateRight1=ControlCustomizer.getRotRight1();
	private boolean ghostPieceOn1=ControlCustomizer.getGhost1();

	private int sensitivity2 = ControlCustomizer.getSens2();
	private int moveLeft2=ControlCustomizer.getLeft2();
	private int moveRight2=ControlCustomizer.getRight2();
	private int moveDown2=ControlCustomizer.getDown2();
	private int hold2=ControlCustomizer.getHold2();
	private int hardDrop2=ControlCustomizer.getDrop2();
	private int rotateLeft2=ControlCustomizer.getRotLeft2();
	private int rotateRight2=ControlCustomizer.getRotRight2();
	private boolean ghostPieceOn2=ControlCustomizer.getGhost2();

	//JComponents
	private static JPanel panel;
	private static JFrame frame;
	private JButton startButton;
	private JMenuItem sfxOption, backgroundOption;
	private JMenu audioMenu;
	private JMenuBar menu;

	//Stores the pieces played/being played for each player
	private int[][] backMatrix1;
	private int[][] curMatrix1;
	private int[][] ghostMatrix1;

	private int[][] backMatrix2;
	private int[][] curMatrix2;
	private int[][] ghostMatrix2;

	//Controls animation
	private Timer timer;

	//controls upcoming and current pieces for each player
	private Block curPiece1;
	private Queue<Block> queue1 = new LinkedList<Block>();
	private Block curPiece2;
	private Queue<Block> queue2 = new LinkedList<Block>();

	//Controls automatic movement of the game
	private long lastDowned1=System.currentTimeMillis();
	private long lastDowned2=System.currentTimeMillis();
	private long buffer1=0;
	private long buffer2=0;

	//Allows colors to be accessed easily
	private HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
	private HashMap<Color, Color> ghostColors = new HashMap<Color, Color>();

	private boolean drawGhost=true;

	//held pieces of each player
	private boolean held1 = false;
	private boolean held2 = false;
	private int heldID1 = 0;
	private int heldID2 = 0;

	//Information about the current game
	private boolean firstStart=true;
	private boolean started = false;

	//Information about the current key(s) being pressed
	private long frameCount=0;
	private long pressedLeft1=0;
	private long pressedRight1=0;
	private long pressedDown1=0;
	private long pressedLeft2=0;
	private long pressedRight2=0;
	private long pressedDown2=0;

	//Which player won
	private int winner = 0;

	//Manages audio 
	private boolean sfxOn=true;
	private boolean musicOn=false;
	private AudioInputStream inputStream;
	private Clip clip;	


	//Constructor
	public TetrisVersus(){

		//Initializes information about JPanel
		setPreferredSize(new Dimension(1300,750));
		setLocation(0,50);
		setLayout(null);


		//Initializes the game matrices
		backMatrix1 = new int[10][20];
		curMatrix1 = new int[10][24];
		ghostMatrix1 = new int[10][24];

		backMatrix2 = new int[10][20];
		curMatrix2 = new int[10][24];
		ghostMatrix2 = new int[10][24];

		//Initializes JComponents
		startButton = new JButton("Start Game");
		startButton.addActionListener(this);
		startButton.setActionCommand("Start");
		startButton.setFocusable(false);
		startButton.setBounds(600, 35, 100, 30);

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
			MainMenu.getVersusFrame().setJMenuBar(menu);
		}

		add(startButton);

		//Initializes timer
		timer = new Timer(20, this);
		timer.setActionCommand("Main");

		//Loads up the current queues
		addTenPieces(1);
		addTenPieces(2);

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

	//Parameters: int player
	//Return: void
	//Description adds ten random pieces to the queue, one of each of the seven and three random pieces
	public void addTenPieces(int player) {
		ArrayList<Integer> starters = new ArrayList<Integer>();

		for (int i =1 ; i<8; i++) {
			starters.add(i);
		}

		for (int i =0 ; i<3; i++) {
			starters.add((int)(Math.random()*7+1));
		}

		for (int i =10 ; i>0; i--) {
			int rand = (int) (Math.random()*i);
			if (player==1)
				queue1.add(makePiece(starters.get(rand)));
			else
				queue2.add(makePiece(starters.get(rand)));
			starters.remove(rand);
		}

	}

	//Parameters: int player
	//Return: void
	//Description: Sends a line to the other player
	public void sendLine(int player) {
		int random =(int)( Math.random()*10);
		boolean killed = false;
		if (player==1) {
			for (int i = 0; i<10; i++) {
				if (backMatrix2[i][0]>0) {
					killed=true;
				}
			}
			for (int i = 0; i<19; i++) {
				for (int j = 0; j<10; j++) {
					backMatrix2[j][i]=backMatrix2[j][i+1];
				}
			}
			for (int j = 0; j<10; j++) {
				if (j!=random)
					backMatrix2[j][19]=10;
				else
					backMatrix2[j][19]=0;
			}

			checkCollisions(0,2);
			if (killed)
				gameOver(2);
		}
		else if (player==2) {
			for (int i = 0; i<10; i++) {
				if (backMatrix1[i][0]>0) {
					killed=true;
				}
			}
			for (int i = 0; i<19; i++) {
				for (int j = 0; j<10; j++) {
					backMatrix1[j][i]=backMatrix1[j][i+1];
				}
			}
			for (int j = 0; j<10; j++) {
				if (j!=random)
					backMatrix1[j][19]=10;
				else
					backMatrix1[j][19]=0;
			}

			checkCollisions(0,1);
			if (killed)
				gameOver(1);
		}
	}

	//Parameters: int direction, int player
		//Return: void
		//Description: checks for collisions, reshifting a piece to its original spot if the movement causes a collision.
	public void checkCollisions(int d, int player) {

		int[] arr;

		if (player==1) {
			for (int i=0; i<4; i++) {
				arr=curPiece1.getOccupies()[i];
				if (arr[0]<0) {
					curPiece1.move(2);
				}
				else if (arr[0]>9) {
					curPiece1.move(1);
				}
				else if (arr[1]>23) {
					curPiece1.move(3);
					if (buffer1==0) 
						buffer1=System.currentTimeMillis();
					else if (System.currentTimeMillis()-buffer1>1000)
						plant(1);
				}

				else if (backMatrix1[arr[0]][Math.max(arr[1]-4,0)]!=0) {
					if (d==0) {
						curPiece1.move(3);
						if (buffer1==0) 
							buffer1=System.currentTimeMillis();
						else if (System.currentTimeMillis()-buffer1>1000)
							plant(1);
						return;
					}
					else if (d==1)
						curPiece1.move(2);
					else if (d==2)
						curPiece1.move(1);
				}
				else if (d==0) {
					lastDowned1=System.currentTimeMillis();
				}
			}


			if (buffer1!=0) {
				boolean resetBuffer=true;

				for (int i=0; i<4; i++) {
					arr=curPiece1.getOccupies()[i];
					if (arr[1]==23) {
						resetBuffer=false;
						break;
					}
					try {
						if (arr[1]>0&&backMatrix1[arr[0]][arr[1]-3]!=0) {
							resetBuffer=false;
							break;
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {

					}
				}
				if (resetBuffer)
					buffer1=0;
			}
		}
		else if (player==2) {
			for (int i=0; i<4; i++) {
				arr=curPiece2.getOccupies()[i];
				if (arr[0]<0) {
					curPiece2.move(2);
				}
				else if (arr[0]>9) {
					curPiece2.move(1);
				}
				else if (arr[1]>23) {
					curPiece2.move(3);
					if (buffer2==0) 
						buffer2=System.currentTimeMillis();
					else if (System.currentTimeMillis()-buffer2>1000)
						plant(2);
				}

				else if (backMatrix2[arr[0]][Math.max(arr[1]-4,0)]!=0) {
					if (d==0) {
						curPiece2.move(3);
						if (buffer2==0) 
							buffer2=System.currentTimeMillis();
						else if (System.currentTimeMillis()-buffer2>1000)
							plant(2);
						return;
					}
					else if (d==1)
						curPiece2.move(2);
					else if (d==2)
						curPiece2.move(1);
				}
				else if (d==0) {
					lastDowned2=System.currentTimeMillis();
				}
			}


			if (buffer2!=0) {
				boolean resetBuffer=true;

				for (int i=0; i<4; i++) {
					arr=curPiece2.getOccupies()[i];
					if (arr[1]==23) {
						resetBuffer=false;
						break;
					}

					try {
						if (arr[1]>0&&backMatrix2[arr[0]][arr[1]-3]!=0) {
							resetBuffer=false;
							break;
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {

					}
				}
				if (resetBuffer)
					buffer2=0;
			}
		}
	}

	//Parameters: int choice
	//Return: void
	//Description: recolours all pieces
	public void pieceColors(int choice) {
		if (choice==1) {
			colors.put(1, Color.yellow);
			colors.put(2, Color.orange);
			colors.put(3, Color.blue);
			colors.put(4, Color.green);
			colors.put(5, Color.red);
			colors.put(6, Color.magenta);
			colors.put(7, Color.cyan);
			colors.put(10, Color.gray);
		}
		else {
			colors.put(1, Color.gray);
			colors.put(2, Color.gray);
			colors.put(3, Color.gray);
			colors.put(4, Color.gray);
			colors.put(5, Color.gray);
			colors.put(6, Color.gray);
			colors.put(7, Color.gray);
			colors.put(10, Color.gray);
		}
	}

	//Parameters: none
	//Return: void
	//Description: ends the game, displays the end-game message
	public void gameOver(int player) {
		winner = player%2 + 1; //1 loses --> 2 wins. 2 loses --> 1 wins
		pieceColors(2);

		curMatrix1 = new int[10][24];
		curMatrix2 = new int[10][24];

		repaint();
		started=false;

		timer.stop();

		JOptionPane.showMessageDialog (this,"<html>PLAYER "+winner+" WINS.<br/><br/>Player "+player+" is awful.</html>","Player "+winner+" wins!",JOptionPane.INFORMATION_MESSAGE);
	}
	
	//Parameters: none
	//Return: void
	//Description: restarts the game
	public void restart(){

		if (!firstStart) {
			queue1.clear();
			queue2.clear();
			addTenPieces(1);
			addTenPieces(2);
		}
		pieceColors(1);

		backMatrix1 = new int[10][20];
		curMatrix1 = new int[10][24];
		ghostMatrix1 = new int[10][24];

		backMatrix2 = new int[10][20];
		ghostMatrix2 = new int[10][24];

		pressedLeft1=0;
		pressedRight1=0;
		pressedDown1=0;
		pressedLeft2=0;
		pressedRight2=0;
		pressedDown2=0;

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

	//Parameters: int player
	//Return: void
	//Description: Draws the ghost piece of a current piece onto the ghostMatrix
	public void drawGhostPiece(int player) {

		int[][] ghostSquares = new int [4][2];

		if (player==1) {
			for (int i=0; i<4; i++) {
				ghostSquares [i][0]=curPiece1.getOccupies()[i][0];
				ghostSquares [i][1]=curPiece1.getOccupies()[i][1];
			}

			try {
				if (curPiece1.getID()==7&&curPiece1.getRot()%2==1) {
					int highestY = -1;
					for (int i=0; i<4; i++) {
						highestY = Math.max(curPiece1.getOccupies()[i][1], highestY);
					}
					while (highestY<23&&backMatrix1[curPiece1.getOccupies()[0][0]][highestY-3]==0) {			
						highestY++;
					}
					ghostMatrix1=new int [10][24];
					for (int i=0; i<4; i++) {
						ghostMatrix1[curPiece1.getOccupies()[0][0]][highestY-i]=3;
					}
					return;
				}
				else {

					while (true) {
						for (int i=0; i<4; i++) {
							if (backMatrix1[ghostSquares[i][0]][Math.max(0, ghostSquares[i][1]-4)]!=0) {
								return;
							}
						}
						ghostMatrix1=new int [10][24];
						for (int i=0; i<4; i++) {
							ghostMatrix1[ghostSquares[i][0]][ghostSquares[i][1]]=3;
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
		else if (player==2) {
			for (int i=0; i<4; i++) {
				ghostSquares [i][0]=curPiece2.getOccupies()[i][0];
				ghostSquares [i][1]=curPiece2.getOccupies()[i][1];
			}

			try {
				if (curPiece2.getID()==7&&curPiece2.getRot()%2==1) {
					int highestY = -1;
					for (int i=0; i<4; i++) {
						highestY = Math.max(curPiece2.getOccupies()[i][1], highestY);
					}
					while (highestY<23&&backMatrix2[curPiece2.getOccupies()[0][0]][highestY-3]==0) {			
						highestY++;
					}
					ghostMatrix2=new int [10][24];
					for (int i=0; i<4; i++) {
						ghostMatrix2[curPiece2.getOccupies()[0][0]][highestY-i]=3;
					}
					return;
				}
				else {
					while (true) {
						for (int i=0; i<4; i++) {
							if (backMatrix2[ghostSquares[i][0]][Math.max(0, ghostSquares[i][1]-4)]!=0) {
								return;
							}
						}
						ghostMatrix2=new int [10][24];
						for (int i=0; i<4; i++) {
							ghostMatrix2[ghostSquares[i][0]][ghostSquares[i][1]]=3;
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
	}

	//Parameters: int player
	//Return: void
	//Description: Draws the current piece onto the curMatrix
	public void drawCur (int player) {	

		int[] arr;
		if (player==1) {
			try {
				for (int i=0; i<4; i++) {
					arr=curPiece1.getOccupies()[i];
					curMatrix1[arr[0]][arr[1]]=curPiece1.getID();
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				return;
			}

			drawGhostPiece(1);
		}
		else if (player==2) {
			try {
				for (int i=0; i<4; i++) {
					arr=curPiece2.getOccupies()[i];
					curMatrix2[arr[0]][arr[1]]=curPiece2.getID();
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				return;
			}

			drawGhostPiece(2);
		}
	}

	//Parameters: int player
	//Return: void
	//Description: Clears out the current matrix
	public void clearCur (int player) {	
		if (player==1) {
			curMatrix1 = new int[10][24];
			ghostMatrix1 = new int[10][24];
		}
		else if (player==2) {
			curMatrix2 = new int[10][24];
			ghostMatrix2 = new int[10][24];
		}
	}

	//Parameters: int player, int row
	//Return: void
	//Description: removes a line
	public void clearLine(int player, int row) {
		if (player==1) {
			for (int j = 0; j<10; j++) {
				backMatrix1[j][row]=0;
			}
			for (int i=row; i>0; i--) {
				for (int j=0; j<10; j++) {
					backMatrix1[j][i]=backMatrix1[j][i-1];
				}
			}
			for (int i = 0; i<10; i++) {
				backMatrix1[i][0]=0;
			}
			if (timer.isRunning())
				sendLine(1);
		}
		if (player==2) {
			for (int j = 0; j<10; j++) {
				backMatrix2[j][row]=0;
			}
			for (int i=row; i>0; i--) {
				for (int j=0; j<10; j++) {
					backMatrix2[j][i]=backMatrix2[j][i-1];
				}
			}
			for (int i = 0; i<10; i++) {
				backMatrix2[i][0]=0;
			}
			if (timer.isRunning())
				sendLine(2);
		}
	}

	//Parameters: int player
	//Return: void
	//Description: holds the current piece if allowed
	public void hold(int player) {
		if (player==1) {
			held1=true;
			curMatrix1 = new int[10][24];

			if (heldID1==0) {
				heldID1=curPiece1.getID();
				curPiece1=queue1.remove();
				curPiece1.move(0);
				checkCollisions(0,1);
				queue1.add(makePiece((int)(Math.random()*7)+1));
			}
			else {
				int temp = curPiece1.getID();
				curPiece1=makePiece(heldID1);
				heldID1=temp;
			}		
		}
		else if (player==2) {
			held2=true;
			curMatrix2 = new int[10][24];

			if (heldID2==0) {
				heldID2=curPiece2.getID();
				curPiece2=queue2.remove();
				curPiece1.move(0);
				checkCollisions(0,1);
				queue2.add(makePiece((int)(Math.random()*7)+1));
			}
			else {
				int temp = curPiece2.getID();
				curPiece2=makePiece(heldID2);
				heldID2=temp;
			}		
		}
	}


	//Parameters: int player
	//Return: void
	//Description: plants the current piece onto the backMatrix, managing game-overs and line-clears
	public void plant (int player) {
		if (player==1) {
			lastDowned1=System.currentTimeMillis();
			Set<Integer> rowsToClear = new TreeSet<Integer>();
			int[] arr;
			for (int i=0; i<4; i++) {
				boolean clear = true;
				arr=curPiece1.getOccupies()[i];
				try {
					backMatrix1[arr[0]][arr[1]-4]=curPiece1.getID();

					curMatrix1[arr[0]][arr[1]]=0;	

					for (int j = 0; j<10; j++) {
						if (backMatrix1[j][arr[1]-4]==0) {
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
							arr=curPiece1.getOccupies()[i];
							if (arr[1]-4>=0) {
								backMatrix1[arr[0]][arr[1]-4]=curPiece1.getID();
							}
						}
						gameOver(1);
						return;
					}
				}
			}

			boolean playSound1=false;
			if (!rowsToClear.isEmpty()) {
				playSound1=true;
				Iterator iter = rowsToClear.iterator();
				while (iter.hasNext()) {
					clearLine(1,(int) iter.next());
					for (Integer i : rowsToClear) {
						i++;
					}
				}
			}
			if (playSound1) {
				playSound("Sounds/clear right.wav");
			}

			curPiece1=queue1.remove();
			curPiece1.move(0);
			checkCollisions(0,1);
			if (queue1.size()<6) {
				addTenPieces(1);
			}

			held1=false;
			playSound("Sounds/drop right.wav");

		}
		else if (player==2) {
			lastDowned2=System.currentTimeMillis();


			Set<Integer> rowsToClear = new TreeSet<Integer>();
			int[] arr;
			for (int i=0; i<4; i++) {
				boolean clear = true;
				arr=curPiece2.getOccupies()[i];
				try {
					backMatrix2[arr[0]][arr[1]-4]=curPiece2.getID();

					curMatrix2[arr[0]][arr[1]]=0;	

					for (int j = 0; j<10; j++) {
						if (backMatrix2[j][arr[1]-4]==0) {
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
							arr=curPiece2.getOccupies()[i];
							if (arr[1]-4>=0) {
								backMatrix2[arr[0]][arr[1]-4]=curPiece2.getID();
							}
						}
						gameOver(2);
						return;
					}
				}
			}

			boolean playSound2 = false;
			if (!rowsToClear.isEmpty()) {
				playSound2=true;
				Iterator iter = rowsToClear.iterator();
				while (iter.hasNext()) {
					clearLine(2,(int) iter.next());
					for (Integer i : rowsToClear) {
						i++;
					}
				}
			}
			if (playSound2) {
				playSound("Sounds/clear left.wav");
			}
			curPiece2=queue2.remove();
			curPiece1.move(0);
			checkCollisions(0,1);
			if (queue2.size()<6) {
				addTenPieces(2);
			}
			held2=false;
			playSound("Sounds/drop left.wav");
		}
	}

	//Parameters: int player
	//Return: void
	//Description: forcefully plants a piece from any position onto the backMatrix
	public void hardDrop(int player) {

		if (player==1) {
			try {
				clearCur(1);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				gameOver(1);
				return;
			}
			while(true) {
				curPiece1.move(0);
				int[] arr;

				for (int i=0; i<4; i++) {
					arr=curPiece1.getOccupies()[i];
					if (arr[1]>23) {
						curPiece1.move(3);
						plant(1);
						return;
					}

					else if (backMatrix1[arr[0]][Math.max(arr[1]-4,0)]!=0) {
						curPiece1.move(3);
						plant(1);
						return;
					}

				}
			}
		}
		else if (player==2) {
			try {
				clearCur(2);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				gameOver(2);
				return;
			}
			while(true) {
				curPiece2.move(0);
				int[] arr;

				for (int i=0; i<4; i++) {
					arr=curPiece2.getOccupies()[i];
					if (arr[1]>23) {
						curPiece2.move(3);
						plant(2);
						return;
					}

					else if (backMatrix2[arr[0]][Math.max(arr[1]-4,0)]!=0) {
						curPiece2.move(3);
						plant(2);
						return;
					}
				}
			}
		}
	}


	//Main Method
	public static void main (String[] args) {

		frame = new JFrame ("Tetris Versus");
		panel = new TetrisVersus();
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
		g.fillRect(0, 0, 1300, 1000);

		//Draws the grid lines
		g.setColor(Color.white);
		for (int r = 0; r<20; r++) {
			for (int c = 0; c<10; c++) {		
				g.drawRect(175+c*25, 120+r*25, 25, 25);			
			}		
		}

		//Draws the pieces on the backMatrix
		g.setColor(Color.gray);
		for (int r = 0; r<20; r++) {
			for (int c = 0; c<10; c++) {		
				if (backMatrix2[c][r]!=0) {
					g.setColor(colors.get(backMatrix2[c][r]));
					g.fillRect(175+c*25, 120+r*25, 25, 25);
				}			
			}	
		}

		if (started) {
			//Draws the ghost piece

			if (drawGhost&&ghostPieceOn2) {
				Color starter = colors.get(curPiece2.getID());

				g.setColor(ghostColors.get(starter));
				for (int r = 4; r<24; r++) {
					for (int c = 0; c<10; c++) {		
						if (ghostMatrix2[c][r]!=0) {
							g.fillRect(175+c*25, 20+r*25, 25, 25);			
						}
					}	
				}
			}
		}

		//Draws the pieces on the curMatrix
		for (int r = 4; r<24; r++) {
			for (int c = 0; c<10; c++) {		
				if (curMatrix2[c][r]!=0) {
					g.setColor(colors.get(curMatrix2[c][r]));
					g.fillRect(175+c*25, 20+r*25, 25, 25);			
				}
			}	
		}

		//Draws the queue
		g.setColor(Color.white);		
		Iterator iter = queue2.iterator();

		double counter=0;
		for (int i = 0; i<5; i++) {

			Block b = (Block) iter.next();
			g.setColor(colors.get(b.getID()));
			int x = 485;
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
		g.setColor(colors.get(heldID2));
		int x = 75;
		int y = (int)(170);

		if (heldID2==1) {
			g.fillRect(x, y-25, 50, 50);
		}
		else if (heldID2==2) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x+25, y-25, 25, 25);
		}
		else if (heldID2==3) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x-25, y-25, 25, 25);
		}
		else if (heldID2==4) {
			g.fillRect(x-25, y, 50, 25);
			g.fillRect(x, y-25, 50, 25);
		}
		else if (heldID2==5) {
			g.fillRect(x, y, 50, 25);
			g.fillRect(x-25, y-25, 50, 25);
		}
		else if (heldID2==6) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x, y-25, 25, 25);
		}
		else if (heldID2==7) {
			g.fillRect(x-25, y, 100, 25);
		}


		//Draws the grid lines
		g.setColor(Color.white);
		for (int r = 0; r<20; r++) {
			for (int c = 0; c<10; c++) {		
				g.drawRect(825+c*25, 120+r*25, 25, 25);			
			}		
		}

		//Draws the pieces on the backMatrix
		g.setColor(Color.gray);
		for (int r = 0; r<20; r++) {
			for (int c = 0; c<10; c++) {		
				if (backMatrix1[c][r]!=0) {
					g.setColor(colors.get(backMatrix1[c][r]));
					g.fillRect(825+c*25, 120+r*25, 25, 25);
				}			
			}	
		}

		if (started) {
			//Draws the ghost piece
			if (drawGhost&&ghostPieceOn1) {
				Color starter = colors.get(curPiece1.getID());

				g.setColor(ghostColors.get(starter));
				for (int r = 4; r<24; r++) {
					for (int c = 0; c<10; c++) {		
						if (ghostMatrix1[c][r]!=0) {
							g.fillRect(825+c*25, 20+r*25, 25, 25);			
						}
					}	
				}
			}
		}

		//Draws the pieces on the curMatrix
		for (int r = 4; r<24; r++) {
			for (int c = 0; c<10; c++) {		
				if (curMatrix1[c][r]!=0) {
					g.setColor(colors.get(curMatrix1[c][r]));
					g.fillRect(825+c*25, 20+r*25, 25, 25);			
				}
			}	
		}


		//Draws the queue
		g.setColor(Color.white);		
		iter = queue1.iterator();

		counter=0;
		for (int i = 0; i<5; i++) {

			Block b = (Block) iter.next();
			g.setColor(colors.get(b.getID()));
			x = 1135;
			y = (int)(170+75*counter);

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
		g.setColor(colors.get(heldID1));
		x = 725;
		y = (int)(170);

		if (heldID1==1) {
			g.fillRect(x, y-25, 50, 50);
		}
		else if (heldID1==2) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x+25, y-25, 25, 25);
		}
		else if (heldID1==3) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x-25, y-25, 25, 25);
		}
		else if (heldID1==4) {
			g.fillRect(x-25, y, 50, 25);
			g.fillRect(x, y-25, 50, 25);
		}
		else if (heldID1==5) {
			g.fillRect(x, y, 50, 25);
			g.fillRect(x-25, y-25, 50, 25);
		}
		else if (heldID1==6) {
			g.fillRect(x-25, y, 75, 25);
			g.fillRect(x, y-25, 25, 25);
		}
		else if (heldID1==7) {
			g.fillRect(x-25, y, 100, 25);
		}


		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", 1, 20));

		g.drawString("Player One", 900, 700);
		g.drawString("Player Two", 250, 700);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String event = e.getActionCommand();

		//Starts a new game
		if (event.equals("Start")){
			drawGhost=true;
			held1 = false;
			heldID1 = 0;

			held2 = false;
			heldID2 = 0;
			started=true;

			restart();
			if (firstStart) {
				firstStart=false;
			}
			curPiece1 = queue1.remove();
			curPiece1.move(0);
			checkCollisions(0,1);
			curPiece2 = queue2.remove();
			curPiece1.move(0);
			checkCollisions(0,1);
		}
		
		//Calculates and animates what is supposed to happen in the next frame

		else if (event.equals("Main")){
			try {
				//moves piece
				clearCur(1);
				if (pressedLeft1!=0&&((sensitivity1*(frameCount-pressedLeft1))/20.0)%1<sensitivity1/20.0) {
					if (frameCount-pressedLeft1>40/sensitivity1||frameCount-pressedLeft1<20/sensitivity1) {
						curPiece1.move(1);
						checkCollisions(1,1);
					}
				}
				if (pressedRight1!=0&&((sensitivity1*(frameCount-pressedRight1))/20.0)%1<sensitivity1/20.0) {
					if (frameCount-pressedRight1>40/sensitivity1||frameCount-pressedRight1<20/sensitivity1) {
						curPiece1.move(2);
						checkCollisions(2,1);
					}
				}
				if (pressedDown1!=0&&((sensitivity1*(frameCount-pressedDown1))/20.0)%1<sensitivity1/20.0) {
					curPiece1.move(0);
					checkCollisions(0,1);
				}
			}
			catch (ArrayIndexOutOfBoundsException e1) {
				gameOver(1);
				return;
			}

			if (System.currentTimeMillis()-lastDowned1>1000) {
				lastDowned1=System.currentTimeMillis();
				curPiece1.move(0);
				checkCollisions(0,1);
			}

			drawCur(1);

			try {
				//moves piece
				clearCur(2);
				if (pressedLeft2!=0&&((sensitivity2*(frameCount-pressedLeft2))/20.0)%1<sensitivity2/20.0) {
					if (frameCount-pressedLeft2>40/sensitivity2||frameCount-pressedLeft2<20/sensitivity2) {
						curPiece2.move(1);
						checkCollisions(1,2);
					}
				}
				if (pressedRight2!=0&&((sensitivity2*(frameCount-pressedRight2))/20.0)%1<sensitivity2/20.0) {
					if (frameCount-pressedRight2>40/sensitivity2||frameCount-pressedRight2<20/sensitivity2) {
						curPiece2.move(2);
						checkCollisions(2,2);
					}
				}
				if (pressedDown2!=0&&((sensitivity2*(frameCount-pressedDown2))/20.0)%1<sensitivity2/20.0) {
					curPiece2.move(0);
					checkCollisions(0,2);
				}
			}
			catch (ArrayIndexOutOfBoundsException e1) {
				gameOver(2);
				return;
			}

			if (System.currentTimeMillis()-lastDowned2>1000) {
				lastDowned2=System.currentTimeMillis();
				curPiece2.move(0);
				checkCollisions(0,2);
			}

			drawCur(2);
			
			//Repaints with new positions
			repaint();
			frameCount++;
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
			if (key==moveLeft1) {
				if (pressedLeft1==0) {
					pressedRight1=0;
					pressedLeft1=frameCount;
				}
			}
			else if (key==moveRight1) {
				if (pressedRight1==0) {
					pressedLeft1=0;
					pressedRight1=frameCount;
				}
			}
			else if (key==moveDown1) {
				if (pressedDown1==0) {
					pressedDown1=frameCount;
				}
			}
			else if (key==hardDrop1) {
				hardDrop(1);
			}
			else if (key==rotateRight1) {
				curPiece1.rotateRight(backMatrix1);
				drawCur(1);
			}
			else if (key==hold1) {
				if (!held1) 
					hold(1);
			}

			else if (key==rotateLeft1) {
				curPiece1.rotateLeft(backMatrix1);
				drawCur(1);

			}

			if (key==moveLeft2) {
				if (pressedLeft2==0) {
					pressedRight2=0;
					pressedLeft2=frameCount;
				}
			}
			else if (key==moveRight2) {
				if (pressedRight2==0) {
					pressedLeft2=0;
					pressedRight2=frameCount;
				}
			}
			else if (key==moveDown2) {
				if (pressedDown2==0) {
					pressedDown2=frameCount;
				}
			}
			else if (key==hardDrop2) {
				hardDrop(2);
			}
			else if (key==rotateRight2) {
				curPiece2.rotateRight(backMatrix2);
				drawCur(2);
			}
			else if (key==hold2) {
				if (!held2) 
					hold(2);
			}
			else if (key==rotateLeft2) {
				curPiece2.rotateLeft(backMatrix2);
				drawCur(2);
			}
		}
	}

	//Parameters: KeyEvent e
	//Return: void
	//Description: Overridden method for moving pieces
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (started) {
			if (key==moveLeft1) {
				if (frameCount==pressedLeft1) {
					curPiece1.move(1);
					checkCollisions(1,1);
				}
				pressedLeft1=0;
			}
			else if (key==moveRight1) {
				if (frameCount==pressedRight1) {
					curPiece1.move(2);
					checkCollisions(2,1);

				}
				pressedRight1=0;
			}
			else if (key==moveDown1) {
				pressedDown1=0;
			}

			if (key==moveLeft2) {
				if (frameCount==pressedLeft2) {
					curPiece2.move(1);
					checkCollisions(1,2);
				}
				pressedLeft2=0;
			}
			else if (key==moveRight2) {
				if (frameCount==pressedRight2) {
					curPiece2.move(2);
					checkCollisions(2,2);

				}
				pressedRight2=0;
			}
			else if (key==moveDown2) {
				pressedDown2=0;
			}
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