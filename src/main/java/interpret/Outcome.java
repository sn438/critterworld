package interpret;

import simulation.Critter;

/** An example interface for representing an outcome of interpreting a critter program. */
public interface Outcome
{
	/**
	 * Applies the effects of this Outcome to a critter.
	 * @param c the critter that this outcome will be applied to
	 * @return whether or not this outcome can be applied to critter {@code c}
	 */
	boolean applyOutcome(Critter c);
}