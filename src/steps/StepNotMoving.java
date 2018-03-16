package steps;

import org.State;

public abstract class StepNotMoving extends Step {

	StepNotMoving(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return !State.i.myPlayer.isMoving();
	}
}
