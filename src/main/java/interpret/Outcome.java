package interpret;

import simulation.SimpleCritter;
import simulation.SimpleWorld;

/** An example interface for representing an outcome of interpreting a critter program. */
public interface Outcome
{
	/**
	 * Applies the effects of this Outcome to a critter.
	 * @param c the critter that this outcome will be applied to
	 * @param w
	 */
	public void applyOutcome(SimpleCritter c, SimpleWorld w);
}