package steps;

import org.State;

public class StepStartRunning extends Step {

	StepStartRunning(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return !s.settings.isRunning() && s.settings.getRunEnergy() > State.i.minRunAmount;
	}

	@Override
	public boolean activate() {
		s.settings.setRunning(true);
		State.i.minRunAmount = s.settings.getRunEnergy() + 10;
		return true;
	}

}
