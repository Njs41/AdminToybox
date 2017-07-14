package no.runsafe.toybox.command;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.command.argument.DecimalNumber;
import no.runsafe.framework.api.command.argument.EntityType;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.WholeNumber;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.player.IPlayer;

public class Bazooka extends PlayerCommand
{
	public Bazooka(IScheduler scheduler)
	{
		super(
			"bazooka", "Fire an entity and make it explode", "runsafe.toybox.bazooka",
			new EntityType(ENTITY_TYPE).require(),
			new WholeNumber(DELAY).require(),
			new DecimalNumber(STRENGTH).require()
		);
		this.scheduler = scheduler;
	}

	private static final String ENTITY_TYPE = "entityType";
	private static final String DELAY = "delay";
	private static final String STRENGTH = "strength";

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		final IWorld world = executor.getWorld();
		if (world == null)
			return null;
		final Integer delay = parameters.getValue(DELAY);
		final Float strength = parameters.getValue(STRENGTH);
		if (strength == null)
			return "&cInvalid strength.";
		if (strength > 255)
			return "&cMax strength: 255";
		final IEntity projectile = executor.Launch(parameters.getValue(ENTITY_TYPE));
		if (projectile != null && delay != null)
		{
			scheduler.startSyncTask(() ->
				{
					ILocation location = projectile.getLocation();
					world.createExplosion(location, strength, false);
					projectile.remove();
				},
				delay
			);
		}
		return null;
	}

	private final IScheduler scheduler;
}
