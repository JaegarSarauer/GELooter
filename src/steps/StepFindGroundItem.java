package steps;

import org.Helper;
import org.State;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.GroundItem;

public class StepFindGroundItem extends Step {
	
	StepFindGroundItem(String state) {
		super(state);
	}

	public boolean shouldActivate() {
		return !State.i.myPlayer.isMoving() 
				|| State.i.itemToGet == null 
				|| (State.i.itemToGet != null 
					&& !Helper.itemStillExists(State.i.itemToGet)
				);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean activate() {	
		State.i.itemToGet = s.groundItems.closest(new Filter<GroundItem>() {

			@Override
			public boolean match(GroundItem item) {
				if (item == null)
					return false;
				int id = item.getDefinition().getUnnotedId();
//				for (int i = 0; i < State.i.ignoreItems.length; i++)
//					if (id == State.i.ignoreItems[i])
//						return skip(item, item.getAmount());
//				for (int i = 0; i < State.i.ignoreStrings.length; i++)
//					if (item.getName().contains(State.i.ignoreStrings[i]))
//						return skip(item, item.getAmount());
				if (!State.GEArea.contains(item.getPosition()))
					return false;
				if (State.i.ignoreCheap.containsKey(id) && item.getAmount() < State.i.ignoreCheap.get(id)) {
					return false;
				}
				int val = Helper.lookupValue(id, item.getAmount());
				//s.log("Item found: " + item.getName() + "(" + item.getAmount() + ")" + " Price: " + val);
				if (val < State.i.minimumValue) {
					return false;
				}
				s.log("Item chose: " + item.getName());
				State.i.itemToGetTimestamp = System.currentTimeMillis() + State.i.minNoItemWait;
				State.i.itemToGetValue = val;
				State.i.itemCountPreviousToLoot = s.inventory.getAmount(item.getId());
				return true;
			}
	    		
	    	});
		return true;
	}

}