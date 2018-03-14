package org;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScriptManifest(name = "GE Looter", author = "apa/niceface911", version = 1.0, info = "", logo = "") 



public class Main extends Script {
	
	public long startTime = System.currentTimeMillis();

	public Area GEArea = new Area(3144, 3512, 3182, 3472);

	public int[] ignoreItems = {592, 229};
	public String[] ignoreStrings = {"Burnt", "Raw"};
	public Map<Integer, Integer> ignoreCheap;
	
	public static final int MAX_BANK_SLOTS = 400;
	
	public int minRunAmount;
	
	GroundItem itemToGet;
	int itemToGetValue;
	long itemCountPreviousToLoot;
	
	UI ui;
	
	public Main() {
		ignoreCheap = new HashMap<Integer, Integer>();
	}
	
    @Override
    public void onStart() {
    		ui = new UI();
    }

    @Override
    public void onExit() {
    }

    @SuppressWarnings("unchecked")
	@Override
    public int onLoop() {
    		if (!State.getInstance().startScript)
    			return (int)(Math.random() * 100) + 100;
    		Player myPlayer = players.myPlayer();
    		slowUpdate();
    		if (!settings.isRunning() && settings.getRunEnergy() > minRunAmount) {
    			settings.setRunning(true);
    			minRunAmount = settings.getRunEnergy() + 10;
    		}
    		if (!myPlayer.isMoving() && !GEArea.contains(myPlayer.getPosition())) {
    			walking.walk(GEArea);
    		}
    		if (!myPlayer.isMoving() && inventory.isFull() && !bank.isOpen()) {
    			try {
					bank.open();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		if (inventory.isFull() && bank.isOpen()) {
    			bank.depositAll();
    		}
    		if (!inventory.isFull() && bank.isOpen()) {
    			bank.close();
        		if (bank.getItemCountForTab(0) >= MAX_BANK_SLOTS) {
        			stop();
        		}
    		}
    		if (itemToGet != null && !itemStillExists(itemToGet)) {
    			if (inventory.contains(itemToGet.getId()) && inventory.getAmount(itemToGet.getId()) > itemCountPreviousToLoot) {
    				State.getInstance().loots++;
    				State.getInstance().totalProfit += itemToGetValue;
    			}
    			itemToGet = null;
    		}
    		if (!myPlayer.isMoving() || itemToGet == null || (itemToGet != null && !itemStillExists(itemToGet))) { 
		    itemToGet = groundItems.closest(new Filter<GroundItem>() {
		
				@Override
				public boolean match(GroundItem item) {
					if (item == null)
						return false;
					for (int i = 0; i < ignoreItems.length; i++)
						if (item.getId() == ignoreItems[i])
							return false;
					for (int i = 0; i < ignoreStrings.length; i++)
						if (item.getName().contains(ignoreStrings[i]))
							return false;
					if (ignoreCheap.containsKey(item.getId()) && item.getAmount() < ignoreCheap.get(item.getId())) {
						return false;
					}
					itemToGetValue = lookupValue(item.getId(), item.getAmount());
					itemCountPreviousToLoot = inventory.getAmount(item.getId());
					return true;
				}
		    		
		    	});
		    if (itemToGet != null) {
			    	if (myPlayer.getPosition().distance(itemToGet.getPosition()) < 14) {
		    			itemToGet.interact("Take");
			    	}
		    }
    		}
    		
        return (int)(Math.random() * 100) + 100;
    }
    
    
    /*
     * 
     * Used for updating over every few minutes.
     */
    private long lastSlowUpdate = System.currentTimeMillis();
    private long nextUpdate = 0;
    public boolean slowUpdate() {
    		long now = System.currentTimeMillis();
    		
    		if (lastSlowUpdate + nextUpdate > now) {
    			return false;
    		}
    		//insert updates here
    		minRunAmount = (int) (4 + (Math.random() * 30));
    		//insert updates here
		lastSlowUpdate = now;
		nextUpdate = (long) (180000l * Math.random());
		return true;
    }
    
    public int lookupValue(int id, int amount) {
    		try {
			int price = PriceLookup.getInstance().getOverallPrice(id);
			if (price <= 1)
				price = 1;
			int value = price * amount;
			if (value < State.getInstance().minimumValue) {
				ignoreCheap.put(id, State.getInstance().minimumValue / price);
			}
			return value;
		} catch (IOException e) {
			e.printStackTrace();
			ignoreCheap.put(id, State.getInstance().minimumValue);
			return 1;
		}
    }
    
    public boolean itemStillExists(GroundItem item) {
    		if (item == null)
    			return false;
    		GroundItem check = groundItems.closest(item.getId());
    		if (check == null)
    			return false;
    		return check.getAmount() == item.getAmount() && check.getX() == item.getX() && check.getY() == item.getY();
    }
    
    public boolean objectStillExists(RS2Object object) {
    		if (object == null)
    			return false;
    		RS2Object check = objects.closest(object.getId());
    		if (check == null)
    			return false;
    		return check.getX() == object.getX() && check.getY() == object.getY();
    }

    Font paintFont = new Font("Times New Roman", Font.PLAIN, 12);
    @Override
    public void onPaint(Graphics2D g) {
    		Point p = mouse.getPosition();
    		g.setFont(paintFont);
    		g.setColor(Color.BLACK);
    		g.fillRect(290, 340, 205, 108);
    		g.setColor(Color.MAGENTA);
    		g.drawOval(p.x, p.y, 3, 3);
    		g.drawString("~ Apa's GE Looter ~", 295, 360);
    		g.drawString("Runtime: " + calcRuntime(), 295, 380);
		g.drawString("Loots: " + State.getInstance().loots + " (" + calcHourly(State.getInstance().loots) + ")", 295, 400);
    		g.drawString("Profit: " + coolFormat(State.getInstance().totalProfit, 0) + " (" + coolFormat(calcHourly(State.getInstance().totalProfit), 0) + ")", 295, 420);
    }
    
    public int calcHourly(int v) {
    		if (v == 0)
    			return 0;
    		return (int)((float)v / ((System.currentTimeMillis() - startTime) / 3600000f));
    }

    private static char[] c = new char[]{'k', 'm', 'b', 't'};

    /**
     * Recursive implementation, invokes itself for each factor of a thousand, increasing the class on each invokation.
     * @param n the number to format
     * @param iteration in fact this is the class from the array c
     * @return a String representing the number n formatted in a cool looking way.
     */
    private static String coolFormat(double n, int iteration) {
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
            ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
             (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
             ) + "" + c[iteration]) 
            : coolFormat(d, iteration+1));

    }
    
    public String calcRuntime() {
    		int secsPassed = (int)((System.currentTimeMillis() - startTime) / 1000);
    		int hours = secsPassed / 3600;
    		secsPassed -= hours * 3600;
    		int minutes = secsPassed / 60;
    		secsPassed -= minutes * 60;
    		return (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (secsPassed < 10 ? "0" + secsPassed : secsPassed);
    }

}