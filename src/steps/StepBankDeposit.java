package steps;

public class StepBankDeposit extends StepBank {

	StepBankDeposit(String state) {
		super(state);
	}

	public boolean shouldActivate() {
		return super.shouldActivate() && s.inventory.isFull() && s.bank.isOpen();
	}
	
	@Override
	public boolean activate() {
		s.bank.depositAll();
		return true;
	}

}
