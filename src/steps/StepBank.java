package steps;

public abstract class StepBank extends Step {

	StepBank(String state) {
		super(state);
	}

	@Override
	public boolean shouldActivate() {
		return s.bank.isOpen();
	}

	@Override
	public abstract boolean activate();

}
