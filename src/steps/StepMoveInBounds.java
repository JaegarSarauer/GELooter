package steps;

import org.State;

public class StepMoveInBounds extends StepNotMoving {

	StepMoveInBounds(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return !State.GEArea.contains(State.i.myPlayer.getPosition());
	}

	@Override
	public boolean activate() {
		s.walking.webWalk(State.GEArea);
		return true;
	}

}
