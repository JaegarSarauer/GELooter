package org;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.osbot.rs07.api.Skills;

public class State {

	public static State instance;
	
	public static State getInstance() {
		if (State.instance == null) {
			State.instance = new State();
		}
		return State.instance;
	}
	
	private State() {

	}
	
	
	
	/*
	 * Data variables.
	 */
	
	//has/should script start?
	public boolean startScript = false;
	
	//minimum value of a stack or single item to pickup. (1000 coins would surpass this value if it were under 1000).
	public int minimumValue = 0;
	
	//loots acquired
	public int loots = 0;
	
	//total value acquired
	public int totalProfit = 0;
}
