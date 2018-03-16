package steps;

import org.State;

/*
 * 
 * Used for updating over every few minutes.
 */
public class StepSlowUpdate extends Step {

    StepSlowUpdate(String state) {
		super(state);
	}

	private long lastSlowUpdate = System.currentTimeMillis();
    private long nextUpdate = 0;
    private long now;

	@Override
	public boolean shouldActivate() {
		now = System.currentTimeMillis();
		return (lastSlowUpdate + nextUpdate > now);
	}

	@Override
	public boolean activate() {
    		//insert updates here
		State.i.minRunAmount = (int) (4 + (Math.random() * 30));
		State.i.minNoItemWait = (int) (10000 + (Math.random() * 40000));
    		//insert updates here
		lastSlowUpdate = now;
		nextUpdate = (long) (180000l * Math.random());
		return true;
	}

}
