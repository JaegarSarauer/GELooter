package steps;

import java.util.ArrayList;

import org.Main;
import org.State;

public class StepControl {

	public static final StepControl i = new StepControl();

	private ArrayList<Step> steps;

	private StepControl() {
		steps = new ArrayList<Step>();
		initSteps();
	}

	private void initSteps() {
		steps.add(new StepMoveInBounds("moving back to area"));
		steps.add(new StepSlowUpdate(null));
		steps.add(new StepStartRunning("turning on run"));
		steps.add(new StepBankOpen("opening bank"));
		steps.add(new StepBankDeposit("depositing items"));
		steps.add(new StepBankClose("closing bank"));
		steps.add(new StepCheckGroundItem(null));
		steps.add(new StepInteractWithGroundItem("after an item"));
		steps.add(new StepFindGroundItem("looking for item"));
		steps.add(new StepMoveInnerGE("moving into inner area"));
	}

	public boolean execute() {
		for (Step s : steps) {
			if (s.shouldActivate()) {
				s.updateState();
				s.activate();
			}
		}
		return true;
	}
}
