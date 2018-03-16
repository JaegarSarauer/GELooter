package steps;

import org.State;

public class StepMoveInnerGE extends StepNotMoving {

	StepMoveInnerGE(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return (System.currentTimeMillis() - State.i.itemToGetTimestamp) >= State.i.minNoItemWait && !State.innerGEArea.contains(State.i.myPlayer.getPosition());
	}

	@Override
	public boolean activate() {
		State.i.itemToGetTimestamp = System.currentTimeMillis() + State.i.minNoItemWait;
		s.walking.webWalk(State.innerGEArea);
		return true;
	}

}
