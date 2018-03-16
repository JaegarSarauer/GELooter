package steps;

import org.State;

public class StepBankOpen extends Step {

	StepBankOpen(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return !State.i.myPlayer.isMoving() && s.inventory.isFull() && !s.bank.isOpen();
	}

	@Override
	public boolean activate() {
		try {
			s.bank.open();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
