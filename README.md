# Tetris

ALEX HU: TETRIS. MADE FOR ICS4U.

Features of the game as originally intended:

	•Single player mode, where the user clears 40 lines 
	•Versus mode, where two players each try to outlast their opponent
	•Control Customization: Each player can change their controls, sensitivity, 
	and some visuals
	•Leaderboards: Users can view the name and duration of all previous games played 
	(to completion) and can sort the list by 4 criteria


Hints:
	•In single player, pressing the delete/backspace button will swap out the 
	currently held piece with a new, random one, and can be done as many times as wished
	•In TetrisMain.java, set 'private final int LINES' to a lower number to make 
	games go by faster
	•In TetrisMain.java, set 'private final int hangTime' to a greater number to have 
	the pieces fall slower
	•The leaderboards.txt file is of the format (time in milliseconds + " " + name)
	
	
Additional Functionalities:
	•Instructions screen now displays a single looping replay of my gameplay
 

Missing Functionalities:
	•Replay system was not implemented, but it was only to be implemented if
	time permitted


Bugs/Errors:
	•Closing the windows for Solo and Versus while the background music is playing
	does not pause the music; you have to manually pause music before closing the tab


Things To Note:
	•MainMenu.java is the Driver class, but the TetrisMain, TetrisVersus, ControlCustomizer, 
	and Leaderboards classes can all be run by themselves

	•Block.java is an abstract class and a parent to seven subclasses (LBlock, ZBlock, etc)

	•Maps
		- TetrisMain, TetrisVersus: mapping Integers to Colors, Colors to Colors
		- ControlCustomizer: mapping Integers (key codes) to Strings (common names)
	•Lists & Sets
		- TetrisMain, TetrisVersus: 'Queue<Block> queue' contains the queue of upcoming blocks
		- TetrisMain, TetrisVersus: 'ArrayList<Integer> starters' contains the random blocks to be added to the queue
		- TetrisMain, TetrisVersus: 'Set<Integer> rowsToClear' contains the lines to be cleared upon placing a piece
		- ControlCustomizer: 'ArrayList<Integer> keyCodes' contains the list of keys that are currently bound to actions
		- Leaderboards: 'ArrayList<Game> games' contains the list of games to be sorted, displayed
