package org;

import java.io.IOException;

import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.RS2Object;

public class Helper {
	public static final Helper i = new Helper();

	private Helper() {

	}

	public static boolean itemStillExists(GroundItem item) {
		if (item == null)
			return false;
		GroundItem check = Main.script.groundItems.closest(item.getId());
		if (check == null)
			return false;
		return check.getAmount() == item.getAmount() && check.getX() == item.getX() && check.getY() == item.getY();
	}

	public static boolean objectStillExists(RS2Object object) {
		if (object == null)
			return false;
		RS2Object check = Main.script.objects.closest(object.getId());
		if (check == null)
			return false;
		return check.getX() == object.getX() && check.getY() == object.getY();
	}

    public static int lookupValue(int id, int amount) {
    		try {
			int price = PriceLookup.i.getOverallPrice(id);
			if (price <= 1)
				price = 1;
			int value = price * amount;
			if (value < State.i.minimumValue) {
				State.i.ignoreCheap.put(id, State.i.minimumValue / price);
			}
			return value;
		} catch (IOException e) {
			e.printStackTrace();
			State.i.ignoreCheap.put(id, State.i.minimumValue);
			return 1;
		}
    }

	public static int lookupPrice(int id) {
		try {
			int price = PriceLookup.i.getOverallPrice(id);
			if (price <= 1)
				price = 1;
			return price;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}
}
