package steps;

import org.State;

public class StepBankClose extends StepBank {

	StepBankClose(String state) {
		super(state);
	}

	public boolean shouldActivate() {
		return super.shouldActivate() && !s.inventory.isFull();
	}
	
	@Override
	public boolean activate() {
		if (s.bank.getItemCountForTab(0) >= State.MAX_BANK_SLOTS) {
			s.stop();
		}
		s.bank.close();
		return true;
	}

}
