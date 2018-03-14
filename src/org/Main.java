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
	Paint paint;
	
	public Main() {
		ignoreCheap = new HashMap<Integer, Integer>();
	}
	
    @Override
    public void onStart() {
    		ui = new UI();
    		paint = new Paint();
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

    @Override
    public void onPaint(Graphics2D g) {
    		paint.onPaint(g, mouse.getPosition(), itemToGet);
	}
    


}