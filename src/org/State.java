package org;

import java.util.HashMap;
import java.util.Map;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Player;

public class State {

	public static final State i = new State();
	
	private State() {
		ignoreCheap = new HashMap<Integer, Integer>();
	}
	
	/*
	 * Data variables.
	 */
	//max bank slots in f2p
	public static final int MAX_BANK_SLOTS = 400;
	
	//script start time
	public static final long START_TIME = System.currentTimeMillis();
	
	//Walkable area of the GE
	public static final Area GEArea = new Area(3144, 3512, 3182, 3472);
	
	//Inner area of the GE
	public static final Area innerGEArea = new Area(3161, 3493, 3168, 3486);
	
	//has/should script start?
	public boolean startScript = false;
	
	//minimum value of a stack or single item to pickup. (1000 coins would surpass this value if it were under 1000).
	public int minimumValue = 0;
	
	//loots acquired
	public int loots = 0;
	
	//total value acquired
	public int totalProfit = 0;
	
	//minimum amount to start running.
	public int minRunAmount;
	
	//minimum amount of time to move to inner GE area.
	public int minNoItemWait = (int) (10000 + (Math.random() * 40000));

	//ignore lists for items.
	public int[] ignoreItems = {592, 229};
	public String[] ignoreStrings = {"Burnt", "Raw"};
	public Map<Integer, Integer> ignoreCheap;
	
	//updated game variables
	public Player myPlayer;
	
	public GroundItem itemToGet;
	public long itemToGetTimestamp = System.currentTimeMillis();
	public InteractState itemToGetState = InteractState.NONE;
	public static enum InteractState {NONE, WALKING, INTERACTED};
	public int itemToGetValue;
	public long itemCountPreviousToLoot;

	//current executing step.
	public String state = "doing nothing";
}
