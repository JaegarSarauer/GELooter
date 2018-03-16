package steps;

import org.Main;
import org.State;
import org.osbot.rs07.script.Script;

public abstract class Step {
	
	protected Script s;
	protected String state;
	
	Step(String state) {
		this.state = state;
		s = Main.script;
	}

	public abstract boolean shouldActivate();
	
	public final void updateState() {
		if (this.state != null)
			State.i.state = this.state;
	}
	
	public abstract boolean activate();
}
