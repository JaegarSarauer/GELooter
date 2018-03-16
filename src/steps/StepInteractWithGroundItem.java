package steps;

import org.Helper;
import org.State;
import org.osbot.rs07.api.Mouse;
import org.osbot.rs07.api.map.Position;

public class StepInteractWithGroundItem extends Step {

	StepInteractWithGroundItem(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return State.i.itemToGet != null;
	}

	@Override
	public boolean activate() {
		Position pos = State.i.itemToGet.getPosition();
	    	if (State.i.myPlayer.getPosition().distance(pos) < 14 && State.i.itemToGetState != State.InteractState.INTERACTED) {
	    		if (State.i.itemToGet.interact("Take")) {
	    			if (s.mouse.getCrossHairColor() != Mouse.CrossHairColor.YELLOW) {
	    				State.i.itemToGetState = State.InteractState.INTERACTED;
	    			}
	    		}
	    	} else if (State.i.itemToGetState != State.InteractState.WALKING) {
	    		s.walking.webWalk(pos);
	    		State.i.itemToGetState = State.InteractState.WALKING;
	    	}
		return true;
	}

}
