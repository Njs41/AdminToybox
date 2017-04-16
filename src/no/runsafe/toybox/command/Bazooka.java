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
import no.runsafe.framework.api.minecraft.RunsafeEntityType;
import no.runsafe.framework.api.player.IPlayer;

public class Bazooka extends PlayerCommand
{
	public Bazooka(IScheduler scheduler)
	{
		super("bazooka",
			"Fire an entity and make it explode",
			"runsafe.toybox.bazooka",
			new EntityType().require(),
			new WholeNumber("delay").require(),
			new DecimalNumber("strength").require()
		);
		this.scheduler = scheduler;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		final IWorld world = executor.getWorld();
		if (world == null)
			return null;
		final Integer delay = parameters.getValue("delay");
		final Float strength = parameters.getValue("strength");
		final IEntity projectile = executor.Launch((RunsafeEntityType) parameters.getValue("entityType"));
		if (projectile != null && delay != null && strength != null)
		{
			scheduler.startSyncTask(
				new Runnable()
				{
					@Override
					public void run()
					{
						ILocation location = projectile.getLocation();
						world.createExplosion(location, strength, false);
						projectile.remove();
					}
				},
				delay
			);
		}
		return null;
	}

	private final IScheduler scheduler;
}
