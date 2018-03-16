package steps;

import org.Helper;
import org.State;

public class StepCheckGroundItem extends Step {

	StepCheckGroundItem(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return State.i.itemToGet != null && !Helper.itemStillExists(State.i.itemToGet);
	}

	@Override
	public boolean activate() {
		s.log(s.inventory.contains(State.i.itemToGet.getId()) + " : " + (s.inventory.getAmount(State.i.itemToGet.getId()) + " : " + State.i.itemCountPreviousToLoot));
		if (s.inventory.contains(State.i.itemToGet.getId()) && s.inventory.getAmount(State.i.itemToGet.getId()) > State.i.itemCountPreviousToLoot) {
			State.i.loots++;
			State.i.totalProfit += State.i.itemToGetValue;
		}
		State.i.itemToGet = null;
		State.i.itemToGetState = State.InteractState.NONE;
		return true;
	}

}
