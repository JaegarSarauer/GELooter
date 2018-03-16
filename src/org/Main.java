package org;

import org.osbot.rs07.script.Script;

import org.osbot.rs07.script.ScriptManifest;

import steps.StepControl;

import java.awt.*;
@ScriptManifest(name = "GE Looter", author = "apa/niceface911", version = 1.0, info = "", logo = "") 



public class Main extends Script {
	
	UI ui;
	Paint paint;
	
	//reference script.
	public static Script script;
	
	public Main() {
		script = this;
	}
	
    @Override
    public void onStart() {
    		ui = new UI();
    		paint = new Paint();
    }

    @Override
    public void onExit() {
    }

	@Override
    public int onLoop() {
    		if (!State.i.startScript)
    			return (int)(Math.random() * 100) + 100;
    		
    		try {
    			State.i.myPlayer = players.myPlayer();
    			StepControl.i.execute();
    		} catch (Exception e) {
    			e.printStackTrace();
    			log("Fatal Error!");
    	        return (int)(Math.random() * 2000) + 3000;
    		}
    		
        return (int)(Math.random() * 50) + 50;
    }

    @Override
    public void onPaint(Graphics2D g) {
    		paint.onPaint(g, mouse.getPosition());
	}
    


}