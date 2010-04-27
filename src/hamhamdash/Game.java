package hamhamdash;

import java.util.ArrayList;
import jgame.*;
import jgame.platform.*;

/**
 *
 * @author Cornel Alders
 */
public class Game extends JGEngine
{
	// DEBUG VAR!
	public boolean debug = true;


	// Define "GLOBAL" Vars
	public boolean loadGame = false; // by default 'New Game' is selected
	public int stateCounter = 0;
	private ArrayList<String> states = new ArrayList<String>();
	public Player player = null;
	public Enemy enemy = null;
	private Levels objLevels;
	public String passString;
	public int tileWidth = 40;
	public int tileHeight = 40;
	private int xofs, yofs = 0;
	private State currentState = null;
	private State previousState = null;
	private int timer = 0;
	private int timercounter = 0;

//***************************************
// Start Game initialization
//***************************************
	private Game(JGPoint dimension)
	{
		initEngine(dimension.x, dimension.y);
		//setGameSpeed(0.8);
	}

	/**
	 * Gets loaded on first execution of getGame() OR at the first call GameHolder.INSTANCE
	 * Next call will return the instance of the game
	 */
	private static class GameHolder
	{
		private static final Game INSTANCE = new Game(new JGPoint(400, 400));
	}

	/**
	 * Returns the instance of the game
	 * @return
	 */
	public static Game getGame()
	{
		return GameHolder.INSTANCE;
	}

	public void initCanvas()
	{
		setCanvasSettings(10, 10, tileWidth, tileHeight, JGColor.white, new JGColor(44, 44, 44), null);
	}

	public void initGame()
	{
		setFrameRate(45, 2);
		setVideoSyncedUpdate(true);
		defineMedia("datasheets/spritetable.tbl");

		objLevels = new Levels();
		states.add("Title");
		states.add("PlayerSelect");
		states.add("StartGame");
		states.add("EnterPwd");
		states.add("InGame");

		if (debug)
		{
			dbgShowBoundingBox(true);
			dbgShowGameState(true);
		}
		// Start the game in title screen
		setCurrentState("Title");
	}

	public static void main(String[] args)
	{
		Game.getGame();
	}

//***************************************
// End Game initialization
//***************************************
//***************************************
// Start default loop
//***************************************
	@Override
	public void doFrame()
	{
		if(!inGameState("Death"))
		{
			if(!inGameState("Pause"))
			{
				if(inGameState("InGame"))
				{
					stateCounter = 0;

					if(getKey(KeyEsc))
					{
						clearKey(KeyEsc);
						addState("Pause");
					}
				}
				else if(inGameState("EnterPwd"))
				{
					if(getKey(KeyEnter))
					{
						clearKey(KeyEnter);
						passString = "";
						for(String perPass : passPosList)
						{
							passString += perPass;
						}
						if(getObjLevels().checkPassword(passString))
						{
							passIsCorrect = true;
							stateCounter = nextState();
						}
						else
						{
							passIsCorrect = false;
							passAttempt++;
						}
					}
					else if(getKey(KeyEsc))
					{
						clearKey(KeyEsc);
						stateCounter = prevState();
					}
				}
				else if(inGameState("PlayerSelect"))
				{
					if(getKey(KeyEnter))
					{
						clearKey(KeyEnter);
						setPlayer(new Player());
						stateCounter = nextState();
					}
					else if(getKey(KeyEsc))
					{
						clearKey(KeyEsc);
						stateCounter = prevState();
					}
				}
				else
				{
					if(getKey(KeyEnter))
					{
						// next step is player selection
						clearKey(KeyEnter);
						if(inGameState("Restart"))
						{
							setCurrentState("InGame");
						}
						else
						{
							stateCounter = nextState();
						}
					}
					else if(getKey(KeyEsc))
					{
						clearKey(KeyEsc);
						stateCounter = prevState();
					}
				}
			}
		}
		else if(inGameState("Death"))
		{
			moveObjects("h", 0);
		}

		// DBG MSG's
		if(debug)
		{
			dbgPrint("LoadGame = " + loadGame);
			dbgPrint(getKeyDesc(getLastKey()) + " was pressed");
			dbgPrint(inGameState("EnterPwd") + "");
		}
	}

	@Override
	public void paintFrame()
	{
		if (!(inGameState("InGame")) && !(inGameState("Restart")))
		{
			drawImage(0, 0, "menu_bg");
			int x = viewWidth() - 30;
			drawString("<ESC>    -            Back", x, viewHeight() - 60, 1, true);
			drawString("<ENTER>    -             Next", x, viewHeight() - 40, 1, true);
			drawString("<ARROWS>    -   Navigation", x, viewHeight() - 20, 1, true);
		}
	
	}

//***************************************
// End default loop
//***************************************
//***************************************
// Start Game State Title
//***************************************
	public void startTitle()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameTitle()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameTitle()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Title
//***************************************
//***************************************
// Start Game State Player Select
//***************************************

	public void startPlayerSelect()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFramePlayerSelect()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFramePlayerSelect()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Player Select
//***************************************
//***************************************
// Start Game State Start Game
//***************************************

	public void startStartGame()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameStartGame()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameStartGame()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Start Game
//***************************************
//***************************************
// Start Game State Enter Password
//***************************************
	// 1 var for each password position, these vars will combine to be the passString
	public String[] passPosList;
	public boolean passIsCorrect = false;
	public int passAttempt = 0;

	public void startEnterPwd()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameEnterPwd()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameEnterPwd()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Enter Password
//***************************************
//***************************************
// Start Game State InGame
//***************************************

	public void startInGame()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameInGame()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameInGame()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State InGame
//***************************************

//***************************************
// Start Game State Death
//***************************************
	public void startDeath()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameDeath()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameDeath()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Death
//***************************************
//***************************************
// Start Game State Restart
//***************************************

	public void startRestart()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameRestart()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameRestart()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Restart
//***************************************

//***************************************
// Start Game State Pause
//***************************************
	public void startPause()
	{
		if (inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFramePause()
	{
		if (inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFramePause()
	{
		if (inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Pause
//***************************************
//***************************************
// Start Game State Win
//***************************************

	public void startWin()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameWin()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameWin()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State Win
//***************************************
//***************************************
// Start Game State GameOver
//***************************************

	public void startGameOver()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().start();
		}
	}

	public void doFrameGameOver()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().doFrame();
		}
	}

	public void paintFrameGameOver()
	{
		if (!inGameState("Pause"))
		{
			getCurrentState().paintFrame();
		}
	}
//***************************************
// End Game State GameOver
//***************************************

	// Global Method(s)
	public int nextState()
	{
		if (stateCounter < states.size() - 1)
		{
			stateCounter++;
		}
		if (inGameState("StartGame") && !loadGame)
		{
			stateCounter++;
		}
		setCurrentState(states.get(stateCounter));

		return stateCounter;
	}

	public int prevState()
	{
		if (stateCounter > 0)
		{
			stateCounter--;
		}
		setCurrentState(states.get(stateCounter));
		return stateCounter;
	}

	// Getter(s) & Setter(s)
	public int getTileSize()
	{
		return 40;
	}

	public int getViewportWidth()
	{
		return 200;
	}

	public int getViewportHeight()
	{
		return 200;
	}

	/**
	 * Sets the PlayField size based on the size of the current level
	 * @param t int[width, height] The size of the current level
	 */
	public void setFieldSize(int[] t)
	{
		setPFSize(t[0], t[1]);
	}

	public Level getCurrentLevel()
	{
		return getObjLevels().getCurrentLevel();
	}

	/**
	 * @return the objLevels
	 */
	public Levels getObjLevels()
	{
		return objLevels;
	}

	public int getXoffset()
	{
		return xofs;
	}

	public int getYoffset()
	{
		return yofs;
	}

	public void setXoffset(int i)
	{
		this.xofs = i;
	}

	public void setYoffset(int i)
	{
		this.yofs = i;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public State getCurrentState()
	{
		return currentState;
	}

	/**
	 * Set the currente Game State ex: "InGame" or "Title"
	 * Will create a new Instance of the given state class and stores ref in currentState.
	 * @param state The state name
	 */
	public void setCurrentState(String state)
	{
		this.currentState = getStateClass(state);
		setGameState(state);
	}

	/**
	 * Add a Game State ex: "InGame" or "Title", to the current Game State
	 * Will create a new Instance of the given state class and stores ref in currentState.
	 * @param state The state name
	 */
	public void addState(String state)
	{
		saveState();
		this.currentState = getStateClass(state);
		addGameState(state);
	}

	public void saveState()
	{
		this.previousState = currentState;
	}

	public void recoverState()
	{
		currentState = previousState;
	}

	/**
	 * Dynamically searches for the given state and returns a new instance of it.
	 *
	 * @param state
	 * @return
	 */
	public State getStateClass(String state)
	{
		State c = null;
		try
		{
			//State c = (State) Class.forName("hamhamdash.states.State" + state).newInstance();
			c = (State) Class.forName("hamhamdash.states.State" + state).newInstance();
			return c;
		}
		catch(ClassNotFoundException cnfe){System.out.println("Class not found!");}
		catch(InstantiationException ie){System.out.println("Instantiation Error!");}
		catch(IllegalAccessException iae){System.out.println("Illegal Access!");}
		return null;
	}

	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player p)
	{
		this.player = p;
	}

	/**
	 * Resets the viewpoint offset to zero
	 */
	public void resetViewport()
	{
		setViewOffset(0, 0, true);
		setPFSize(10, 10);
	}

	/**
	 * Starts the level timer.
	 * This should only be called in the InGame state.
	 */
	public void startTimer()
	{
		timercounter = 1;
		timer -= timercounter;
	}

	/**
	 * Stop the timer. Use it in states like Pause.
	 */
	public void stopTimer()
	{
		timercounter = 0;
	}

	/**
	 * Resets the level timer.
	 * Should be called when a life has been lost and when changing levels.
	 */
	public void resetTimer()
	{
		timer = getObjLevels().getCurrentLevel().getLevelTimer();
	}

	/**
	 * Returns the current value of the level timer.
	 * @return amount of time left
	 */
	public int getTimer()
	{
		return timer;
	}
}
